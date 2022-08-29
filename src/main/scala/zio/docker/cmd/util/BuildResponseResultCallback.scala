package zio.docker.cmd.util

import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.model.BuildResponseItem

trait BuildResponseResultCallback extends ResultCallback[BuildResponseItem]
