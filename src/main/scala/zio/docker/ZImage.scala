package zio.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.command._
import zio.docker.cmd._
import zio.docker.cmd.util.{BuildResponseResultCallback, PullResponseResultCallback, PushResponseResultCallback}
import zio.{RIO, RLayer, Scope, Task, UIO, ZIO, ZLayer}

trait ZImage(client: DockerClient) extends ZDockerCmdAccessor:

  def inspectImageCmd[R](cmd: InspectImageCmdSettings): RIO[R, Task[InspectImageCmd]]

  def loadImageCmd[R](cmd: LoadImageCmdSettings): RIO[R, Task[LoadImageCmd]]

  def pushImageCmd[R](cmd: PushImageCmdSettings): RIO[R, Task[(PushImageCmd, PushResponseResultCallback)]]

  def pullImageCmd[R](cmd: PullImageCmdSettings): RIO[R, Task[(PullImageCmd, PullResponseResultCallback)]]

  def buildImageCmd[R](cmd: BuildImageCmdSettings): RIO[R, Task[(BuildImageCmd, BuildResponseResultCallback)]]

  def createImageCmd[R](cmd: CreateImageCmdSettings): RIO[R, Task[CreateImageCmd]]

  def listImagesCmd[R](cmd: ListImagesCmdSettings): RIO[R, Task[ListImagesCmd]]

  def removeImageCmd[R](cmd: RemoveImageCmdSettings): RIO[R, Task[RemoveImageCmd]]

  def saveImageCmd[R](cmd: SaveImageCmdSettings): RIO[R, Task[SaveImageCmd]]

  def searchImageCmd[R](cmd: SearchImagesCmdSettings): RIO[R, Task[SearchImagesCmd]]

  def tagImageCmd[R](cmd: TagImageCmdSettings): RIO[R, Task[TagImageCmd]]

  def close: UIO[Unit] = ZIO.succeed(client.close)

