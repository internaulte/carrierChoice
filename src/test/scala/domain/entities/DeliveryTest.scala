package domain.entities

import core.UnitTestSpec
import domain.entities.mocks.{DeliveryMock, PackageMock, PointMock}
import domain.entities.utils.types.{DistanceInMeters, Latitude, LongNatural, Natural, VolumeInMillim3, WeightInGram}
import domain.entities.utils.types.NonEmptyList.NonEmptyList
import domain.entities.utils.types.NonEmptyList

final class DeliveryTest extends UnitTestSpec {
  describe("travelDistance") {
    it("should return zero if points are the same") {
      val point1 = new PointMock().build()
      val point2 = point1

      val delivery = new DeliveryMock().setWithdrawal(point1).setDestination(point2).build()

      assert(delivery.travelDistance == DistanceInMeters(Natural.zero))
    }
    it("should return zero if points are < one meter of distance") {
      val point1 = new PointMock().setLatitude(Latitude.unsafe(43.2969901)).build()
      val point2 = new PointMock().setLatitude(Latitude.unsafe(43.2969990)).build()

      val delivery = new DeliveryMock().setWithdrawal(point1).setDestination(point2).build()

      assert(delivery.travelDistance == DistanceInMeters(Natural.zero))
    }
    it("should return one if points are == one meter of distance") {
      val point1 = new PointMock().setLatitude(Latitude.unsafe(43.296990104)).build()
      val point2 = new PointMock().setLatitude(Latitude.unsafe(43.2969991)).build()

      val delivery = new DeliveryMock().setWithdrawal(point1).setDestination(point2).build()

      assert(delivery.travelDistance == DistanceInMeters(Natural.one))
    }
    it("should return one if points are > one meter and < two meters of distance") {
      val point1 = new PointMock().setLatitude(Latitude.unsafe(43.2969901)).build()
      val point2 = new PointMock().setLatitude(Latitude.unsafe(43.2969991)).build()

      val delivery = new DeliveryMock().setWithdrawal(point1).setDestination(point2).build()

      assert(delivery.travelDistance == DistanceInMeters(Natural.one))
    }
  }
  describe("totalVolume") {
    it("should return valid value for total volume of packages") {
      val package1 = new PackageMock().setVolume(VolumeInMillim3(LongNatural.one)).build()
      val package2 = new PackageMock().setVolume(VolumeInMillim3(LongNatural.one)).build()
      val package3 = new PackageMock().setVolume(VolumeInMillim3(LongNatural.one)).build()
      val package4 = new PackageMock().setVolume(VolumeInMillim3(LongNatural.one)).build()
      val package5 = new PackageMock().setVolume(VolumeInMillim3(LongNatural.one)).build()
      val delivery =
        new DeliveryMock().setPackages(NonEmptyList(package1, package2, package3, package4, package5)).build()

      assert(delivery.totalVolume == LongNatural.unsafe(5))
    }
  }
  describe("maxPackageWeight") {
    it("should return valid value for max package weight") {
      val maxValue = LongNatural.unsafe(7)
      val nonMaxValue1 = LongNatural.unsafe(maxValue - LongNatural.one)
      val nonMaxValue2 = LongNatural.unsafe(nonMaxValue1 - LongNatural.one)

      val package1 = new PackageMock().setWeight(WeightInGram(LongNatural.one)).build()
      val package2 = new PackageMock().setWeight(WeightInGram(LongNatural.one)).build()
      val package3 = new PackageMock().setWeight(WeightInGram(nonMaxValue1)).build()
      val package4 = new PackageMock().setWeight(WeightInGram(maxValue)).build()
      val package5 = new PackageMock().setWeight(WeightInGram(nonMaxValue2)).build()
      val delivery =
        new DeliveryMock().setPackages(NonEmptyList(package1, package2, package3, package4, package5)).build()

      assert(delivery.maxPackageWeight == maxValue)
    }
  }
  describe("totalWeight") {
    it("should return valid value for total package weight") {
      val maxValue = LongNatural.unsafe(7)
      val nonMaxValue1 = LongNatural.unsafe(maxValue - LongNatural.one)
      val nonMaxValue2 = LongNatural.unsafe(nonMaxValue1 - LongNatural.one)

      val package1 = new PackageMock().setWeight(WeightInGram(LongNatural.one)).build()
      val package2 = new PackageMock().setWeight(WeightInGram(LongNatural.one)).build()
      val package3 = new PackageMock().setWeight(WeightInGram(nonMaxValue1)).build()
      val package4 = new PackageMock().setWeight(WeightInGram(maxValue)).build()
      val package5 = new PackageMock().setWeight(WeightInGram(nonMaxValue2)).build()
      val delivery =
        new DeliveryMock().setPackages(NonEmptyList(package1, package2, package3, package4, package5)).build()

      val total = LongNatural.unsafe(20)
      assert(delivery.totalWeight == WeightInGram(total))
    }
  }
}
