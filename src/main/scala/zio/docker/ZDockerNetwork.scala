package zio.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.command._
import zio.docker.cmd._
import zio.{RIO, RLayer, Scope, Task, UIO, ZIO, ZLayer}

trait ZDockerNetwork(client: DockerClient) extends ZDockerCmdAccessor:
  
  def connectToNetworkCmd[R](cmd: ConnectToNetworkCmdSettings): RIO[R, Task[ConnectToNetworkCmd]]

  def createNetworkCmd[R](cmd: CreateNetworkCmdSettings): RIO[R, Task[CreateNetworkCmd]]

  def removeNetworkCmd[R](cmd: RemoveNetworkCmdSettings): RIO[R, Task[RemoveNetworkCmd]]

  def listNetworksCmd[R](cmd: ListNetworksCmdSettings): RIO[R, Task[ListNetworksCmd]]

  def inspectNetworkCmd[R](cmd: InspectNetworkCmdSettings): RIO[R, Task[InspectNetworkCmd]]

  def disconnectFromNetworkCmd[R](cmd: DisconnectFromNetworkCmdSettings): RIO[R, Task[DisconnectFromNetworkCmd]]

  def close: UIO[Unit] = ZIO.succeed(client.close)

object ZDockerNetwork:

  protected[this] case class Live(client: DockerClient) extends ZDockerNetwork(client) {

    def connectToNetworkCmd[R](cmd: ConnectToNetworkCmdSettings): RIO[R, Task[ConnectToNetworkCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        connectToNetwork <- liftUnsafe(client.connectToNetworkCmd)
        connectToNetworkCmd <- attemptBlockingUnsafe(settings(connectToNetwork))
      } yield connectToNetworkCmd

    def createNetworkCmd[R](cmd: CreateNetworkCmdSettings): RIO[R, Task[CreateNetworkCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        createNetwork <- liftUnsafe(client.createNetworkCmd)
        createNetworkCmd <- attemptBlockingUnsafe(settings(createNetwork))
      } yield createNetworkCmd

    def removeNetworkCmd[R](cmd: RemoveNetworkCmdSettings): RIO[R, Task[RemoveNetworkCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        removeNetwork <- liftUnsafe(client.removeNetworkCmd(cmd.network))
        removeNetworkCmd <- attemptBlockingUnsafe(settings(removeNetwork))
      } yield removeNetworkCmd

    def listNetworksCmd[R](cmd: ListNetworksCmdSettings): RIO[R, Task[ListNetworksCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        listNetworks <- liftUnsafe(client.listNetworksCmd)
        listNetworksCmd <- attemptBlockingUnsafe(settings(listNetworks))
      } yield listNetworksCmd

    def inspectNetworkCmd[R](cmd: InspectNetworkCmdSettings): RIO[R, Task[InspectNetworkCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        inspectNetwork <- liftUnsafe(client.inspectNetworkCmd)
        inspectNetworkCmd <- attemptBlockingUnsafe(settings(inspectNetwork))
      } yield inspectNetworkCmd

    def disconnectFromNetworkCmd[R](cmd: DisconnectFromNetworkCmdSettings): RIO[R, Task[DisconnectFromNetworkCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        disconnectFromNetwork <- liftUnsafe(client.disconnectFromNetworkCmd)
        disconnectFromNetworkCmd <- attemptBlockingUnsafe(settings(disconnectFromNetwork))
      } yield disconnectFromNetworkCmd
  }

  def connectToNetworkCmd(cmd: ConnectToNetworkCmdSettings): RIO[DockerClient with ZDockerNetwork, ConnectToNetworkCmd] =
    service(_.connectToNetworkCmd(cmd)).flatten

  def createNetworkCmd(cmd: CreateNetworkCmdSettings): RIO[DockerClient with ZDockerNetwork, CreateNetworkCmd] =
    service(_.createNetworkCmd(cmd)).flatten

  def removeNetworkCmd(cmd: RemoveNetworkCmdSettings): RIO[DockerClient with ZDockerNetwork, RemoveNetworkCmd] =
    service(_.removeNetworkCmd(cmd)).flatten

  def listNetworksCmd(cmd: ListNetworksCmdSettings): RIO[DockerClient with ZDockerNetwork, ListNetworksCmd] =
    service(_.listNetworksCmd(cmd)).flatten

  def inspectNetworkCmd(cmd: InspectNetworkCmdSettings): RIO[DockerClient with ZDockerNetwork, InspectNetworkCmd] =
    service(_.inspectNetworkCmd(cmd)).flatten

  def disconnectFromNetworkCmd(cmd: DisconnectFromNetworkCmdSettings): RIO[DockerClient with ZDockerNetwork, DisconnectFromNetworkCmd] =
    service(_.disconnectFromNetworkCmd(cmd)).flatten

  def make(client: DockerClient): ZIO[Scope, Throwable, ZDockerNetwork] =
    ZIO.acquireRelease(ZIO.attempt(client).map(Live(_)))(client => client.close)

  def live(dockerClient: DockerClient): RLayer[DockerClient, ZDockerNetwork] =
    ZLayer.scoped {
      for {
        client <- ZIO.attempt(dockerClient)
        network <- make(client)
      } yield network
    }

  def service[R, A](r: ZDockerNetwork => RIO[R, A]): RIO[R with ZDockerNetwork, A] =
    ZIO.serviceWithZIO[ZDockerNetwork](r)