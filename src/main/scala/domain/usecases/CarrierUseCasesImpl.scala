package domain.usecases

import adapters.repositories.interfaces.CarrierRepository
import domain.entities.utils.Types.{PositiveOrZeroReal, SpeedInKmH}
import domain.entities.*
import domain.usecases.interfaces.CarrierUseCases
import zio.prelude.NonEmptySet
import zio.prelude.newtypes.Natural

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

protected class CarrierUseCasesImpl(private val carrierRepository: CarrierRepository) extends CarrierUseCases {
  override def createCarrier(
      deliveryCategory: DeliveryCategory,
      averageSpeed: SpeedInKmH,
      costPerRide: PositiveOrZeroReal,
      carrierId: UUID
  ): Future[Carrier] = {
    val newCarrier = Carrier(
      id = carrierId,
      deliveryCategory = deliveryCategory,
      averageSpeed = averageSpeed,
      costPerRide = costPerRide
    )

    for {
      _ <- carrierRepository.createCarrier(newCarrier)
    } yield newCarrier
  }

  override def getAllCarriersCompatibilities(
      deliveryCategory: DeliveryCategory
  ): Future[Set[CarrierWithCompatibility]] = {
    for {
      carriers <- carrierRepository.getAllCarriers
    } yield carriers.map(carrier =>
      CarrierWithCompatibility(carrier, carrier.getCompatibilityForDeliveryCategory(deliveryCategory))
    )
  }

  override def getBestCarrierForDelivery(delivery: Delivery): Future[Option[Carrier]] = {
    for {
      carriers <- carrierRepository.getAllCarriers
    } yield {
      val carriersWithCompatibility =
        carriers.map(carrier => CarrierWithCompatibility(carrier, carrier.getCompatibilityForDelivery(delivery)))

      carriersWithCompatibility.headOption match {
        case Some(carrierWithCompatibility) =>
          val bestCarrier = carriersWithCompatibility.tail.foldLeft(carrierWithCompatibility) {
            case (bestCarrierCompatibility, newCarrierWithCompatibility) =>
              selectBestCarrierWithCompatibility(bestCarrierCompatibility, newCarrierWithCompatibility)
          }

          Some(bestCarrier.carrier)
        case None => None
      }
    }
  }

  private def selectBestCarrierWithCompatibility(
      carrierWithCompatibility: CarrierWithCompatibility,
      otherCarrierWithCompatibility: CarrierWithCompatibility
  ): CarrierWithCompatibility = {
    if (carrierWithCompatibility.carrierScore > otherCarrierWithCompatibility.carrierScore) {
      carrierWithCompatibility
    } else if (carrierWithCompatibility.carrierScore < otherCarrierWithCompatibility.carrierScore) {
      otherCarrierWithCompatibility
    } else {
      CarrierWithCompatibility.getCheaperCarrierOfTwo(carrierWithCompatibility, otherCarrierWithCompatibility)
    }
  }
}
