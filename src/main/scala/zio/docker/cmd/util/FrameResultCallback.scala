package zio.docker.cmd.util

import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.model.Frame

trait FrameResultCallback extends ResultCallback[Frame]
