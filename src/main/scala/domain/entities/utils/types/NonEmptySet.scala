package domain.entities.utils.types

object NonEmptySet {
  opaque type NonEmptySet[A] <: Set[A] = Set[A]

  def apply[A](head: A, tail: A*): NonEmptySet[A] = tail.toSet + head

  def fromIterable[A](iterable: Iterable[A]): Option[NonEmptySet[A]] = {
    fromSet(iterable.toSet)
  }

  def fromSet[A](set: Set[A]): Option[NonEmptySet[A]] = set.headOption match {
    case Some(head) => Some(apply(head, (set - head).toSeq: _*))
    case None => None
  }

  def unsafe[A](iterable: Iterable[A]): NonEmptySet[A] = fromIterable(iterable).get

  extension [A](nonEmptySet: NonEmptySet[A]) {
    def mapExt[B](f: A => B): NonEmptySet[B] = nonEmptySet.map(f)
    def flatMapExt[B](f: A => IterableOnce[B]): NonEmptySet[B] = nonEmptySet.flatMap(f)
  }
}
