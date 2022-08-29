package zio.docker.cmd

import com.github.dockerjava.api.command._
import com.github.dockerjava.api.model._
import zio._
import zio.docker.cmd.util._

import scala.jdk.CollectionConverters._
import scala.util.Try

object ZDockerCmdExec {

  trait ContainerService:
    def createContainerExec(createContainerCmd: CreateContainerCmd): Task[CreateContainerResponse]

    def listContainerExec(listContainersCmd: ListContainersCmd): Task[Seq[Container]]

    def startContainerExec(startContainerCmd: StartContainerCmd): Task[Unit]

    def pauseContainerExec(pauseContainerCmd: PauseContainerCmd): Task[Unit]

    def unpauseContainerExec(unpauseContainerCmd: UnpauseContainerCmd): Task[Unit]

    def inspectContainerExec(inspectContainerCmd: InspectContainerCmd): Task[InspectContainerResponse]

    def removeContainerExec(removeContainerCmd: RemoveContainerCmd): Task[Unit]

    def waitContainerExec(waitContainerCmd: WaitContainerCmd, callback: WaitResponseResultCallback): Task[WaitResponseResultCallback]

    def attachContainerExec(attachContainerCmd: AttachContainerCmd, callback: FrameResultCallback): Task[FrameResultCallback]

    def inspectExecExec(inspectExecCmd: InspectExecCmd): Task[InspectExecResponse]

    def logContainerExec(logContainerCmd: LogContainerCmd, callback: FrameResultCallback): Task[FrameResultCallback]

    def copyArchiveFromContainerExec(copyArchiveFromContainerCmd: CopyArchiveFromContainerCmd): Task[Unit]

    def copyArchiveToContainerExec(copyArchiveToContainerCmd: CopyArchiveToContainerCmd): Task[Unit]

    def containerDiffExec(containerDiffCmd: ContainerDiffCmd): Task[Seq[ChangeLog]]

    def stopContainerExec(stopContainerCmd: StopContainerCmd): Task[Unit]

    def killContainerExec(killContainerCmd: KillContainerCmd): Task[Unit]

    def updateContainerExec(updateContainerCmd: UpdateContainerCmd): Task[UpdateContainerResponse]

    def renameContainerExec(renameContainerCmd: RenameContainerCmd): Task[Unit]

    def restartContainerExec(restartContainerCmd: RestartContainerCmd): Task[Unit]

  object ContainerService:

