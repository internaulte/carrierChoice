package domain.entities.utils.types

import core.UnitTestSpec

final class LongNaturalTest extends UnitTestSpec {
  describe("times") {
    it("should return zero if times ZERO") {
      val one = LongNatural.one
      val zero = LongNatural.zero

      assert(zero.times(one) == zero)
    }
    it("should return four if 2 times 2") {
      val two = LongNatural.unsafe(2L)
      val four = LongNatural.unsafe(4L)

      assert(two.times(two) == four)
    }
  }
  describe("plus") {
    it("should return same if plus ZERO") {
      val one = LongNatural.one
      val zero = LongNatural.zero

      assert(zero.plus(one) == one)
    }
    it("should return four if 2 plus 2") {
      val two = LongNatural.unsafe(2L)
      val four = LongNatural.unsafe(4L)

      assert(two.plus(two) == four)
    }
  }
  describe("max") {
    it("should return max of both") {
      val one = LongNatural.one
      val zero = LongNatural.zero

      assert(zero.maximum(one) == one)
    }
    it("should return max of 2 and four") {
      val two = LongNatural.unsafe(2L)
      val four = LongNatural.unsafe(4L)

      assert(four.maximum(two) == four)
    }
  }
}
