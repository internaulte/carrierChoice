package adapters.controllers

import adapters.controllers.dtos.*
import adapters.repositories.interfaces.CarrierRepository
import domain.usecases.interfaces.CarrierUseCases
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import upickle.default.*

import scala.concurrent.Await
import scala.concurrent.duration.Duration

final class CarrierControllerImplTest extends AnyFunSpec with BeforeAndAfterAll with BeforeAndAfterEach {

  private val repository = CarrierRepository.instance
  private val useCases = CarrierUseCases.create(repository)
  private val controller = new CarrierControllerImpl(useCases)
  private val testHost = "localhost"
  private val testPort = 8765
  private given cask.util.Logger = cask.util.Logger.Console()
  private val dispatchTrie = cask.main.Main.prepareDispatchTrie(Seq(controller))
  private val server = io.undertow.Undertow.builder
    .addHttpListener(testPort, testHost)
    .setHandler(
      io.undertow.server.handlers.BlockingHandler(
        cask.main.Main.DefaultHandler(
          dispatchTrie,
          mainDecorators = Seq.empty,
          debugMode = true,
          handleNotFound = _ => cask.Response("", statusCode = 404),
          handleMethodNotAllowed = _ => cask.Response("", statusCode = 405),
          handleError = (_, _, _, _) => cask.Response("", statusCode = 500)
        )
      )
    )
    .build

  private def baseUrl: String = s"http://$testHost:$testPort"

  override def beforeAll(): Unit = {
    server.start()
  }

  override def afterAll(): Unit = {
    server.stop()
  }

  override def beforeEach(): Unit = {
    Await.result(repository.deleteAll(), Duration.Inf)
  }

  private val validDeliveryTimeRangeDto = DeliveryTimeRangeDto(
    startInterval = "2026-01-01T08:00:00",
    endInterval = "2026-01-01T20:00:00"
  )

  private val validAreaDto = AreaDto(
    center = PointDto(latitude = 43.0, longitude = 5.0),
    radius = 10000
  )

  private val validDeliveryCategoryDto = DeliveryCategoryDto(
    deliveryTimeRange = validDeliveryTimeRangeDto,
    deliveryArea = validAreaDto,
    totalWeight = 50,
    totalVolume = 50,
    maxPackageWeight = 20
  )

  private val validCreateCarrierRequest = CreateCarrierRequest(
    deliveryCategory = validDeliveryCategoryDto,
    averageSpeed = 10,
    costPerRide = 500
  )

  private val validDeliveryDto = DeliveryDto(
    id = "550e8400-e29b-41d4-a716-446655440000",
    withdrawal = PointDto(latitude = 43.0, longitude = 5.0),
    destination = PointDto(latitude = 43.1, longitude = 5.1),
    deliveryTimeRange = validDeliveryTimeRangeDto,
    packages = Seq(PackageDto(weightInGram = 10, volume = 100))
  )

  private def post(path: String, body: String): requests.Response =
    requests.post(s"$baseUrl$path", data = body, check = false)

