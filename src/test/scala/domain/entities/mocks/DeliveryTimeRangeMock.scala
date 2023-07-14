package domain.entities.mocks

import core.Mock
import domain.entities.DeliveryTimeRange

import java.time.LocalDateTime

final class DeliveryTimeRangeMock extends Mock[DeliveryTimeRange] {
  private var startInterval: LocalDateTime = LocalDateTime.now()
  private var endInterval: LocalDateTime = LocalDateTime.now()

  def setStartInterval(startInterval: LocalDateTime): DeliveryTimeRangeMock = {
    this.startInterval = startInterval
    this
  }

  def setEndInterval(endInterval: LocalDateTime): DeliveryTimeRangeMock = {
    this.endInterval = endInterval
    this
  }

  override def build(): DeliveryTimeRange = {
    DeliveryTimeRange(
      startInterval = this.startInterval,
      endInterval = this.endInterval
    )
  }
}
