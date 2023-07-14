package domain.entities

import core.UnitTestSpec
import domain.entities.mocks.{CarrierMock, CarrierWithCompatibilityMock}

import java.util.UUID
import domain.entities.utils.types.*

final class CarrierWithCompatibilityTest extends UnitTestSpec {
  describe("getCheaperCarrierOfTwo") {
    it("should return first carrier if cheaper") {
      val longNaturalFive = LongNatural.unsafe(5L)
      val cheaperCost = CostInMillis(longNaturalFive)
      val notCheapCost = CostInMillis(longNaturalFive.plus(LongNatural.one))
      val carrier1 = new CarrierMock().setCostPerRide(cheaperCost).build()
      val carrierWithCompatibility1 = new CarrierWithCompatibilityMock().setCarrier(carrier1).build()
      val carrier2 = new CarrierMock().setCostPerRide(notCheapCost).build()
      val carrierWithCompatibility2 = new CarrierWithCompatibilityMock().setCarrier(carrier2).build()

      assert(CarrierWithCompatibility.getCheaperCarrierOfTwo(carrierWithCompatibility1, carrierWithCompatibility2) == carrierWithCompatibility1)
    }
    it("should return second carrier if cheaper") {
      val longNaturalFive = LongNatural.unsafe(5L)
      val cheaperCost = CostInMillis(longNaturalFive)
      val notCheapCost = CostInMillis(longNaturalFive.plus(LongNatural.one))
      val carrier1 = new CarrierMock().setCostPerRide(cheaperCost).build()
      val carrierWithCompatibility1 = new CarrierWithCompatibilityMock().setCarrier(carrier1).build()
      val carrier2 = new CarrierMock().setCostPerRide(notCheapCost).build()
      val carrierWithCompatibility2 = new CarrierWithCompatibilityMock().setCarrier(carrier2).build()

      assert(CarrierWithCompatibility.getCheaperCarrierOfTwo(carrierWithCompatibility2, carrierWithCompatibility1) == carrierWithCompatibility1)
    }
  }
}
