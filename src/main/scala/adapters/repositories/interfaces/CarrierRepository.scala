package adapters.repositories.interfaces

import adapters.repositories.CarrierRepositoryImpl
import domain.entities.utils.types.SpeedInMetersPerSecond
import domain.entities.{Carrier, DeliveryCategory}

import java.util.UUID
import scala.concurrent.Future

trait CarrierRepository {
  def createCarrier(carrier: Carrier): Future[Unit]

  def getAllCarriers: Future[Set[Carrier]]

  def deleteAll(): Future[Unit]
}

object CarrierRepository {
  lazy val instance: CarrierRepository = new CarrierRepositoryImpl()
}