  describe("POST /carrier") {
    it("should return 201 and persist carrier when parameters are valid") {
      val response = post("/carrier", write(validCreateCarrierRequest))

      assert(response.statusCode == 201)
      assert(response.text().contains("\"id\""))
      assert(response.text().contains("\"averageSpeed\":10"))

      val carriers = Await.result(repository.getAllCarriers, Duration.Inf)
      assert(carriers.size == 1)
    }

    it("should persist multiple carriers across multiple calls") {
      post("/carrier", write(validCreateCarrierRequest))
      post("/carrier", write(validCreateCarrierRequest))

      val carriers = Await.result(repository.getAllCarriers, Duration.Inf)
      assert(carriers.size == 2)
    }

    it("should return 400 when JSON is invalid") {
      val response = post("/carrier", "not json")

      assert(response.statusCode == 400)

      val carriers = Await.result(repository.getAllCarriers, Duration.Inf)
      assert(carriers.isEmpty)
    }

    it("should return 400 when averageSpeed is zero") {
      val response = post("/carrier", write(validCreateCarrierRequest.copy(averageSpeed = 0)))

      assert(response.statusCode == 400)
    }

    it("should return 400 when costPerRide is negative") {
      val response = post("/carrier", write(validCreateCarrierRequest.copy(costPerRide = -1)))

      assert(response.statusCode == 400)
    }

    it("should return 400 when deliveryCategory has invalid latitude") {
      val invalidCategory = validDeliveryCategoryDto.copy(
        deliveryArea = validAreaDto.copy(center = PointDto(latitude = 999.0, longitude = 5.0))
      )
      val response = post("/carrier", write(validCreateCarrierRequest.copy(deliveryCategory = invalidCategory)))

      assert(response.statusCode == 400)
    }

    it("should return 400 when deliveryTimeRange has invalid date format") {
      val invalidCategory = validDeliveryCategoryDto.copy(
        deliveryTimeRange = DeliveryTimeRangeDto(startInterval = "not-a-date", endInterval = "2026-01-01T20:00:00")
      )
      val response = post("/carrier", write(validCreateCarrierRequest.copy(deliveryCategory = invalidCategory)))

      assert(response.statusCode == 400)
    }
  }

  describe("POST /carrier/compatibilities") {
    it("should return 200 with compatibilities for existing carriers") {
      post("/carrier", write(validCreateCarrierRequest))

      val response = post("/carrier/compatibilities", write(validDeliveryCategoryDto))

      assert(response.statusCode == 200)
      assert(response.text().contains("carrierCompatibility"))
    }

    it("should return 200 with empty array when no carriers exist") {
      val response = post("/carrier/compatibilities", write(validDeliveryCategoryDto))

      assert(response.statusCode == 200)
      assert(response.text() == "[]")
    }

    it("should return 400 when JSON is invalid") {
      val response = post("/carrier/compatibilities", "not json")

      assert(response.statusCode == 400)
    }

    it("should return 400 when totalWeight is negative") {
      val response = post("/carrier/compatibilities", write(validDeliveryCategoryDto.copy(totalWeight = -1)))

      assert(response.statusCode == 400)
    }
  }

  describe("POST /carrier/delivery") {
    it("should return 200 with best carrier when carriers exist") {
      post("/carrier", write(validCreateCarrierRequest))

      val response = post("/carrier/delivery", write(validDeliveryDto))

      assert(response.statusCode == 200)
      assert(response.text().contains("\"id\""))
    }

    it("should return 204 when no carriers exist") {
      val response = post("/carrier/delivery", write(validDeliveryDto))

      assert(response.statusCode == 204)
    }

    it("should return 400 when JSON is invalid") {
      val response = post("/carrier/delivery", "not json")

      assert(response.statusCode == 400)
    }

    it("should return 400 when delivery id is not a valid UUID") {
      val response = post("/carrier/delivery", write(validDeliveryDto.copy(id = "not-a-uuid")))

      assert(response.statusCode == 400)
    }

    it("should return 400 when packages list is empty") {
      val response = post("/carrier/delivery", write(validDeliveryDto.copy(packages = Seq.empty)))

      assert(response.statusCode == 400)
    }

    it("should return 400 when package has negative weight") {
      val invalidPackage = PackageDto(weightInGram = -5, volume = 100)
      val response = post("/carrier/delivery", write(validDeliveryDto.copy(packages = Seq(invalidPackage))))

      assert(response.statusCode == 400)
    }
  }

  describe("end-to-end isolation") {
    it("should start with empty repository after beforeEach reset") {
      val carriers = Await.result(repository.getAllCarriers, Duration.Inf)
      assert(carriers.isEmpty)
    }

    it("should not see carriers created in other tests") {
      post("/carrier", write(validCreateCarrierRequest))
      val carriers = Await.result(repository.getAllCarriers, Duration.Inf)
      assert(carriers.size == 1)
    }

    it("should again start with empty repository proving reset works") {
      val carriers = Await.result(repository.getAllCarriers, Duration.Inf)
      assert(carriers.isEmpty)
    }
  }
}
