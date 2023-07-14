package domain.usecases

import adapters.repositories.interfaces.CarrierRepository
import domain.entities.utils.types.SpeedInMetersPerSecond
import domain.entities.*
import domain.entities.utils.types.CostInMillis.CostInMillis
import domain.entities.utils.types.SpeedInMetersPerSecond.SpeedInMetersPerSecond
import domain.usecases.interfaces.CarrierUseCases

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

protected class CarrierUseCasesImpl(carrierRepository: CarrierRepository) extends CarrierUseCases {
  override def createCarrier(
      deliveryCategory: DeliveryCategory,
      averageSpeed: SpeedInMetersPerSecond,
      costPerRide: CostInMillis,
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
      carriers.headOption match {
        case Some(carrier) =>
          val carrierAndCompatibility = CarrierWithCompatibility(carrier, carrier.getCompatibilityForDelivery(delivery))
          val bestCarrier = carriers.tail.foldLeft(carrierAndCompatibility) {
            case (bestCarrierWithCompatibility, newCarrier) =>
              val newCarrierWithCompatibility = CarrierWithCompatibility(
                carrier = newCarrier,
                carrierCompatibility = newCarrier.getCompatibilityForDelivery(delivery)
              )
              selectBestCarrierWithCompatibility(bestCarrierWithCompatibility, newCarrierWithCompatibility)
          }

          Some(bestCarrier.carrier)
        case None => None
      }
    }
  }

  private[usecases] def selectBestCarrierWithCompatibility(
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
