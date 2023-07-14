package domain.usecases

import adapters.repositories.interfaces.CarrierRepository
import core.UnitTestSpec
import domain.entities.{Carrier, CarrierCompatibility, CarrierWithCompatibility, DeliveryCategory}
import domain.entities.mocks.{CarrierMock, CarrierWithCompatibilityMock, DeliveryCategoryMock, DeliveryMock}
import domain.entities.utils.types.CostInMillis.CostInMillis
import domain.entities.utils.types.{CostInMillis, LongNatural, Natural}
import org.mockito.Mockito.{doReturn, reset, spy, times, verify}
import org.scalatest.funspec.AsyncFunSpec
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.Future

final class CarrierUseCasesImplTest extends UnitTestSpec {
  private val carrierRepositoryMock = mock[CarrierRepository]
  private val carrierUseCases = new CarrierUseCasesImpl(carrierRepositoryMock)

  override def beforeEach(): Unit = {
    reset(carrierRepositoryMock)
  }

  describe("getBestCarrierForDelivery") {
    it("should return none if no carrier") {
      val delivery = new DeliveryMock().build()
      doReturn(Future.successful(Set.empty[Carrier])).when(carrierRepositoryMock).getAllCarriers

      carrierUseCases.getBestCarrierForDelivery(delivery).map { result =>
        verify(carrierRepositoryMock).getAllCarriers
        assert(result == None)
      }
    }
    it("should return best carrier from selectBestCarrierWithCompatibility") {
      val carrierUseCasesSpy = spy(carrierUseCases)
      val delivery = new DeliveryMock().build()
      val carrierOne = new CarrierMock().build()
      val carrierTwo = new CarrierMock().build()
      val carrierOneWithCompatibility =
        CarrierWithCompatibility(carrierOne, carrierOne.getCompatibilityForDelivery(delivery))
      val carrierTwoWithCompatibility =
        CarrierWithCompatibility(carrierTwo, carrierTwo.getCompatibilityForDelivery(delivery))

      doReturn(Future.successful(Set(carrierOne, carrierTwo))).when(carrierRepositoryMock).getAllCarriers
      doReturn(carrierTwoWithCompatibility)
        .when(carrierUseCasesSpy)
        .selectBestCarrierWithCompatibility(carrierOneWithCompatibility, carrierTwoWithCompatibility)
      doReturn(carrierTwoWithCompatibility)
        .when(carrierUseCasesSpy)
        .selectBestCarrierWithCompatibility(carrierTwoWithCompatibility, carrierOneWithCompatibility)

      carrierUseCasesSpy.getBestCarrierForDelivery(delivery).map { result =>
        verify(carrierRepositoryMock).getAllCarriers
        assert(result == Some(carrierTwo))
      }
    }
  }

  describe("createCarrier") {
    it("should call repository and return created carrier") {
      val newCarrier = new CarrierMock().build()

      doReturn(Future.successful(())).when(carrierRepositoryMock).createCarrier(newCarrier)

      carrierUseCases
        .createCarrier(newCarrier.deliveryCategory, newCarrier.averageSpeed, newCarrier.costPerRide, newCarrier.id)
        .map { result =>
          verify(carrierRepositoryMock, times(1)).createCarrier(newCarrier)
          assert(result == newCarrier)
        }
    }
  }

  describe("getAllCarriersCompatibilities") {
    it("should call repository and return carriers with compatibility") {
      val deliveryCategory: DeliveryCategory = new DeliveryCategoryMock().build()

      val carrier1 = new CarrierMock().build()
      val carrier2 = new CarrierMock().build()

      doReturn(Future.successful(Set(carrier1, carrier2))).when(carrierRepositoryMock).getAllCarriers

      carrierUseCases.getAllCarriersCompatibilities(deliveryCategory).map { result =>
        verify(carrierRepositoryMock, times(1)).getAllCarriers
        assert(
          result.forall(carrierWithCompatibility => Set(carrier1, carrier2).contains(carrierWithCompatibility.carrier))
        )
      }
    }
  }

  describe("selectBestCarrierWithCompatibility") {
    it("should return this carrierWithCompatibility if has better score than other") {
      val carrierWithCompatibility =
        new CarrierWithCompatibilityMock().setCarrierCompatibility(CarrierCompatibility(Natural.unsafe(3))).build()
      val otherCarrierWithCompatibility =
        new CarrierWithCompatibilityMock().setCarrierCompatibility(CarrierCompatibility(Natural.unsafe(2))).build()

      val result =
        carrierUseCases.selectBestCarrierWithCompatibility(carrierWithCompatibility, otherCarrierWithCompatibility)

      assert(result == carrierWithCompatibility)
    }
    it("should return other carrierWithCompatibility if has better score than first") {
      val carrierWithCompatibility =
        new CarrierWithCompatibilityMock().setCarrierCompatibility(CarrierCompatibility(Natural.unsafe(3))).build()
      val otherCarrierWithCompatibility =
        new CarrierWithCompatibilityMock().setCarrierCompatibility(CarrierCompatibility(Natural.unsafe(4))).build()

      val result =
        carrierUseCases.selectBestCarrierWithCompatibility(carrierWithCompatibility, otherCarrierWithCompatibility)

      assert(result == otherCarrierWithCompatibility)
    }
    it("should return other carrierWithCompatibility if has same score than first but cheaper") {
      val longNatural10 = LongNatural.unsafe(10L)
      val notCheapCost: CostInMillis = CostInMillis(longNatural10)
      val carrierWithCompatibility =
        new CarrierWithCompatibilityMock()
          .setCarrierCompatibility(CarrierCompatibility(Natural.unsafe(3)))
          .setCarrier(new CarrierMock().setCostPerRide(notCheapCost).build())
          .build()
      val longNatural5 = LongNatural.unsafe(5L)
      val cheapCost: CostInMillis = CostInMillis(longNatural5)
      val otherCarrierWithCompatibility = new CarrierWithCompatibilityMock()
        .setCarrierCompatibility(CarrierCompatibility(Natural.unsafe(3)))
        .setCarrier(new CarrierMock().setCostPerRide(cheapCost).build())
        .build()

      val result =
        carrierUseCases.selectBestCarrierWithCompatibility(carrierWithCompatibility, otherCarrierWithCompatibility)

      assert(result == otherCarrierWithCompatibility)
    }
  }
}
