package domain.entities.mocks

import core.Mock
import domain.entities.Area
import domain.entities.utils.Point
import domain.entities.utils.types.DistanceInMeters.DistanceInMeters
import domain.entities.utils.types.DistanceInMeters
import domain.entities.utils.types.Natural

final class AreaMock extends Mock[Area] {
  private var center: Point = new PointMock().build()
  private var radius: DistanceInMeters = DistanceInMeters(Natural.unsafe(5))

  def setCenter(center: Point): AreaMock = {
    this.center = center
    this
  }

  def setRadius(radius: DistanceInMeters): AreaMock = {
    this.radius = radius
    this
  }

  override def build(): Area = {
    Area(
      center = this.center,
      radius = this.radius
    )
  }
}
