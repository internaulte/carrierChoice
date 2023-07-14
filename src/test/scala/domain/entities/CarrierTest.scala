package domain.entities

import core.UnitTestSpec
import domain.entities.mocks.{
  AreaMock,
  CarrierMock,
  DeliveryCategoryMock,
  DeliveryMock,
  DeliveryTimeRangeMock,
  PointMock
}
import domain.entities.utils.Point
import domain.entities.utils.types.*

import java.time.LocalDateTime

final class CarrierTest extends UnitTestSpec {
  describe("isCheaperThanOther") {
    it("should return true if other carrier has cost per ride > this") {
      val longNaturalFive = LongNatural.unsafe(5)
      val longNaturalMoreThanFive = LongNatural.unsafe(5000)
      val costPerRideCheap = CostInMillis(longNaturalFive)
      val costPerRideNotCheap = CostInMillis(longNaturalMoreThanFive)

      val carrierCheap = new CarrierMock().setCostPerRide(costPerRideCheap).build()
      val carrierNotCheap = new CarrierMock().setCostPerRide(costPerRideNotCheap).build()

      assert(carrierCheap.isCheaperThanOther(carrierNotCheap) == true)
    }
    it("should return true if other carrier has cost per ride == this") {
      val longNaturalFive = LongNatural.unsafe(5)
      val costPerRideCheap = CostInMillis(longNaturalFive)
      val costPerRideNotCheap = CostInMillis(longNaturalFive)

      val carrierCheap = new CarrierMock().setCostPerRide(costPerRideCheap).build()
      val carrierNotCheap = new CarrierMock().setCostPerRide(costPerRideNotCheap).build()

      assert(carrierCheap.isCheaperThanOther(carrierNotCheap) == true)
    }
    it("should return false if other carrier has cost per ride < this") {
      val longNaturalFive = LongNatural.unsafe(5)
      val longNaturalMoreThanFive = LongNatural.unsafe(5000)
      val costPerRideCheap = CostInMillis(longNaturalFive)
      val costPerRideNotCheap = CostInMillis(longNaturalMoreThanFive)

      val carrierCheap = new CarrierMock().setCostPerRide(costPerRideCheap).build()
      val carrierNotCheap = new CarrierMock().setCostPerRide(costPerRideNotCheap).build()

      assert(carrierNotCheap.isCheaperThanOther(carrierCheap) == false)
    }
  }
  describe("isDeliveryPossibleInTime") {
    it("should return true if delivery duration < wanted delivery duration") {
      val natural10 = NonZeroNaturalInt.unsafe(10)
      val speed10meterPerSecond = SpeedInMetersPerSecond(natural10)

      val startInterval = LocalDateTime.now()
      val durationInHours = 1
      val endInterval = startInterval.plusHours(durationInHours)
      val deliveryTimeRange =
        new DeliveryTimeRangeMock().setStartInterval(startInterval).setEndInterval(endInterval).build()
      val deliveryCategory = new DeliveryCategoryMock().setDeliveryTimeRange(deliveryTimeRange).build()
      val carrier =
        new CarrierMock().setAverageSpeed(speed10meterPerSecond).setDeliveryCategory(deliveryCategory).build()

      val point1 = new PointMock().setLatitude(Latitude.unsafe(43.2969901)).build()
      val point2 = new PointMock().setLatitude(Latitude.unsafe(43.2969991)).build()

      val delivery =
        new DeliveryMock().setWithdrawal(point1).setDestination(point2).build() // delivery distance is 1 meter

      assert(carrier.isDeliveryPossibleInTime(delivery) == true)
    }
    it("should return true if delivery duration == wanted delivery duration") {
      val natural1 = NonZeroNaturalInt.one
      val speed1meterPerSecond = SpeedInMetersPerSecond(natural1)

      val startInterval = LocalDateTime.now()
      val durationInSeconds = 1
      val endInterval = startInterval.plusSeconds(durationInSeconds)
      val deliveryTimeRange =
        new DeliveryTimeRangeMock().setStartInterval(startInterval).setEndInterval(endInterval).build()
      val deliveryCategory = new DeliveryCategoryMock().setDeliveryTimeRange(deliveryTimeRange).build()
      val carrier =
        new CarrierMock().setAverageSpeed(speed1meterPerSecond).setDeliveryCategory(deliveryCategory).build()

      val point1 = new PointMock().setLatitude(Latitude.unsafe(43.2969901)).build()
      val point2 = new PointMock().setLatitude(Latitude.unsafe(43.2969991)).build()

      val delivery =
        new DeliveryMock().setWithdrawal(point1).setDestination(point2).build() // delivery distance is 1 meter

      assert(carrier.isDeliveryPossibleInTime(delivery) == true)
    }
    it("should return false if delivery duration > wanted delivery duration") {
      val natural1 = NonZeroNaturalInt.one
      val speed1meterPerSecond = SpeedInMetersPerSecond(natural1)

      val startInterval = LocalDateTime.now()
      val durationInSeconds = 1
      val endInterval = startInterval.plusSeconds(durationInSeconds)
      val deliveryTimeRange =
        new DeliveryTimeRangeMock().setStartInterval(startInterval).setEndInterval(endInterval).build()
      val deliveryCategory = new DeliveryCategoryMock().setDeliveryTimeRange(deliveryTimeRange).build()
      val carrier =
        new CarrierMock().setAverageSpeed(speed1meterPerSecond).setDeliveryCategory(deliveryCategory).build()

      val point1 = new PointMock().setLatitude(Latitude.unsafe(43.2969901)).build()
      val point2 = new PointMock().setLatitude(Latitude.unsafe(43.2979991)).build()

      val delivery =
        new DeliveryMock().setWithdrawal(point1).setDestination(point2).build() // delivery distance is >= 2 meter

      assert(carrier.isDeliveryPossibleInTime(delivery) == false)
    }
  }
  describe("getDeliveryZonePossibleScore for DeliveryCategory") {
    it("should return one if carrier delivery zone contains delivery point") {
      val point1 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))
      val point2 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5.009)) // points are 996 meters of distance
      val carrierArea = new AreaMock().setCenter(point1).setRadius(DistanceInMeters.unsafe(2000)).build()
      val carrierDeliveryCategory = new DeliveryCategoryMock().setDeliveryArea(carrierArea).build()
      val carrier = new CarrierMock().setDeliveryCategory(carrierDeliveryCategory).build()

      val otherArea = new AreaMock().setCenter(point2).setRadius(DistanceInMeters.unsafe(1000)).build()
      val otherDeliveryCategory = new DeliveryCategoryMock().setDeliveryArea(otherArea).build()

      assert(carrier.getDeliveryZonePossibleScore(otherDeliveryCategory) == Natural.one)
    }
    it("should return zero if carrier delivery zone does not contains delivery point") {
      val point1 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))
      val point2 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5.009)) // points are 996 meters of distance
      val carrierArea = new AreaMock().setCenter(point1).setRadius(DistanceInMeters.unsafe(1000)).build()
      val carrierDeliveryCategory = new DeliveryCategoryMock().setDeliveryArea(carrierArea).build()
      val carrier = new CarrierMock().setDeliveryCategory(carrierDeliveryCategory).build()

      val otherArea = new AreaMock().setCenter(point2).setRadius(DistanceInMeters.unsafe(1000)).build()
      val otherDeliveryCategory = new DeliveryCategoryMock().setDeliveryArea(otherArea).build()

      assert(carrier.getDeliveryZonePossibleScore(otherDeliveryCategory) == Natural.zero)
    }
  }
  describe("getDeliveryPossibleScore") {
    it("should return one if carrier delivery zone contains delivery point") {
      val point1 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))
      val point2 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5.009)) // points are 996 meters of distance
      val carrierArea = new AreaMock().setCenter(point1).setRadius(DistanceInMeters.unsafe(1000)).build()
      val carrierDeliveryCategory = new DeliveryCategoryMock().setDeliveryArea(carrierArea).build()
      val carrier = new CarrierMock().setDeliveryCategory(carrierDeliveryCategory).build()

      val point3 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5.002))
      val otherDelivery = new DeliveryMock().setWithdrawal(point2).setDestination(point3).build()

      assert(carrier.getDeliveryPossibleScore(otherDelivery) == Natural.one)
    }
    it("should return zero if carrier delivery zone does not contains delivery point") {
      val point1 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))
      val point2 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5.009)) // points are 996 meters of distance
      val carrierArea = new AreaMock().setCenter(point1).setRadius(DistanceInMeters.unsafe(1000)).build()
      val carrierDeliveryCategory = new DeliveryCategoryMock().setDeliveryArea(carrierArea).build()
      val carrier = new CarrierMock().setDeliveryCategory(carrierDeliveryCategory).build()

      val point3 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5.010))
      val otherDelivery = new DeliveryMock().setWithdrawal(point2).setDestination(point3).build()

      assert(carrier.getDeliveryPossibleScore(otherDelivery) == Natural.zero)
    }
    it("should return zero if carrier delivery zone does not contains withdrawal point") {
      val point1 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))
      val point2 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5.010))
      val carrierArea = new AreaMock().setCenter(point1).setRadius(DistanceInMeters.unsafe(1000)).build()
      val carrierDeliveryCategory = new DeliveryCategoryMock().setDeliveryArea(carrierArea).build()
      val carrier = new CarrierMock().setDeliveryCategory(carrierDeliveryCategory).build()

      val point3 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5.009))
      val otherDelivery = new DeliveryMock().setWithdrawal(point2).setDestination(point3).build()

      assert(carrier.getDeliveryPossibleScore(otherDelivery) == Natural.zero)
    }
  }
  describe("getSchedulingPossibilityScore") {
    it("should return one if time range is compatible with carrier") {
      val time = LocalDateTime.now()
      val deliveryTimeRange1 =
        new DeliveryTimeRangeMock().setStartInterval(time.minusHours(1)).setEndInterval(time.plusHours(1)).build()
      val deliveryCategory: DeliveryCategory =
        new DeliveryCategoryMock().setDeliveryTimeRange(deliveryTimeRange1).build()
      val carrier = new CarrierMock().setDeliveryCategory(deliveryCategory).build()

      val deliveryTimeRange2: DeliveryTimeRange =
        new DeliveryTimeRangeMock().setStartInterval(time.minusMinutes(55)).setEndInterval(time.plusMinutes(1)).build()

      assert(carrier.getSchedulingPossibilityScore(deliveryTimeRange2) == Natural.one)
    }
    it("should return zero if start time range is not compatible with carrier") {
      val time = LocalDateTime.now()
      val deliveryTimeRange1 =
        new DeliveryTimeRangeMock().setStartInterval(time.minusHours(1)).setEndInterval(time.plusHours(1)).build()
      val deliveryCategory: DeliveryCategory =
        new DeliveryCategoryMock().setDeliveryTimeRange(deliveryTimeRange1).build()
      val carrier = new CarrierMock().setDeliveryCategory(deliveryCategory).build()

      val deliveryTimeRange2: DeliveryTimeRange =
        new DeliveryTimeRangeMock().setStartInterval(time.minusMinutes(65)).setEndInterval(time.plusMinutes(1)).build()

      assert(carrier.getSchedulingPossibilityScore(deliveryTimeRange2) == Natural.zero)
    }
    it("should return zero if end time range is not compatible with carrier") {
      val time = LocalDateTime.now()
      val deliveryTimeRange1 =
        new DeliveryTimeRangeMock().setStartInterval(time.minusHours(1)).setEndInterval(time.plusHours(1)).build()
      val deliveryCategory: DeliveryCategory =
        new DeliveryCategoryMock().setDeliveryTimeRange(deliveryTimeRange1).build()
      val carrier = new CarrierMock().setDeliveryCategory(deliveryCategory).build()

      val deliveryTimeRange2: DeliveryTimeRange =
        new DeliveryTimeRangeMock().setStartInterval(time.minusMinutes(20)).setEndInterval(time.plusMinutes(61)).build()

      assert(carrier.getSchedulingPossibilityScore(deliveryTimeRange2) == Natural.zero)
    }
    it("should return zero if start and end time range is not compatible with carrier") {
      val time = LocalDateTime.now()
      val deliveryTimeRange1 =
        new DeliveryTimeRangeMock().setStartInterval(time.minusHours(1)).setEndInterval(time.plusHours(1)).build()
      val deliveryCategory: DeliveryCategory =
        new DeliveryCategoryMock().setDeliveryTimeRange(deliveryTimeRange1).build()
      val carrier = new CarrierMock().setDeliveryCategory(deliveryCategory).build()

      val deliveryTimeRange2: DeliveryTimeRange =
        new DeliveryTimeRangeMock().setStartInterval(time.minusMinutes(65)).setEndInterval(time.plusMinutes(61)).build()

      assert(carrier.getSchedulingPossibilityScore(deliveryTimeRange2) == Natural.zero)
    }
  }
  describe("getTransportPossiblityScore") {
    it("should return one if transport possible for weight, volume and total weight") {
      val natural50 = LongNatural.unsafe(50L)
      val natural20 = LongNatural.unsafe(20L)
      val deliveryCategory: DeliveryCategory = new DeliveryCategoryMock()
        .setMaxPackageWeight(WeightInGram(natural50))
        .setTotalVolume(VolumeInMillim3(natural50))
        .setTotalWeight(WeightInGram(natural50))
        .build()
      val carrier = new CarrierMock().setDeliveryCategory(deliveryCategory).build()

      val weightInGram50 = WeightInGram(natural50)
      val volume50 = VolumeInMillim3(natural50)
      val weight20 = WeightInGram(natural20)
      assert(carrier.getTransportPossiblityScore(weight20, volume50, weightInGram50) == Natural.one)
    }
    it("should return zero if transport possible for weight, volume but not total weight") {
      val natural50 = LongNatural.unsafe(50L)
      val natural20 = LongNatural.unsafe(20L)
      val deliveryCategory: DeliveryCategory = new DeliveryCategoryMock()
        .setMaxPackageWeight(WeightInGram(natural50))
        .setTotalVolume(VolumeInMillim3(natural50))
        .setTotalWeight(WeightInGram(natural20))
        .build()
      val carrier = new CarrierMock().setDeliveryCategory(deliveryCategory).build()

      val weightInGram50 = WeightInGram(natural50)
      val volume50 = VolumeInMillim3(natural50)
      val weight20 = WeightInGram(natural20)
      assert(carrier.getTransportPossiblityScore(weight20, volume50, weightInGram50) == Natural.zero)
    }
    it("should return zero if transport possible for weight and total weight but not volume") {
      val natural50 = LongNatural.unsafe(50L)
      val natural20 = LongNatural.unsafe(20L)
      val deliveryCategory: DeliveryCategory = new DeliveryCategoryMock()
        .setMaxPackageWeight(WeightInGram(natural50))
        .setTotalVolume(VolumeInMillim3(natural20))
        .setTotalWeight(WeightInGram(natural50))
        .build()
      val carrier = new CarrierMock().setDeliveryCategory(deliveryCategory).build()

      val weightInGram50 = WeightInGram(natural50)
      val volume50 = VolumeInMillim3(natural50)
      val weight20 = WeightInGram(natural20)
      assert(carrier.getTransportPossiblityScore(weight20, volume50, weightInGram50) == Natural.zero)
    }
    it("should return zero if transport possible for volume and total weight but not weight") {
      val natural50 = LongNatural.unsafe(50L)
      val natural20 = LongNatural.unsafe(20L)
      val deliveryCategory: DeliveryCategory = new DeliveryCategoryMock()
        .setMaxPackageWeight(WeightInGram(natural20))
        .setTotalVolume(VolumeInMillim3(natural50))
        .setTotalWeight(WeightInGram(natural50))
        .build()
      val carrier = new CarrierMock().setDeliveryCategory(deliveryCategory).build()

      val weightInGram50 = WeightInGram(natural50)
      val volume50 = VolumeInMillim3(natural20)
      val weight20 = WeightInGram(natural50)
      assert(carrier.getTransportPossiblityScore(weight20, volume50, weightInGram50) == Natural.zero)
    }
  }
  describe("getCompatibilityForDelivery") {
    it("should return sum of scores with one as possibility to deliver in time") {
      val point1 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))
      val point2 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5.009)) // points are 996 meters of distance
      val carrierArea = new AreaMock().setCenter(point1).setRadius(DistanceInMeters.unsafe(1000)).build()

      val point3 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5.002))
      val time = LocalDateTime.now()
      val deliveryTimeRange1 =
        new DeliveryTimeRangeMock().setStartInterval(time.minusHours(1)).setEndInterval(time.plusHours(1)).build()
      val natural50 = LongNatural.unsafe(50L)
      val deliveryCategory: DeliveryCategory = new DeliveryCategoryMock()
        .setMaxPackageWeight(WeightInGram(natural50))
        .setTotalVolume(VolumeInMillim3(natural50))
        .setDeliveryTimeRange(deliveryTimeRange1)
        .setTotalWeight(WeightInGram(natural50))
        .setDeliveryArea(carrierArea)
        .build()
      val carrier = new CarrierMock().setDeliveryCategory(deliveryCategory).build()
      val deliveryTimeRange2 =
        new DeliveryTimeRangeMock().setStartInterval(time.minusMinutes(1)).setEndInterval(time.plusMinutes(1)).build()
      val otherDelivery = new DeliveryMock()
        .setWithdrawal(point2)
        .setDestination(point3)
        .setDeliveryTimeRange(deliveryTimeRange2)
        .build()

      val natural4 = Natural.unsafe(4)

      assert(carrier.getCompatibilityForDelivery(otherDelivery) == CarrierCompatibility(natural4))
    }
    it("should return sum of scores with zero as possibility to deliver in time") {
      val point1 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))
      val point2 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5.009)) // points are 996 meters of distance
      val carrierArea = new AreaMock().setCenter(point1).setRadius(DistanceInMeters.unsafe(1000)).build()

      val point3 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(6.002))
      val time = LocalDateTime.now()
      val deliveryTimeRange1 =
        new DeliveryTimeRangeMock().setStartInterval(time.minusHours(1)).setEndInterval(time.plusHours(1)).build()
      val natural50 = LongNatural.unsafe(50L)
      val deliveryCategory: DeliveryCategory = new DeliveryCategoryMock()
        .setMaxPackageWeight(WeightInGram(natural50))
        .setTotalVolume(VolumeInMillim3(natural50))
        .setDeliveryTimeRange(deliveryTimeRange1)
        .setTotalWeight(WeightInGram(natural50))
        .setDeliveryArea(carrierArea)
        .build()
      val speed = SpeedInMetersPerSecond(NonZeroNaturalInt.one)
      val carrier = new CarrierMock().setDeliveryCategory(deliveryCategory).setAverageSpeed(speed).build()

      val deliveryTimeRange2 = new DeliveryTimeRangeMock().build()
      val otherDelivery = new DeliveryMock()
        .setWithdrawal(point2)
        .setDestination(point3)
        .setDeliveryTimeRange(deliveryTimeRange2)
        .build()

      val natural3 = Natural.unsafe(3)

      assert(carrier.getCompatibilityForDelivery(otherDelivery) == CarrierCompatibility(natural3))
    }
  }
  describe("getCompatibilityForDeliveryCategory") {
    it("should return sum of scores") {
      val point1 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5))
      val point2 = Point(latitude = Latitude.unsafe(5), longitude = Longitude.unsafe(5.009)) // points are 996 meters of distance
      val carrierArea = new AreaMock().setCenter(point1).setRadius(DistanceInMeters.unsafe(10)).build()
      val otherArea = new AreaMock().setCenter(point2).setRadius(DistanceInMeters.unsafe(1)).build()

      val time = LocalDateTime.now()
      val deliveryTimeRange1 =
        new DeliveryTimeRangeMock().setStartInterval(time.minusHours(1)).setEndInterval(time.plusHours(1)).build()
      val natural50 = LongNatural.unsafe(50L)
      val natural20 = LongNatural.unsafe(20L)
      val deliveryCategory: DeliveryCategory = new DeliveryCategoryMock()
        .setMaxPackageWeight(WeightInGram(natural20))
        .setTotalVolume(VolumeInMillim3(natural20))
        .setDeliveryTimeRange(deliveryTimeRange1)
        .setTotalWeight(WeightInGram(natural20))
        .setDeliveryArea(carrierArea)
        .build()
      val speed = SpeedInMetersPerSecond(NonZeroNaturalInt.one)
      val carrier = new CarrierMock().setDeliveryCategory(deliveryCategory).setAverageSpeed(speed).build()

      val deliveryTimeRange2 =
        new DeliveryTimeRangeMock().setStartInterval(time.minusHours(2)).setEndInterval(time.plusHours(2)).build()
      val otherDelivery = new DeliveryCategoryMock()
        .setMaxPackageWeight(WeightInGram(natural50))
        .setTotalVolume(VolumeInMillim3(natural50))
        .setTotalWeight(WeightInGram(natural50))
        .setDeliveryTimeRange(deliveryTimeRange2)
        .setDeliveryArea(otherArea)
        .build()

      assert(carrier.getCompatibilityForDeliveryCategory(otherDelivery) == CarrierCompatibility(Natural.zero))
    }
  }
}
