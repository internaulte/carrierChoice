package adapters.repositories

import adapters.repositories.interfaces.CarrierRepository
import domain.entities.Carrier

import scala.collection.mutable
import scala.concurrent.Future

protected final class CarrierRepositoryImpl() extends CarrierRepository {
  override def createCarrier(carrier: Carrier): Future[Unit] = {
    Future.successful(carriersDataBase += carrier)
  }

  override def getAllCarriers: Future[Set[Carrier]] = {
    Future.successful(carriersDataBase.toSet)
  }

  override def deleteAll(): Future[Unit] = {
    Future.successful(carriersDataBase.clear())
  }

  private val carriersDataBase: mutable.Set[Carrier] = scala.collection.mutable.Set.empty[Carrier]
}
