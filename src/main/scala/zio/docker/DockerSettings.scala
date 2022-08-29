package zio.docker

sealed trait DockerSettings:
  def dockerHost: String

  def dockerTlsVerify: Boolean

  def dockerCertPath: String

case class DefaultDockerSettings(dockerHost: String,
                                 dockerTlsVerify: Boolean,
                                 dockerCertPath: String) extends DockerSettings

case class RegistryDockerSettings(dockerHost: String,
                                  dockerTlsVerify: Boolean,
                                  dockerCertPath: String,
                                  registryUsername: String,
                                  registryPassword: String,
                                  registryEmail: String,
                                  registryUrl: String) extends DockerSettings

object DockerSettings {

  def apply(dockerHost: String,
            dockerTlsVerify: Boolean,
            dockerCertPath: String): DockerSettings =
    DefaultDockerSettings(dockerHost, dockerTlsVerify, dockerCertPath)

  def apply(dockerHost: String,
            dockerTlsVerify: Boolean,
            dockerCertPath: String,
            registryUsername: String,
            registryPassword: String,
            registryEmail: String,
            registryUrl: String): DockerSettings =
    RegistryDockerSettings(
      dockerHost,
      dockerTlsVerify,
      dockerCertPath,
      registryUsername,
      registryPassword,
      registryEmail,
      registryUrl
    )
}