object ZImage:

  protected[this] case class Live(override val client: DockerClient) extends ZImage(client) {

    def inspectImageCmd[R](cmd: InspectImageCmdSettings): RIO[R, Task[InspectImageCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        inspectImage <- liftUnsafe(client.inspectImageCmd(cmd.image))
        inspectImageCmd <- attemptBlockingUnsafe(settings(inspectImage))
      } yield inspectImageCmd

    def loadImageCmd[R](cmd: LoadImageCmdSettings): RIO[R, Task[LoadImageCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        loadImage <- liftUnsafe(client.loadImageCmd(cmd.is))
        loadImageCmd <- attemptBlockingUnsafe(settings(loadImage))
      } yield loadImageCmd

    def pushImageCmd[R](cmd: PushImageCmdSettings): RIO[R, Task[(PushImageCmd, PushResponseResultCallback)]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        callback <- ZIO.attempt(cmd.callback)
        pushImage <- liftUnsafe(client.pushImageCmd(cmd.repo))
        pushRespCallback <- attemptBlockingUnsafe(settings(pushImage) -> callback)
      } yield pushRespCallback

    def pullImageCmd[R](cmd: PullImageCmdSettings): RIO[R, Task[(PullImageCmd, PullResponseResultCallback)]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        callback <- ZIO.attempt(cmd.callback)
        pullImage <- liftUnsafe(client.pullImageCmd(cmd.repo))
        pullRespCallback <- attemptBlockingUnsafe(settings(pullImage) -> callback)
      } yield pullRespCallback

    def buildImageCmd[R](cmd: BuildImageCmdSettings): RIO[R, Task[(BuildImageCmd, BuildResponseResultCallback)]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        callback <- ZIO.attempt(cmd.callback)
        buildImage <- liftUnsafe(client.buildImageCmd(cmd.is))
        buildRespCallback <- attemptBlockingUnsafe(settings(buildImage) -> callback)
      } yield buildRespCallback

    def createImageCmd[R](cmd: CreateImageCmdSettings): RIO[R, Task[CreateImageCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        createImage <- liftUnsafe(client.createImageCmd(cmd.repo, cmd.imgIs))
        createImageCmd <- attemptBlockingUnsafe(settings(createImage))
      } yield createImageCmd

    def listImagesCmd[R](cmd: ListImagesCmdSettings): RIO[R, Task[ListImagesCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        listImages <- liftUnsafe(client.listImagesCmd)
        listImagesCmd <- attemptBlockingUnsafe(settings(listImages))
      } yield listImagesCmd

    def removeImageCmd[R](cmd: RemoveImageCmdSettings): RIO[R, Task[RemoveImageCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        removeImage <- liftUnsafe(client.removeImageCmd(cmd.image))
        removeImageCmd <- attemptBlockingUnsafe(settings(removeImage))
      } yield removeImageCmd

    def saveImageCmd[R](cmd: SaveImageCmdSettings): RIO[R, Task[SaveImageCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        saveImage <- liftUnsafe(client.saveImageCmd(cmd.image))
        saveImageCmd <- attemptBlockingUnsafe(settings(saveImage))
      } yield saveImageCmd

    def searchImageCmd[R](cmd: SearchImagesCmdSettings): RIO[R, Task[SearchImagesCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        searchImage <- liftUnsafe(client.searchImagesCmd(cmd.image))
        searchImageCmd <- attemptBlockingUnsafe(settings(searchImage))
      } yield searchImageCmd

    def tagImageCmd[R](cmd: TagImageCmdSettings): RIO[R, Task[TagImageCmd]] =
      for {
        settings <- ZIO.attempt(cmd.settings)
        tagImage <- liftUnsafe(client.tagImageCmd(cmd.image, cmd.repo, cmd.tag))
        tagImageCmd <- attemptBlockingUnsafe(settings(tagImage))
      } yield tagImageCmd
  }

  def inspectImageCmd(cmd: InspectImageCmdSettings): RIO[DockerClient with ZImage, InspectImageCmd] =
    service(_.inspectImageCmd(cmd)).flatten

  def loadImageCmd(cmd: LoadImageCmdSettings): RIO[DockerClient with ZImage, LoadImageCmd] =
    service(_.loadImageCmd(cmd)).flatten

  def pushImageCmd(cmd: PushImageCmdSettings): RIO[DockerClient with ZImage, (PushImageCmd, PushResponseResultCallback)] =
    service(_.pushImageCmd(cmd)).flatten

  def pullImageCmd(cmd: PullImageCmdSettings): RIO[DockerClient with ZImage, (PullImageCmd, PullResponseResultCallback)] =
    service(_.pullImageCmd(cmd)).flatten

  def buildImageCmd(cmd: BuildImageCmdSettings): RIO[DockerClient with ZImage, (BuildImageCmd, BuildResponseResultCallback)] =
    service(_.buildImageCmd(cmd)).flatten

  def createImageCmd(cmd: CreateImageCmdSettings): RIO[DockerClient with ZImage, CreateImageCmd] =
    service(_.createImageCmd(cmd)).flatten

  def listImagesCmd(cmd: ListImagesCmdSettings): RIO[DockerClient with ZImage, ListImagesCmd] =
    service(_.listImagesCmd(cmd)).flatten

  def removeImageCmd(cmd: RemoveImageCmdSettings): RIO[DockerClient with ZImage, RemoveImageCmd] =
    service(_.removeImageCmd(cmd)).flatten

  def saveImageCmd(cmd: SaveImageCmdSettings): RIO[DockerClient with ZImage, SaveImageCmd] =
    service(_.saveImageCmd(cmd)).flatten

  def searchImageCmd(cmd: SearchImagesCmdSettings): RIO[DockerClient with ZImage, SearchImagesCmd] =
    service(_.searchImageCmd(cmd)).flatten

  def tagImageCmd(cmd: TagImageCmdSettings): RIO[DockerClient with ZImage, TagImageCmd] =
    service(_.tagImageCmd(cmd)).flatten

  def make(client: DockerClient): ZIO[Scope, Throwable, ZImage] =
    ZIO.acquireRelease(ZIO.attempt(client).map(Live(_)))(client => client.close)

  val live: RLayer[DockerClient, ZImage] =
    ZLayer.scoped {
      for {
        client <- ZIO.service[DockerClient]
        container <- make(client)
      } yield container
    }

  def service[R, A](r: ZImage => RIO[R, A]): RIO[R with ZImage, A] =
    ZIO.serviceWithZIO[ZImage](r)
