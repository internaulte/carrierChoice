package domain.entities.utils

import core.UnitTestSpec
import domain.entities.Area

final class PointTest extends UnitTestSpec {
  describe("isAreaContainsOther") {
    it("should return 0 if points are equals") {
      val point1 = Point(latitude = 5, longitude = 5)
      val point2 = Point(latitude = 5, longitude = 5)

      assert(point1.distanceInKmTo(point2) == 0)
    }
    it("should return distance between points, no negative value") {
      val point1 = Point(43.2969901, 5.3789783)
      val point2 = Point(43.3969901, 5.4789783)

      assert(point1.distanceInKmTo(point2) == 13.748854678798967)
      assert(point2.distanceInKmTo(point1) == 13.748854678798967)
    }
  }
}
