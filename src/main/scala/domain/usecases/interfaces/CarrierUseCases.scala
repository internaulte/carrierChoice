package domain.usecases.interfaces

import adapters.repositories.interfaces.CarrierRepository
import domain.entities.utils.Types.{PositiveOrZeroReal, SpeedInKmH}
import domain.entities.{Carrier, CarrierCompatibility, CarrierWithCompatibility, Delivery, DeliveryCategory}
import domain.usecases.CarrierUseCasesImpl

import java.util.UUID
import scala.concurrent.Future

trait CarrierUseCases {
  def createCarrier(
      deliveryCategory: DeliveryCategory,
      averageSpeed: SpeedInKmH,
      costPerRide: PositiveOrZeroReal,
      carrierId: UUID = UUID.randomUUID()
  ): Future[Carrier]

  def getAllCarriersCompatibilities(
      deliveryCategory: DeliveryCategory
  ): Future[Set[CarrierWithCompatibility]]

  def getBestCarrierForDelivery(delivery: Delivery): Future[Option[Carrier]]
}

object CarrierUseCases {
  lazy val instance: CarrierUseCases = new CarrierUseCasesImpl(carrierRepository = CarrierRepository.instance)
}
