package domain.entities

import core.UnitTestSpec
import domain.entities.utils.{Point, PositiveOrZeroReal}

final class AreaTest extends UnitTestSpec {
  describe("isAreaContainsOther") {
    it("should return true if other area is in area") {
      val point1 = Point(latitude = 5, longitude = 5)
      val point2 = Point(latitude = 5, longitude = 5)

      val area = Area(point1, radius = PositiveOrZeroReal(2))
      val area2 = Area(point2, radius = PositiveOrZeroReal(1))

      assert(area.isAreaContainsOther(area2))
    }
    it("should return false if other area is not in area") {
      val point1 = Point(latitude = 5, longitude = 5)
      val point2 = Point(latitude = 5, longitude = 5)

      val area = Area(point1, radius = PositiveOrZeroReal(2))
      val area2 = Area(point2, radius = PositiveOrZeroReal(3))

      assert(!area.isAreaContainsOther(area2))
    }
  }

  describe("isPointInArea") {
    it("should return true if point is at center of area") {
      val point1 = Point(latitude = 5, longitude = 5)
      val point2 = Point(latitude = 5, longitude = 5)

      val area = Area(point1, radius = PositiveOrZeroReal.zero)

      assert(area.isPointInArea(point2))
    }
    it("should return false if point is not at center of 0 radius area") {
      val point1 = Point(latitude = 5, longitude = 5)
      val point2 = Point(latitude = 5.00000001, longitude = 5)

      val area = Area(point1, radius = PositiveOrZeroReal.zero)

      assert(!area.isPointInArea(point2))
    }
    it("should return true if point contained in area") {
      val point1 = Point(43.2969901, 5.3789783)
      val point2 = Point(43.3969901, 5.4789783)

      val area = Area(point1, radius = PositiveOrZeroReal(15))

      assert(area.isPointInArea(point2))
    }
    it("should return false if point not contained in area") {
      val point1 = Point(43.2969901, 5.3789783)
      val point2 = Point(5, -10)

      val area = Area(point1, radius = PositiveOrZeroReal(10))

      assert(!area.isPointInArea(point2))
    }
  }
}
