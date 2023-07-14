package domain.entities.mocks

import core.Mock
import domain.entities.utils.types.VolumeInMillim3.VolumeInMillim3
import domain.entities.utils.types.WeightInGram.WeightInGram
import domain.entities.utils.types.{VolumeInMillim3, WeightInGram}
import domain.entities.{Area, DeliveryCategory, DeliveryTimeRange}

final class DeliveryCategoryMock extends Mock[DeliveryCategory] {
  private var deliveryTimeRange: DeliveryTimeRange = new DeliveryTimeRangeMock().build()
  private var deliveryArea: Area = new AreaMock().build()
  private var totalWeight: WeightInGram = WeightInGram.zero
  private var totalVolume: VolumeInMillim3 = VolumeInMillim3.zero
  private var maxPackageWeight: WeightInGram = WeightInGram.zero

  def setDeliveryTimeRange(deliveryTimeRange: DeliveryTimeRange): DeliveryCategoryMock = {
    this.deliveryTimeRange = deliveryTimeRange
    this
  }

  def setDeliveryArea(deliveryArea: Area): DeliveryCategoryMock = {
    this.deliveryArea = deliveryArea
    this
  }

  def setTotalWeight(totalWeight: WeightInGram): DeliveryCategoryMock = {
    this.totalWeight = totalWeight
    this
  }

  def setTotalVolume(totalVolume: VolumeInMillim3): DeliveryCategoryMock = {
    this.totalVolume = totalVolume
    this
  }

  def setMaxPackageWeight(maxPackageWeight: WeightInGram): DeliveryCategoryMock = {
    this.maxPackageWeight = maxPackageWeight
    this
  }

  override def build(): DeliveryCategory = {
    DeliveryCategory(
      deliveryTimeRange = this.deliveryTimeRange,
      deliveryArea = this.deliveryArea,
      totalWeight = this.totalWeight,
      totalVolume = this.totalVolume,
      maxPackageWeight = this.maxPackageWeight
    )
  }
}
