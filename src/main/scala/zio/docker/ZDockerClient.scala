package zio.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.exception.DockerException
import com.github.dockerjava.core.{DefaultDockerClientConfig, DockerClientBuilder, DockerClientConfig}
import zio._

import java.util.Properties

trait ZDockerClient:
  def conf: UIO[DockerClientConfig]
  def client: UIO[DockerClient]
  def close: UIO[Unit]

object ZDockerClient:

  protected[this] case class Live(dockerConf: DockerClientConfig) extends ZDockerClient {

    val conf: UIO[DockerClientConfig] =
      ZIO.attempt(dockerConf).orDie

    val client: UIO[DockerClient] =
      conf.map(conf => DockerClientBuilder.getInstance(conf)).flatMap(builder => ZIO.attempt(builder.build).orDie)

    def close: UIO[Unit] =
      client.map(_.close)
  }

  def make(dockerConf: DockerClientConfig): ZIO[Scope, Throwable, ZDockerClient] =
    ZIO.acquireRelease {
      for {
        conf          <- ZIO.attempt(dockerConf)
        zDockerClient <- ZIO.attempt(Live(conf))
      } yield zDockerClient
    }(_.close)

  def live(dockerConf: DockerClientConfig): RLayer[DefaultDockerClientConfig, ZDockerClient] =
    ZLayer.scoped(make(dockerConf))

  def live(props: Properties): RLayer[DefaultDockerClientConfig, ZDockerClient] =
    live(DefaultDockerClientConfig.createDefaultConfigBuilder.withProperties(props).build)

  def default: ZLayer[Any, Throwable, ZDockerClient] = ZLayer.scoped {
    ZIO.acquireRelease(ZIO.attempt(Live(DefaultDockerClientConfig.createDefaultConfigBuilder.withProperties(new Properties).build)))(_.close)
  }

  def live(dockerSettings: DockerSettings): RLayer[DefaultDockerClientConfig, ZDockerClient] =
    ZLayer.scoped {
      (for {
        settings <- ZIO.attempt(dockerSettings)
        dockerConf <- for {
                        builder <- ZIO.attempt(DefaultDockerClientConfig.createDefaultConfigBuilder)
                        conf <- settings match {
                                  case default: DefaultDockerSettings =>
                                    ZIO.attempt {
                                      builder
                                        .withDockerHost(default.dockerHost)
                                        .withDockerTlsVerify(default.dockerTlsVerify)
                                        .withDockerCertPath(default.dockerCertPath)
                                    }
                                  case registry: RegistryDockerSettings =>
                                    ZIO.attempt {
                                      builder
                                        .withDockerHost(registry.dockerHost)
                                        .withDockerTlsVerify(registry.dockerTlsVerify)
                                        .withDockerCertPath(registry.dockerCertPath)
                                        .withRegistryUrl(registry.registryUrl)
                                        .withRegistryEmail(registry.registryEmail)
                                        .withRegistryUsername(registry.registryUsername)
                                        .withRegistryPassword(registry.registryPassword)
                            }
                        }
                      } yield conf.build
      } yield dockerConf).flatMap(conf => make(conf))
    }

  def conf: RIO[DockerClientConfig with ZDockerClient, DockerClientConfig] =
    service(_.conf)

  def client: RIO[DockerClientConfig with ZDockerClient, DockerClient] =
    service(_.client)

  def close: RIO[DockerClientConfig with ZDockerClient, Unit] =
    service(_.close)

  def service[R, A](r: ZDockerClient => RIO[R, A]): RIO[R with ZDockerClient, A] =
    ZIO.serviceWithZIO[ZDockerClient](r)

  def make: ZIO[ZDockerClient, DockerException, DockerClient] =
    ZIO.environmentWithZIO[ZDockerClient](_.get.client)