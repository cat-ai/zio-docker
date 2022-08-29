package zio.docker.cmd

import com.github.dockerjava.api.command._
import com.github.dockerjava.api.model.AuthConfig
import zio.docker.cmd.util.{BuildResponseResultCallback, PullResponseResultCallback, PushResponseResultCallback, WaitResponseResultCallback}

import java.io.InputStream

sealed trait DockerCmdSettings

sealed trait ContainerApiCmdSettings extends DockerCmdSettings

case class ListContainersCmdSettings(settings: ListContainersCmd => ListContainersCmd)                               extends ContainerApiCmdSettings
case class CreateContainerCmdSettings(image: String,
                                      settings: CreateContainerCmd => CreateContainerCmd)                            extends ContainerApiCmdSettings
case class StartContainerCmdSettings(container: String,
                                     settings: StartContainerCmd => StartContainerCmd)                               extends ContainerApiCmdSettings
case class ExecCreateCmdSettings(container: String,
                                 settings: ExecCreateCmd => ExecCreateCmd)                                           extends ContainerApiCmdSettings
case class InspectContainerCmdSettings(container: String,
                                       settings: InspectContainerCmd => InspectContainerCmd)                         extends ContainerApiCmdSettings
case class RemoveContainerCmdSettings(container: String,
                                      settings: RemoveContainerCmd => RemoveContainerCmd)                            extends ContainerApiCmdSettings
case class WaitContainerCmdSettings(container: String,
                                    callback: WaitResponseResultCallback,
                                    settings: WaitContainerCmd => WaitContainerCmd)                                  extends ContainerApiCmdSettings
case class AttachContainerCmdSettings(container: String,
                                      callback: WaitResponseResultCallback,
                                      settings: AttachContainerCmd => AttachContainerCmd)                            extends ContainerApiCmdSettings
case class ExecStartCmdSettings(settings: ExecStartCmd => ExecStartCmd)                                              extends ContainerApiCmdSettings
case class InspectExecCmdSettings(container: String,
                                  settings: InspectExecCmd => InspectExecCmd)                                        extends ContainerApiCmdSettings
case class LogContainerCmdSettings(container: String,
                                   callback: WaitResponseResultCallback,
                                   settings: LogContainerCmd => LogContainerCmd)                                     extends ContainerApiCmdSettings
case class CopyArchiveFromContainerCmdSettings(container: String,
                                               resource: String,
                                               settings: CopyArchiveFromContainerCmd => CopyArchiveFromContainerCmd) extends ContainerApiCmdSettings
case class CopyArchiveToContainerCmdSettings(container: String,
                                             settings: CopyArchiveToContainerCmd => CopyArchiveToContainerCmd)       extends ContainerApiCmdSettings
case class ContainerDiffCmdSettings(container: String,
                                    settings: ContainerDiffCmd => ContainerDiffCmd)                                  extends ContainerApiCmdSettings
case class StopContainerCmdSettings(container: String,
                                    settings: StopContainerCmd => StopContainerCmd)                                  extends ContainerApiCmdSettings
case class KillContainerCmdSettings(container: String,
                                    settings: KillContainerCmd => KillContainerCmd)                                  extends ContainerApiCmdSettings
case class UpdateContainerCmdSettings(container: String,
                                      settings: UpdateContainerCmd => UpdateContainerCmd)                            extends ContainerApiCmdSettings
case class RenameContainerCmdSettings(container: String,
                                      settings: RenameContainerCmd => RenameContainerCmd)                            extends ContainerApiCmdSettings
case class RestartContainerCmdSettings(container: String,
                                       settings: RestartContainerCmd => RestartContainerCmd)                         extends ContainerApiCmdSettings
case class PauseContainerCmdSettings(container: String,
                                     settings: PauseContainerCmd => PauseContainerCmd)                               extends ContainerApiCmdSettings

case class UnpauseContainerCmdSettings(container: String, settings: UnpauseContainerCmd => UnpauseContainerCmd)      extends ContainerApiCmdSettings

sealed trait ImageApiCmd extends DockerCmdSettings

case class InspectImageCmdSettings(image: String,
                                   settings: InspectImageCmd => InspectImageCmd)                                     extends ImageApiCmd
case class LoadImageCmdSettings(is: InputStream,
                                settings: LoadImageCmd => LoadImageCmd)                                              extends ImageApiCmd
case class PushImageCmdSettings(repo: String,
                                callback: PushResponseResultCallback,
                                settings: PushImageCmd => PushImageCmd)                                              extends ImageApiCmd
case class PullImageCmdSettings(repo: String,
                                callback: PullResponseResultCallback,
                                settings: PullImageCmd => PullImageCmd)                                              extends ImageApiCmd
case class BuildImageCmdSettings(is: InputStream,
                                 callback: BuildResponseResultCallback,
                                 settings: BuildImageCmd => BuildImageCmd)                                           extends ImageApiCmd
case class CreateImageCmdSettings(repo: String,
                                  imgIs: InputStream,
                                  settings: CreateImageCmd => CreateImageCmd)                                        extends ImageApiCmd
case class ListImagesCmdSettings(settings: ListImagesCmd => ListImagesCmd)                                           extends ImageApiCmd
case class RemoveImageCmdSettings(image: String,
                                  settings: RemoveImageCmd => RemoveImageCmd)                                        extends ImageApiCmd
case class SaveImageCmdSettings(image: String,
                                settings: SaveImageCmd => SaveImageCmd)                                              extends ImageApiCmd
case class SearchImagesCmdSettings(image: String,
                                   settings: SearchImagesCmd => SearchImagesCmd)                                     extends ImageApiCmd
case class TagImageCmdSettings(image: String,
                               repo: String,
                               tag: String,
                               settings: TagImageCmd => TagImageCmd)                                                 extends ImageApiCmd

case class AuthCmdSettings(authConfig: AuthConfig)                                                                   extends DockerCmdSettings

sealed trait DockerNetworkApiCmd extends ImageApiCmd

case class ConnectToNetworkCmdSettings(settings: ConnectToNetworkCmd => ConnectToNetworkCmd)                         extends DockerNetworkApiCmd
case class CreateNetworkCmdSettings(settings: CreateNetworkCmd => CreateNetworkCmd)                                  extends DockerNetworkApiCmd
case class RemoveNetworkCmdSettings(network: String, settings: RemoveNetworkCmd => RemoveNetworkCmd)                 extends DockerNetworkApiCmd
case class ListNetworksCmdSettings(settings: ListNetworksCmd => ListNetworksCmd)                                     extends DockerNetworkApiCmd
case class InspectNetworkCmdSettings(settings: InspectNetworkCmd => InspectNetworkCmd)                               extends DockerNetworkApiCmd
case class DisconnectFromNetworkCmdSettings(settings: DisconnectFromNetworkCmd => DisconnectFromNetworkCmd)          extends DockerNetworkApiCmd