    protected[this] case object Live extends ContainerService {

      def createContainerExec(createContainerCmd: CreateContainerCmd): Task[CreateContainerResponse] =
        ZIO.fromTry {
          Try(createContainerCmd.exec)
        }

      def listContainerExec(listContainersCmd: ListContainersCmd): Task[Seq[Container]] =
        ZIO.fromTry {
          Try(listContainersCmd.exec.asScala.toSeq)
        }

      def startContainerExec(startContainerCmd: StartContainerCmd): Task[Unit] =
        ZIO.fromTry {
          Try(startContainerCmd.exec).map(_ => ())
        }

      def pauseContainerExec(pauseContainerCmd: PauseContainerCmd): Task[Unit] =
        ZIO.fromTry {
          Try(pauseContainerCmd.exec).map(_ => ())
        }

      def unpauseContainerExec(unpauseContainerCmd: UnpauseContainerCmd): Task[Unit] =
        ZIO.fromTry {
          Try(unpauseContainerCmd.exec).map(_ => ())
        }

      def inspectContainerExec(inspectContainerCmd: InspectContainerCmd): Task[InspectContainerResponse] =
        ZIO.fromTry {
          Try(inspectContainerCmd.exec)
        }

      def removeContainerExec(removeContainerCmd: RemoveContainerCmd): Task[Unit] =
        ZIO.fromTry {
          Try(removeContainerCmd.exec).map(_ => ())
        }

      def waitContainerExec(waitContainerCmd: WaitContainerCmd,
                            callback: WaitResponseResultCallback): Task[WaitResponseResultCallback] =
        ZIO.fromTry {
          Try(waitContainerCmd.exec(callback))
        }

      def attachContainerExec(attachContainerCmd: AttachContainerCmd,
                              callback: FrameResultCallback): Task[FrameResultCallback] =
        ZIO.fromTry {
          Try(attachContainerCmd.exec(callback))
        }

      def logContainerExec(logContainerCmd: LogContainerCmd, callback: FrameResultCallback): Task[FrameResultCallback] =
        ZIO.fromTry {
          Try(logContainerCmd.exec(callback))
        }

      def inspectExecExec(inspectExecCmd: InspectExecCmd): Task[InspectExecResponse] =
        ZIO.fromTry {
          Try(inspectExecCmd.exec)
        }

      def copyArchiveFromContainerExec(copyArchiveFromContainerCmd: CopyArchiveFromContainerCmd): Task[Unit] =
        ZIO.fromTry {
          Try(copyArchiveFromContainerCmd.exec).map(_ => ())
        }

      def copyArchiveToContainerExec(copyArchiveToContainerCmd: CopyArchiveToContainerCmd): Task[Unit] =
        ZIO.fromTry {
          Try(copyArchiveToContainerCmd.exec).map(_ => ())
        }

      def containerDiffExec(containerDiffCmd: ContainerDiffCmd): Task[Seq[ChangeLog]] =
        ZIO.fromTry {
          Try(containerDiffCmd.exec.asScala.toSeq)
        }

      def stopContainerExec(stopContainerCmd: StopContainerCmd): Task[Unit] =
        ZIO.fromTry {
          Try(stopContainerCmd.exec).map(_ => ())
        }

      def killContainerExec(killContainerCmd: KillContainerCmd): Task[Unit] =
        ZIO.fromTry {
          Try(killContainerCmd.exec).map(_ => ())
        }

      def updateContainerExec(updateContainerCmd: UpdateContainerCmd): Task[UpdateContainerResponse] =
        ZIO.fromTry {
          Try(updateContainerCmd.exec)
        }

      def renameContainerExec(renameContainerCmd: RenameContainerCmd): Task[Unit] =
        ZIO.fromTry {
          Try(renameContainerCmd.exec).map(_ => ())
        }

      def restartContainerExec(restartContainerCmd: RestartContainerCmd): Task[Unit] =
        ZIO.fromTry {
          Try(restartContainerCmd.exec).map(_ => ())
        }

    def service[R, A](r: ContainerService => RIO[R, A]): RIO[R with ContainerService, A] =
      ZIO.serviceWithZIO[ContainerService](r)

    val live: RLayer[Nothing, ContainerService] =
      ZLayer.scoped(make)

    def make: ZIO[Scope, Throwable, ContainerService] =
      ZIO.acquireRelease(ZIO.attempt(Live))(_ => ZIO.succeed(()))
  }

  trait ImageService:
    def inspectImageExec(inspectImageApiCmd: InspectImageCmd): Task[InspectImageResponse]

    def loadImageExec(loadImageCmd: LoadImageCmd): Task[Unit]

    def pushImageExec(pushImageCmd: PushImageCmd, callback: PushResponseResultCallback): Task[PushResponseResultCallback]

    def pullImageExec(pullImageCmd: PullImageCmd, callback: PullResponseResultCallback): Task[PullResponseResultCallback]

    def buildImageExec(buildImageCmd: BuildImageCmd, callback: BuildResponseResultCallback): Task[BuildResponseResultCallback]

    def createImageExec(createImageCmd: CreateImageCmd): Task[CreateImageResponse]

    def listImagesExec(listImagesCmd: ListImagesCmd): Task[Seq[Image]]

    def removeImageExec(removeImageCmd: RemoveImageCmd): Task[Unit]

    def saveImageExec(saveImageCmd: SaveImageCmd): Task[ZInputStream]

    def searchImagesExec(searchImagesCmd: SearchImagesCmd): Task[Seq[SearchItem]]

    def tagImageExec(tagImageCmd: TagImageCmd): Task[Unit]

  object ImageService:

