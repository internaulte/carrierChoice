package core

trait Mock[T] {
  def build(): T
}
