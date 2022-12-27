package domain.entities.utils

import core.UnitTestSpec
import domain.entities.Area
import domain.entities.utils.types.{DistanceInMeters, Latitude, Longitude}
import zio.prelude.newtypes.Natural

final class PointTest extends UnitTestSpec {
  describe("isAreaContainsOther") {
    it("should return 0 if points are equals") {
      val point1 = Point(latitude = Latitude(5), longitude = Longitude(5))
      val point2 = Point(latitude = Latitude(5), longitude = Longitude(5))

      assert(point1.distanceInMetersTo(point2) == DistanceInMeters(Natural.zero))
    }
    it("should return distance between points, no negative value") {
      val point1 = Point(Latitude(43.2969901), Longitude(5.3789783))
      val point2 = Point(Latitude(43.3969901), Longitude(5.4789783))

      assert(point1.distanceInMetersTo(point2) == DistanceInMeters.fromInt(13748.854678798967.toInt).get)
      assert(point2.distanceInMetersTo(point1) == DistanceInMeters.fromInt(13748.854678798967.toInt).get)
    }
  }
}
