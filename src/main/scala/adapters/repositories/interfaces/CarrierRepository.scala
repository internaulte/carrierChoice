package adapters.repositories.interfaces

import adapters.repositories.CarrierRepositoryImpl
import domain.entities.{Carrier, DeliveryCategory}
import domain.entities.utils.Types.SpeedInMetersPerSecond
import java.util.UUID
import scala.concurrent.Future

trait CarrierRepository {
  def createCarrier(carrier: Carrier): Future[Unit]

  def getAllCarriers: Future[Set[Carrier]]
}

object CarrierRepository {
  lazy val instance: CarrierRepository = new CarrierRepositoryImpl()
}
