package domain.entities.utils.types

object NonEmptyList {
  opaque type NonEmptyList[A] <: List[A] = List[A]

  def apply[A](head: A, tail: A*): NonEmptyList[A] = head +: tail.toList

  def fromIterable[A](iterable: Iterable[A]): Option[NonEmptyList[A]] = {
    fromList(iterable.toList)
  }

  def fromList[A](list: List[A]): Option[NonEmptyList[A]] = list.headOption match {
    case Some(head) => Some(apply(head, list.tail: _*))
    case None => None
  }

  def unsafe[A](iterable: Iterable[A]): NonEmptyList[A] = fromIterable(iterable).get

  extension[A] (nonEmptyList: NonEmptyList[A]) {
    def mapExt[B](f: A => B): NonEmptyList[B] = nonEmptyList.map(f)
    def flatMapExt[B](f: A => IterableOnce[B]): NonEmptyList[B] = nonEmptyList.flatMap(f)
  }
}
