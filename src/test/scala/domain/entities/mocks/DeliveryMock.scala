package domain.entities.mocks

import core.Mock
import domain.entities.utils.Point
import domain.entities.{Delivery, DeliveryTimeRange}
import domain.entities.Package
import domain.entities.utils.types.NonEmptyList.NonEmptyList
import domain.entities.utils.types.NonEmptyList

import java.util.UUID

final class DeliveryMock extends Mock[Delivery] {
  private var id: UUID = UUID.randomUUID()
  private var withdrawal: Point = new PointMock().build()
  private var destination: Point = new PointMock().build()
  private var deliveryTimeRange: DeliveryTimeRange = new DeliveryTimeRangeMock().build()
  private var packages: NonEmptyList[Package] = NonEmptyList(new PackageMock().build())

  def setId(id: UUID): DeliveryMock = {
    this.id = id
    this
  }

  def setWithdrawal(withdrawal: Point): DeliveryMock = {
    this.withdrawal = withdrawal
    this
  }

  def setDestination(destination: Point): DeliveryMock = {
    this.destination = destination
    this
  }

  def setDeliveryTimeRange(deliveryTimeRange: DeliveryTimeRange): DeliveryMock = {
    this.deliveryTimeRange = deliveryTimeRange
    this
  }

  def setPackages(packages: NonEmptyList[Package]): DeliveryMock = {
    this.packages = packages
    this
  }

  override def build(): Delivery = {
    Delivery(
      id = this.id,
      withdrawal = this.withdrawal,
      destination = this.destination,
      deliveryTimeRange = this.deliveryTimeRange,
      packages = this.packages
    )
  }
}
