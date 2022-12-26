package adapters.repositories

import adapters.repositories.interfaces.CarrierRepository
import domain.entities.{Carrier, DeliveryCategory}
import domain.entities.utils.Types.SpeedInMetersPerSecond
import java.util.UUID
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

protected class CarrierRepositoryImpl() extends CarrierRepository {
  override def createCarrier(carrier: Carrier): Future[Unit] = {
    Future.successful(carriersDataBase += carrier)
  }

  override def getAllCarriers: Future[Set[Carrier]] = {
    Future.successful(carriersDataBase.toSet)
  }

  private val carriersDataBase: mutable.Set[Carrier] = scala.collection.mutable.Set.empty[Carrier]
}
