package adapters.controllers

import adapters.controllers.dtos.*
import cask.model.Response
import domain.entities.utils.types.{CostInMillis, LongNatural, NonZeroNaturalInt, SpeedInMetersPerSecond}
import domain.usecases.interfaces.CarrierUseCases
import upickle.default.*

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Try

protected[controllers] final class CarrierControllerImpl(carrierUseCases: CarrierUseCases) extends cask.Routes {

  private val badRequestResponse: Response.Raw = cask.Response("Bad Request: invalid parameters", statusCode = 400)

  @cask.post("/carrier")
  def createCarrier(request: cask.Request): Response.Raw = {
    val validated = for {
      req <- Try(read[CreateCarrierRequest](request.text())).toOption
      validDeliveryCategory <- req.deliveryCategory.toDeliveryCategory
      validSpeed <- NonZeroNaturalInt(req.averageSpeed)
      validCost <- LongNatural(req.costPerRide)
    } yield (validDeliveryCategory, SpeedInMetersPerSecond(validSpeed), CostInMillis(validCost))

    validated match {
      case None => badRequestResponse
      case Some((validDeliveryCategory, averageSpeed, costPerRide)) =>
        val result = for {
          _ <- canUserCreate
          newCarrier <- carrierUseCases.createCarrier(
            validDeliveryCategory,
            averageSpeed,
            costPerRide,
            carrierId = getUUID
          )
        } yield CarrierDto(newCarrier)

        cask.Response(write(Await.result(result, Duration.Inf)), statusCode = 201)
    }
  }

  @cask.post("/carrier/compatibilities")
  def getAllCarriersCompatibilitiesOrderedByScoreAndPrice(request: cask.Request): Response.Raw = {
    val parsed = Try(read[DeliveryCategoryDto](request.text())).toOption
    parsed.flatMap(_.toDeliveryCategory) match {
      case None => badRequestResponse
      case Some(validDeliveryCategory) =>
        val result = for {
          _ <- canUserRead
          carriersWithCompatibility <- carrierUseCases.getAllCarriersCompatibilities(
            deliveryCategory = validDeliveryCategory
          )
        } yield {
          val sortedCarriersWithCompatibility = carriersWithCompatibility.toSeq.sortBy { carrierWithCompatibility =>
            (-carrierWithCompatibility.carrierScore, -carrierWithCompatibility.carrier.costPerRide)
          }

          sortedCarriersWithCompatibility.map(CarrierWithCompatibilityDto(_))
        }

        cask.Response(write(Await.result(result, Duration.Inf)))
    }
  }

  @cask.post("/carrier/delivery")
  def getBestCarrierForDelivery(request: cask.Request): Response.Raw = {
    val parsed = Try(read[DeliveryDto](request.text())).toOption
    parsed.flatMap(_.toDelivery) match {
      case None => badRequestResponse
      case Some(validDelivery) =>
        val result = for {
          _ <- canUserRead
          maybeCarrier <- carrierUseCases.getBestCarrierForDelivery(delivery = validDelivery)
        } yield {
          maybeCarrier.map(CarrierDto(_))
        }

        Await.result(result, Duration.Inf) match
          case Some(bestCarrier) => cask.Response(write(bestCarrier))
          case None => cask.Response("", statusCode = 204)
    }
  }

  private def canUserCreate: Future[Boolean] = {
    Future.successful(true)
  }

  private def canUserRead: Future[Boolean] = {
    Future.successful(true)
  }

  private def getUUID: UUID = UUID.randomUUID()

  initialize()
}
