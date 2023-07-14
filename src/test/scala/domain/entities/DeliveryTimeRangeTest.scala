package domain.entities

import core.UnitTestSpec

import java.time.{Duration, LocalDateTime, Month}

final class DeliveryTimeRangeTest extends UnitTestSpec {
  describe("duration") {
    it("should return zero if start and end are the same") {
      val time = LocalDateTime.now()
      val startInterval: LocalDateTime = time
      val endInterval: LocalDateTime = time

      val deliveryTimeRange = DeliveryTimeRange(startInterval, endInterval)

      assert(deliveryTimeRange.duration == Duration.ZERO)
    }
    it("should return correct interval duration") {
      val durationDifference = 4
      val time = LocalDateTime.now()
      val startInterval: LocalDateTime = time
      val endInterval: LocalDateTime = time.minusHours(durationDifference)

      val deliveryTimeRange = DeliveryTimeRange(startInterval, endInterval)

      assert(deliveryTimeRange.duration == Duration.ofHours(durationDifference))
    }
    it("should return correct interval duration as positive value when end and start are inverted") {
      val durationDifference = 4
      val time = LocalDateTime.now()
      val startInterval: LocalDateTime = time
      val endInterval: LocalDateTime = time.minusHours(durationDifference)

      val deliveryTimeRange = DeliveryTimeRange(endInterval, startInterval)

      assert(deliveryTimeRange.duration == Duration.ofHours(durationDifference))
    }
    it("should return correct interval duration as positive value when end and start are on different days") {
      val durationDifference = 1
      val time = LocalDateTime.of(0, Month.DECEMBER, 20, 23, 50)
      val startInterval: LocalDateTime = time
      val endInterval: LocalDateTime = time.plusHours(durationDifference)

      val deliveryTimeRange = DeliveryTimeRange(endInterval, startInterval)

      assert(deliveryTimeRange.duration == Duration.ofHours(durationDifference))
    }
  }
  describe("containsTotally") {
    it("should return true if both time ranges are same") {
      val time = LocalDateTime.now()
      val startInterval: LocalDateTime = time
      val endInterval: LocalDateTime = time

      val deliveryTimeRange1 = DeliveryTimeRange(startInterval, endInterval)
      val deliveryTimeRange2 = DeliveryTimeRange(startInterval, endInterval)

      assert(deliveryTimeRange1.containsTotally(deliveryTimeRange2) == true)
      assert(deliveryTimeRange2.containsTotally(deliveryTimeRange1) == true)
    }
    it("should return true if both time start are same and second End is strictly before first end") {
      val time = LocalDateTime.now()
      val startInterval: LocalDateTime = time
      val endInterval: LocalDateTime = time

      val deliveryTimeRange1 = DeliveryTimeRange(startInterval, endInterval)
      val deliveryTimeRange2 = DeliveryTimeRange(startInterval, endInterval.minusSeconds(5))

      assert(deliveryTimeRange1.containsTotally(deliveryTimeRange2) == true)
    }
    it("should return false if both time start are same and second End is strictly after first end") {
      val time = LocalDateTime.now()
      val startInterval: LocalDateTime = time.minusSeconds(5)
      val endInterval: LocalDateTime = time.plusSeconds(5)

      val deliveryTimeRange1 = DeliveryTimeRange(startInterval, endInterval)
      val deliveryTimeRange2 = DeliveryTimeRange(startInterval, endInterval.plusSeconds(1))

      assert(deliveryTimeRange1.containsTotally(deliveryTimeRange2) == false)
    }
    it("should return true if both time ends are same and second start is strictly after first start") {
      val time = LocalDateTime.now()
      val startInterval: LocalDateTime = time.minusSeconds(5)
      val endInterval: LocalDateTime = time.plusSeconds(5)

      val deliveryTimeRange1 = DeliveryTimeRange(startInterval, endInterval)
      val deliveryTimeRange2 = DeliveryTimeRange(startInterval.plusSeconds(1), endInterval)

      assert(deliveryTimeRange1.containsTotally(deliveryTimeRange2) == true)
    }
    it("should return false if both time ends are same and second start is strictly before first start") {
      val time = LocalDateTime.now()
      val startInterval: LocalDateTime = time.minusSeconds(5)
      val endInterval: LocalDateTime = time.plusSeconds(5)

      val deliveryTimeRange1 = DeliveryTimeRange(startInterval, endInterval)
      val deliveryTimeRange2 = DeliveryTimeRange(startInterval.minusSeconds(1), endInterval)

      assert(deliveryTimeRange2.containsTotally(deliveryTimeRange1) == true)
    }
    it("should return false if second start and end are strictly before start and end") {
      val time = LocalDateTime.now()
      val startInterval: LocalDateTime = time.minusSeconds(5)
      val endInterval: LocalDateTime = time.plusSeconds(5)

      val deliveryTimeRange1 = DeliveryTimeRange(startInterval, endInterval)
      val deliveryTimeRange2 = DeliveryTimeRange(startInterval.minusSeconds(1), endInterval.minusSeconds(1))

      assert(deliveryTimeRange1.containsTotally(deliveryTimeRange2) == false)
    }
    it("should return false if second start and end are strictly after start and end") {
      val time = LocalDateTime.now()
      val startInterval: LocalDateTime = time.minusSeconds(5)
      val endInterval: LocalDateTime = time.plusSeconds(5)

      val deliveryTimeRange1 = DeliveryTimeRange(startInterval, endInterval)
      val deliveryTimeRange2 = DeliveryTimeRange(startInterval.plusSeconds(1), endInterval.plusSeconds(1))

      assert(deliveryTimeRange1.containsTotally(deliveryTimeRange2) == false)
    }
    it("should return false if second start is strictly before first and second end is strictly after first end") {
      val time = LocalDateTime.now()
      val startInterval: LocalDateTime = time.minusSeconds(5)
      val endInterval: LocalDateTime = time.plusSeconds(5)

      val deliveryTimeRange1 = DeliveryTimeRange(startInterval, endInterval)
      val deliveryTimeRange2 = DeliveryTimeRange(startInterval.minusSeconds(1), endInterval.plusSeconds(1))

      assert(deliveryTimeRange1.containsTotally(deliveryTimeRange2) == false)
    }
    it("should return true if second start is strictly after first and second end is strictly before first end") {
      val time = LocalDateTime.now()
      val startInterval: LocalDateTime = time.minusSeconds(5)
      val endInterval: LocalDateTime = time.plusSeconds(5)

      val deliveryTimeRange1 = DeliveryTimeRange(startInterval, endInterval)
      val deliveryTimeRange2 = DeliveryTimeRange(startInterval.plusSeconds(1), endInterval.minusSeconds(1))

      assert(deliveryTimeRange1.containsTotally(deliveryTimeRange2) == true)
    }
    it("should return true if second start is strictly after first and second end is strictly before first end, even if on another day") {
      val time = LocalDateTime.of(0, Month.DECEMBER, 20, 23, 50)
      val startInterval: LocalDateTime = time
      val endInterval: LocalDateTime = time.plusMinutes(11)

      val deliveryTimeRange1 = DeliveryTimeRange(startInterval, endInterval)
      val deliveryTimeRange2 = DeliveryTimeRange(startInterval.plusSeconds(1), endInterval.minusSeconds(1))

      assert(deliveryTimeRange1.containsTotally(deliveryTimeRange2) == true)
    }
  }
}
