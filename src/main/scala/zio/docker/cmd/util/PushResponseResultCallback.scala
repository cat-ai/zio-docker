package zio.docker.cmd.util

import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.model.PushResponseItem

trait PushResponseResultCallback extends ResultCallback[PushResponseItem]
