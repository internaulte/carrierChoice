package adapters.controllers

import adapters.controllers.dtos.*
import domain.entities.utils.types.*
import domain.usecases.interfaces.CarrierUseCases

import java.util.UUID
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

object CarrierController extends cask.MainRoutes {
  private val carrierUseCases = CarrierUseCases.instance

  @cask.post("/carrier")
  def createCarrier(
      deliveryCategory: DeliveryCategoryDto,
      averageSpeed: SpeedInMetersPerSecond,
      costPerRide: CostInMillis
  ): Unit = {
    val result = for {
      _ <- canUserCreate
      newCarrier <- carrierUseCases.createCarrier(
        deliveryCategory.toDeliveryCategory,
        averageSpeed,
        costPerRide,
        carrierId = getUUID()
      )
    } yield CarrierDto(newCarrier)

    Await.result(result, Duration.Inf)
  }

  @cask.post("/carrier/compatibilities")
  def getAllCarriersCompatibilitiesOrderedByScoreAndPrice(deliveryCategoryDto: DeliveryCategoryDto): Unit = {
    val result = for {
      _ <- canUserRead
      carriersWithCompatibility <- carrierUseCases.getAllCarriersCompatibilities(
        deliveryCategory = deliveryCategoryDto.toDeliveryCategory
      )
    } yield {
      val sortedCarriersWithCompatibility = carriersWithCompatibility.toSeq.sortBy { carrierWithCompatibility =>
        (-carrierWithCompatibility.carrierScore, -carrierWithCompatibility.carrier.costPerRide)
      }

      sortedCarriersWithCompatibility.map(CarrierWithCompatibilityDto(_))
    }

    Await.result(result, Duration.Inf)
  }

  @cask.post("/carrier/delivery")
  def getBestCarrierForDelivery(deliveryDto: DeliveryDto): Unit = {
    val result = for {
      _ <- canUserRead
      maybeCarrier <- carrierUseCases.getBestCarrierForDelivery(
        delivery = deliveryDto.toDelivery
      )
    } yield {
      maybeCarrier.map(CarrierDto(_))
    }

    Await.result(result, Duration.Inf)
  }

  //initialize() TODO : add readers

  private def canUserCreate: Future[Boolean] = {
    Future.successful(true)
  }

  private def canUserRead: Future[Boolean] = {
    Future.successful(true)
  }

  private def getUUID(): UUID = UUID.randomUUID()
}
