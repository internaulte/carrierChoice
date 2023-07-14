package domain.entities

import core.UnitTestSpec
import domain.entities.utils.Point
import domain.entities.utils.types.{DistanceInMeters, Latitude, LongNatural, Longitude, Natural}

final class AreaTest extends UnitTestSpec {
  describe("isAreaContainsOther") {
    it("should return true if other area is in area") {
      val point1 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))
      val point2 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5.009))

      val area = Area(point1, radius = DistanceInMeters(Natural.unsafe(2000)))
      val area2 = Area(point2, radius = DistanceInMeters(Natural.unsafe(1000)))

      assert(area.isAreaContainsOther(area2))
    }
    it("should return true if radius not zero and points are same") {
      val point1 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))
      val point2 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))

      val area = Area(point1, radius = DistanceInMeters(Natural.unsafe(2000)))
      val area2 = Area(point2, radius = DistanceInMeters(Natural.unsafe(1000)))

      assert(area.isAreaContainsOther(area2))
    }
    it("should return true if other area is not in area but distance is less than 1 meter") {
      val point1 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))
      val point2 = Point(latitude = Latitude.unsafe(5.000001), longitude = Longitude.unsafe(5))

      val area = Area(point1, radius = DistanceInMeters(Natural.zero))
      val area2 = Area(point2, radius = DistanceInMeters(Natural.zero))

      assert(area.isAreaContainsOther(area2))
    }
    it("should return false if other area has zero radius and distance is more than 1 meter") {
      val point1 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))
      val point2 = Point(latitude = Latitude.unsafe(5.00001), longitude = Longitude.unsafe(5))

      val area = Area(point1, radius = DistanceInMeters(Natural.zero))
      val area2 = Area(point2, radius = DistanceInMeters(Natural.zero))

      assert(!area.isAreaContainsOther(area2))
    }
    it("should return false if other area is not in area") {
      val point1 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))
      val point2 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))

      val area = Area(point1, radius = DistanceInMeters(Natural.unsafe(2000)))
      val area2 = Area(point2, radius = DistanceInMeters(Natural.unsafe(3000)))

      assert(!area.isAreaContainsOther(area2))
    }
  }

  describe("isPointInArea") {
    it("should return true if point is at center of area") {
      val point1 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))
      val point2 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))

      val area = Area(point1, radius = DistanceInMeters(Natural.zero))

      assert(area.isPointInArea(point2))
    }
    it("should return false if point is not at center of 0 radius area") {
      val point1 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))
      val point2 = Point(latitude = Latitude.unsafe(5.00001), longitude = Longitude.unsafe(5))

      val area = Area(point1, radius = DistanceInMeters(Natural.zero))

      assert(!area.isPointInArea(point2))
    }
    it("should return true if point is not at center of 0 radius area, but distance is < meter") {
      val point1 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))
      val point2 = Point(latitude = Latitude.unsafe(5.000001), longitude = Longitude.unsafe(5))

      val area = Area(point1, radius = DistanceInMeters(Natural.zero))

      assert(area.isPointInArea(point2))
    }
    it("should return true if point contained in area") {
      val point1 = Point(Latitude.unsafe(43.2969901), Longitude.unsafe(5.3789783))
      val point2 = Point(Latitude.unsafe(43.3969901), Longitude.unsafe(5.4789783))

      val area = Area(point1, radius = DistanceInMeters(Natural.unsafe(15000)))

      assert(area.isPointInArea(point2))
    }
    it("should return false if point not contained in area") {
      val point1 = Point(Latitude.unsafe(43.2969901), Longitude.unsafe(5.3789783))
      val point2 = Point(Latitude.unsafe(5), Longitude.unsafe(-10))

      val area = Area(point1, radius = DistanceInMeters(Natural.unsafe(10000)))

      assert(!area.isPointInArea(point2))
    }
  }
}
