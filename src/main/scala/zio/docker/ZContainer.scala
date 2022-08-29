package zio.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.command._
import zio.docker.cmd._
import zio.docker.cmd.util.WaitResponseResultCallback
import zio.{RIO, RLayer, Scope, Task, UIO, ZIO, ZLayer}

trait ZContainer(client: DockerClient) extends ZDockerCmdAccessor:

  def listContainerCmd[R](cmd: ListContainersCmdSettings): RIO[R, Task[ListContainersCmd]]

  def createContainerCmd[R](cmd: CreateContainerCmdSettings): RIO[R, Task[CreateContainerCmd]]

  def startContainerCmd[R](cmd: StartContainerCmdSettings): RIO[R, Task[StartContainerCmd]]

  def inspectContainerCmd[R](cmd: InspectContainerCmdSettings): RIO[R, Task[InspectContainerCmd]]

  def removeContainerCmd[R](cmd: RemoveContainerCmdSettings): RIO[R, Task[RemoveContainerCmd]]

  def waitContainerCmd[R](cmd: WaitContainerCmdSettings): RIO[R, Task[(WaitContainerCmd, WaitResponseResultCallback)]]

  def attachContainerCmd[R](cmd: AttachContainerCmdSettings): RIO[R, Task[(AttachContainerCmd, WaitResponseResultCallback)]]

  def logContainerCmd[R](cmd: LogContainerCmdSettings): RIO[R, Task[(LogContainerCmd, WaitResponseResultCallback)]]

  def copyArchiveFromContainerCmd[R](cmd: CopyArchiveFromContainerCmdSettings): RIO[R, Task[CopyArchiveFromContainerCmd]]

  def copyArchiveToContainerCmd[R](cmd: CopyArchiveToContainerCmdSettings): RIO[R, Task[CopyArchiveToContainerCmd]]

  def containerDiffCmd[R](cmd: ContainerDiffCmdSettings): RIO[R, Task[ContainerDiffCmd]]

  def stopContainerCmd[R](cmd: StopContainerCmdSettings): RIO[R, Task[StopContainerCmd]]

  def killContainerCmd[R](cmd: KillContainerCmdSettings): RIO[R, Task[KillContainerCmd]]

  def updateContainerCmd[R](cmd: UpdateContainerCmdSettings): RIO[R, Task[UpdateContainerCmd]]

  def renameContainerCmd[R](cmd: RenameContainerCmdSettings): RIO[R, Task[RenameContainerCmd]]

  def restartContainerCmd[R](cmd: RestartContainerCmdSettings): RIO[R, Task[RestartContainerCmd]]

  def pauseContainerCmd[R](cmd: PauseContainerCmdSettings): RIO[R, Task[PauseContainerCmd]]

  def unpauseContainerCmd[R](cmd: UnpauseContainerCmdSettings): RIO[R, Task[UnpauseContainerCmd]]

  def close: UIO[Unit] = ZIO.succeed(client.close)