    protected[this] case object Live extends ImageService {

      def inspectImageExec(inspectImageCmd: InspectImageCmd): Task[InspectImageResponse] =
        ZIO.fromTry {
          Try(inspectImageCmd.exec)
        }

      def loadImageExec(loadImageCmd: LoadImageCmd): Task[Unit] =
        ZIO.fromTry {
          Try(loadImageCmd.exec).map(_ => ())
        }

      def pushImageExec(pushImageCmd: PushImageCmd, callback: PushResponseResultCallback): Task[PushResponseResultCallback] =
        ZIO.fromTry {
          Try(pushImageCmd.exec(callback))
        }

      def pullImageExec(pullImageCmd: PullImageCmd, callback: PullResponseResultCallback): Task[PullResponseResultCallback] =
        ZIO.fromTry {
          Try(pullImageCmd.exec(callback))
        }

      def buildImageExec(buildImageCmd: BuildImageCmd, callback: BuildResponseResultCallback): Task[BuildResponseResultCallback] =
        ZIO.fromTry {
          Try(buildImageCmd.exec(callback))
        }

      def createImageExec(createImageCmd: CreateImageCmd): Task[CreateImageResponse] =
        ZIO.fromTry {
          Try(createImageCmd.exec)
        }

      def listImagesExec(listImagesCmd: ListImagesCmd): Task[Seq[Image]] =
        ZIO.fromTry {
          Try(listImagesCmd.exec.asScala.toSeq)
        }

      def removeImageExec(removeImageCmd: RemoveImageCmd): Task[Unit] =
        ZIO.fromTry {
          Try(removeImageCmd.exec).map(_ => ())
        }

      def saveImageExec(saveImageCmd: SaveImageCmd): Task[ZInputStream] =
        for {
          is <- ZIO.fromTry(Try(saveImageCmd.exec))
          zIS <- ZIO.succeed(ZInputStream.fromInputStream(is))
        } yield zIS

      def searchImagesExec(searchImagesCmd: SearchImagesCmd): Task[Seq[SearchItem]] =
        ZIO.fromTry {
          Try(searchImagesCmd.exec.asScala.toSeq)
        }

      def tagImageExec(tagImageCmd: TagImageCmd): Task[Unit] =
        ZIO.fromTry {
          Try(tagImageCmd.exec).map(_ => ())
        }
    }

    def service[R, A](r: ImageService => RIO[R, A]): RIO[R with ImageService, A] =
      ZIO.serviceWithZIO[ImageService](r)

    val live: RLayer[Nothing, ImageService] =
      ZLayer.scoped(make)

    def make: ZIO[Scope, Throwable, ImageService] =
      ZIO.acquireRelease(ZIO.attempt(Live))(_ => ZIO.succeed(()))

  trait AuthService {
    def authExec(authSettings: AuthCmd): Task[AuthResponse]
  }

  trait NetworkService:
    def connectToNetworkExec(connectToNetworkCmd: ConnectToNetworkCmd): Task[Unit]

    def createNetworkExec(createNetworkCmd: CreateNetworkCmd): Task[CreateNetworkResponse]

    def removeNetworkExec(removeNetworkCmd: RemoveNetworkCmd): Task[Unit]

    def listNetworksExec(listNetworksCmd: ListNetworksCmd): Task[Seq[Network]]

    def inspectNetworkExec(inspectNetworkCmd: InspectNetworkCmd): Task[Network]

    def disconnectFromNetworkExec(disconnectFromNetworkCmd: DisconnectFromNetworkCmd): Task[Unit]

  object NetworkService {

    protected[this] case object Live extends NetworkService {

      def connectToNetworkExec(connectToNetworkCmd: ConnectToNetworkCmd): Task[Unit] =
        ZIO.fromTry {
          Try(connectToNetworkCmd.exec).map(_ => ())
        }

      def createNetworkExec(createNetworkCmd: CreateNetworkCmd): Task[CreateNetworkResponse] =
        ZIO.fromTry {
          Try(createNetworkCmd.exec)
        }

      def removeNetworkExec(removeNetworkCmd: RemoveNetworkCmd): Task[Unit] =
        ZIO.fromTry {
          Try(removeNetworkCmd.exec).map(_ => ())
        }

      def listNetworksExec(listNetworksCmd: ListNetworksCmd): Task[Seq[Network]] =
        ZIO.fromTry {
          Try(listNetworksCmd.exec.asScala.toSeq)
        }

      def inspectNetworkExec(inspectNetworkCmd: InspectNetworkCmd): Task[Network] =
        ZIO.fromTry {
          Try(inspectNetworkCmd.exec)
        }

      def disconnectFromNetworkExec(disconnectFromNetworkCmd: DisconnectFromNetworkCmd): Task[Unit] =
        ZIO.fromTry {
          Try(disconnectFromNetworkCmd.exec).map(_ => ())
        }
    }

    def service[R, A](r: NetworkService => RIO[R, A]): RIO[R with NetworkService, A] =
      ZIO.serviceWithZIO[NetworkService](r)

    val live: RLayer[Nothing, NetworkService] =
      ZLayer.scoped(make)

    def make: ZIO[Scope, Throwable, NetworkService] =
      ZIO.acquireRelease(ZIO.attempt(Live))(_ => ZIO.succeed(()))
  }
}