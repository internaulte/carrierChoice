package domain.entities.mocks

import core.Mock
import domain.entities.utils.types.CostInMillis.CostInMillis
import domain.entities.utils.types.{CostInMillis, NonZeroNaturalInt, SpeedInMetersPerSecond}
import domain.entities.utils.types.SpeedInMetersPerSecond.SpeedInMetersPerSecond
import domain.entities.{Carrier, DeliveryCategory}

import java.util.UUID

final class CarrierMock extends Mock[Carrier] {
  private var id: UUID = UUID.randomUUID()
  private var deliveryCategory: DeliveryCategory = new DeliveryCategoryMock().build()
  private var averageSpeed: SpeedInMetersPerSecond = SpeedInMetersPerSecond(NonZeroNaturalInt.one)
  private var costPerRide: CostInMillis = CostInMillis.zero

  def setId(id: UUID): CarrierMock = {
    this.id = id
    this
  }

  def setDeliveryCategory(deliveryCategory: DeliveryCategory): CarrierMock = {
    this.deliveryCategory = deliveryCategory
    this
  }

  def setAverageSpeed(averageSpeed: SpeedInMetersPerSecond): CarrierMock = {
    this.averageSpeed = averageSpeed
    this
  }

  def setCostPerRide(costPerRide: CostInMillis): CarrierMock = {
    this.costPerRide = costPerRide
    this
  }

  override def build(): Carrier = {
    Carrier(
      id = this.id,
      deliveryCategory = this.deliveryCategory,
      averageSpeed = this.averageSpeed,
      costPerRide = this.costPerRide
    )
  }
}