object ZContainer:

  private[this] case class Live(client: DockerClient) extends ZContainer(client):

    def listContainerCmd[R](cmd: ListContainersCmdSettings): RIO[R, Task[ListContainersCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        listContainers <- liftUnsafe(client.listContainersCmd)
        listContainersCmd <- attemptBlockingUnsafe(settings(listContainers))
      } yield listContainersCmd

    def createContainerCmd[R](cmd: CreateContainerCmdSettings): RIO[R, Task[CreateContainerCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        createContainer <- liftUnsafe(client.createContainerCmd(cmd.image))
        createContainerCmd <- attemptBlockingUnsafe(settings(createContainer))
      } yield createContainerCmd

    def startContainerCmd[R](cmd: StartContainerCmdSettings): RIO[R, Task[StartContainerCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        startContainer <- liftUnsafe(client.startContainerCmd(cmd.container))
        startContainerCmd <- attemptBlockingUnsafe(settings(startContainer))
      } yield startContainerCmd

    def inspectContainerCmd[R](cmd: InspectContainerCmdSettings): RIO[R, Task[InspectContainerCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        inspectContainer <- liftUnsafe(client.inspectContainerCmd(cmd.container))
        inspectContainerCmd <- attemptBlockingUnsafe(settings(inspectContainer))
      } yield inspectContainerCmd

    def removeContainerCmd[R](cmd: RemoveContainerCmdSettings): RIO[R, Task[RemoveContainerCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        removeContainer <- liftUnsafe(client.removeContainerCmd(cmd.container))
        removeContainerCmd <- attemptBlockingUnsafe(settings(removeContainer))
      } yield removeContainerCmd

    def waitContainerCmd[R](cmd: WaitContainerCmdSettings): RIO[R, Task[(WaitContainerCmd, WaitResponseResultCallback)]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        callback <- ZIO.attempt(cmd.callback)
        waitContainer <- liftUnsafe(client.waitContainerCmd(cmd.container))
        waitContainerCallback <- attemptBlockingUnsafe(settings(waitContainer) -> callback)
      } yield waitContainerCallback

    def attachContainerCmd[R](cmd: AttachContainerCmdSettings): RIO[R, Task[(AttachContainerCmd, WaitResponseResultCallback)]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        callback <- ZIO.attempt(cmd.callback)
        attachContainer <- liftUnsafe(client.attachContainerCmd(cmd.container))
        attachContainerCallback <- attemptBlockingUnsafe(settings(attachContainer) -> callback)
      } yield attachContainerCallback

    def logContainerCmd[R](cmd: LogContainerCmdSettings): RIO[R, Task[(LogContainerCmd, WaitResponseResultCallback)]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        callback <- ZIO.attempt(cmd.callback)
        logContainer <- liftUnsafe(client.logContainerCmd(cmd.container))
        logContainerCallback <- attemptBlockingUnsafe(settings(logContainer) -> callback)
      } yield logContainerCallback

    def copyArchiveFromContainerCmd[R](cmd: CopyArchiveFromContainerCmdSettings): RIO[R, Task[CopyArchiveFromContainerCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        copyArchiveFromContainer <- liftUnsafe(client.copyArchiveFromContainerCmd(cmd.container, cmd.resource))
        copyArchiveFromContainerCmd <- attemptBlockingUnsafe(settings(copyArchiveFromContainer))
      } yield copyArchiveFromContainerCmd

    def copyArchiveToContainerCmd[R](cmd: CopyArchiveToContainerCmdSettings): RIO[R, Task[CopyArchiveToContainerCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        copyArchiveToContainer <- liftUnsafe(client.copyArchiveToContainerCmd(cmd.container))
        copyArchiveToContainerCmd <- attemptBlockingUnsafe(settings(copyArchiveToContainer))
      } yield copyArchiveToContainerCmd

    def containerDiffCmd[R](cmd: ContainerDiffCmdSettings): RIO[R, Task[ContainerDiffCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        containerDiff <- liftUnsafe(client.containerDiffCmd(cmd.container))
        containerDiffCmd <- attemptBlockingUnsafe(settings(containerDiff))
      } yield containerDiffCmd

    def stopContainerCmd[R](cmd: StopContainerCmdSettings): RIO[R, Task[StopContainerCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        stopContainer <- liftUnsafe(client.stopContainerCmd(cmd.container))
        stopContainerCmd <- attemptBlockingUnsafe(settings(stopContainer))
      } yield stopContainerCmd

    def killContainerCmd[R](cmd: KillContainerCmdSettings): RIO[R, Task[KillContainerCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        killContainer <- liftUnsafe(client.killContainerCmd(cmd.container))
        killContainerCmd <- attemptBlockingUnsafe(settings(killContainer))
      } yield killContainerCmd

    def updateContainerCmd[R](cmd: UpdateContainerCmdSettings): RIO[R, Task[UpdateContainerCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        updateContainer <- liftUnsafe(client.updateContainerCmd(cmd.container))
        updateContainerCmd <- attemptBlockingUnsafe(settings(updateContainer))
      } yield updateContainerCmd

    def renameContainerCmd[R](cmd: RenameContainerCmdSettings): RIO[R, Task[RenameContainerCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        renameContainer <- liftUnsafe(client.renameContainerCmd(cmd.container))
        renameContainerCmd <- attemptBlockingUnsafe(settings(renameContainer))
      } yield renameContainerCmd

    def restartContainerCmd[R](cmd: RestartContainerCmdSettings): RIO[R, Task[RestartContainerCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        restartContainer <- liftUnsafe(client.restartContainerCmd(cmd.container))
        restartContainerCmd <- attemptBlockingUnsafe(settings(restartContainer))
      } yield restartContainerCmd

    def pauseContainerCmd[R](cmd: PauseContainerCmdSettings): RIO[R, Task[PauseContainerCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        pauseContainer <- liftUnsafe(client.pauseContainerCmd(cmd.container))
        pauseContainerCmd <- attemptBlockingUnsafe(settings(pauseContainer))
      } yield pauseContainerCmd

    def unpauseContainerCmd[R](cmd: UnpauseContainerCmdSettings): RIO[R, Task[UnpauseContainerCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        unpauseContainer <- liftUnsafe(client.unpauseContainerCmd(cmd.container))
        unpauseContainerCmd <- attemptBlockingUnsafe(settings(unpauseContainer))
      } yield unpauseContainerCmd

  def listContainerCmd(cmd: ListContainersCmdSettings): RIO[DockerClient with ZContainer, ListContainersCmd] =
    service(_.listContainerCmd(cmd)).flatten

  def listContainerCmd1(cmd: ListContainersCmdSettings): RIO[DockerClient with ZContainer, ListContainersCmd] =
    service(_.listContainerCmd(cmd)).flatten

  def createContainerCmd(cmd: CreateContainerCmdSettings): RIO[DockerClient with ZContainer, CreateContainerCmd] =
    service(_.createContainerCmd(cmd)).flatten

  def startContainerCmd(cmd: StartContainerCmdSettings): RIO[DockerClient with ZContainer, StartContainerCmd] =
    service(_.startContainerCmd(cmd)).flatten

  def inspectContainerCmd(cmd: InspectContainerCmdSettings): RIO[DockerClient with ZContainer, InspectContainerCmd] =
    service(_.inspectContainerCmd(cmd)).flatten

  def removeContainerCmd(cmd: RemoveContainerCmdSettings): RIO[DockerClient with ZContainer, RemoveContainerCmd] =
    service(_.removeContainerCmd(cmd)).flatten

  def waitContainerCmd(cmd: WaitContainerCmdSettings): RIO[DockerClient with ZContainer, (WaitContainerCmd, WaitResponseResultCallback)] =
    service(_.waitContainerCmd(cmd)).flatten

  def attachContainerCmd(cmd: AttachContainerCmdSettings): RIO[DockerClient with ZContainer, (AttachContainerCmd, WaitResponseResultCallback)] =
    service(_.attachContainerCmd(cmd)).flatten

  def logContainerCmd(cmd: LogContainerCmdSettings): RIO[DockerClient with ZContainer, (LogContainerCmd, WaitResponseResultCallback)] =
    service(_.logContainerCmd(cmd)).flatten

  def copyArchiveFromContainerCmd(cmd: CopyArchiveFromContainerCmdSettings): RIO[DockerClient with ZContainer, CopyArchiveFromContainerCmd] =
    service(_.copyArchiveFromContainerCmd(cmd)).flatten

  def copyArchiveToContainerCmd(cmd: CopyArchiveToContainerCmdSettings): RIO[DockerClient with ZContainer, CopyArchiveToContainerCmd] =
    service(_.copyArchiveToContainerCmd(cmd)).flatten

  def containerDiffCmd(cmd: ContainerDiffCmdSettings): RIO[DockerClient with ZContainer, ContainerDiffCmd] =
    service(_.containerDiffCmd(cmd)).flatten

  def stopContainerCmd(cmd: StopContainerCmdSettings): RIO[DockerClient with ZContainer, StopContainerCmd] =
    service(_.stopContainerCmd(cmd)).flatten

  def killContainerCmd(cmd: KillContainerCmdSettings): RIO[DockerClient with ZContainer, KillContainerCmd] =
    service(_.killContainerCmd(cmd)).flatten

  def updateContainerCmd(cmd: UpdateContainerCmdSettings): RIO[DockerClient with ZContainer, UpdateContainerCmd] =
    service(_.updateContainerCmd(cmd)).flatten

  def renameContainerCmd(cmd: RenameContainerCmdSettings): RIO[DockerClient with ZContainer, RenameContainerCmd] =
    service(_.renameContainerCmd(cmd)).flatten

  def restartContainerCmd(cmd: RestartContainerCmdSettings): RIO[DockerClient with ZContainer, RestartContainerCmd] =
    service(_.restartContainerCmd(cmd)).flatten

  def pauseContainerCmd(cmd: PauseContainerCmdSettings): RIO[DockerClient with ZContainer, PauseContainerCmd] =
    service(_.pauseContainerCmd(cmd)).flatten

  def unpauseContainerCmd(cmd: UnpauseContainerCmdSettings): RIO[DockerClient with ZContainer, UnpauseContainerCmd] =
    service(_.unpauseContainerCmd(cmd)).flatten

  def make(client: DockerClient): ZIO[Scope, Throwable, ZContainer] =
    ZIO.acquireRelease(ZIO.attempt(client).map(Live(_)))(client => client.close)

  val live: RLayer[DockerClient, ZContainer] =
    ZLayer.scoped {
      for {
        client <- ZIO.service[DockerClient]
        container <- make(client)
      } yield container
    }

  def service[R, A](r: ZContainer => RIO[R, A]): RIO[R with ZContainer, A] =
    ZIO.serviceWithZIO[ZContainer](r)
