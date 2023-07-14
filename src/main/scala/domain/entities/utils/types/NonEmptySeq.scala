package domain.entities.utils.types

object NonEmptySeq {
  opaque type NonEmptySeq[A] <: Seq[A] = Seq[A]

  def apply[A](head: A, tail: A*): NonEmptySeq[A] = head +: tail.toSeq

  def fromIterable[A](iterable: Iterable[A]): Option[NonEmptySeq[A]] = {
    fromSeq(iterable.toSeq)
  }

  def fromSeq[A](seq: Seq[A]): Option[NonEmptySeq[A]] = seq.headOption match {
    case Some(head) => Some(apply(head, seq.tail: _*))
    case None => None
  }

  def unsafe[A](iterable: Iterable[A]): NonEmptySeq[A] = fromIterable(iterable).get

  extension [A](nonEmptySeq: NonEmptySeq[A]) {
    def mapExt[B](f: A => B): NonEmptySeq[B] = nonEmptySeq.map(f)
    def flatMapExt[B](f: A => IterableOnce[B]): NonEmptySeq[B] = nonEmptySeq.flatMap(f)
  }
}
