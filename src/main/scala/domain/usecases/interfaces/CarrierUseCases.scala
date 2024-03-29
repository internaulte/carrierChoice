package domain.usecases.interfaces

import adapters.repositories.interfaces.CarrierRepository
import domain.entities.*
import domain.entities.utils.types.CostInMillis.CostInMillis
import domain.entities.utils.types.SpeedInMetersPerSecond.SpeedInMetersPerSecond
import domain.usecases.CarrierUseCasesImpl

import java.util.UUID
import scala.concurrent.Future

trait CarrierUseCases {
  def createCarrier(
      deliveryCategory: DeliveryCategory,
      averageSpeed: SpeedInMetersPerSecond,
      costPerRide: CostInMillis,
      carrierId: UUID
  ): Future[Carrier]

  def getAllCarriersCompatibilities(
      deliveryCategory: DeliveryCategory
  ): Future[Set[CarrierWithCompatibility]]

  def getBestCarrierForDelivery(delivery: Delivery): Future[Option[Carrier]]
}

object CarrierUseCases {
  lazy val instance: CarrierUseCases = new CarrierUseCasesImpl(carrierRepository = CarrierRepository.instance)
}
