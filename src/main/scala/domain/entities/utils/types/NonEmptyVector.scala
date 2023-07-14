package domain.entities.utils.types

object NonEmptyVector {
  opaque type NonEmptyVector[A] <: Vector[A] = Vector[A]

  def apply[A](head: A, tail: A*): NonEmptyVector[A] = head +: tail.toVector

  def fromIterable[A](iterable: Iterable[A]): Option[NonEmptyVector[A]] = {
    fromList(iterable.toVector)
  }

  def fromList[A](vector: Vector[A]): Option[NonEmptyVector[A]] = vector.headOption match {
    case Some(head) => Some(apply(head, vector.tail: _*))
    case None => None
  }

  def unsafe[A](iterable: Iterable[A]): NonEmptyVector[A] = fromIterable(iterable).get

  extension [A](nonEmptyVector: NonEmptyVector[A]) {
    def mapExt[B](f: A => B): NonEmptyVector[B] = nonEmptyVector.map(f)
    def flatMapExt[B](f: A => IterableOnce[B]): NonEmptyVector[B] = nonEmptyVector.flatMap(f)
  }
}
