package zio.docker

import com.github.dockerjava.api.DockerClient
import zio.{Promise, RIO, Task, UIO, ZIO}

trait ZDockerCmdAccessor:

  def client: DockerClient

  def liftUnsafe[T](f: => T): Task[T] = ZIO.attempt[T](f)

  def attemptBlockingUnsafe[R, A](effect: => A): RIO[R, Task[A]] =
    for {
      done <- Promise.make[Throwable, A]
      _ <- ZIO.attemptBlocking(effect)
    } yield done.await

  def close: UIO[Unit]

