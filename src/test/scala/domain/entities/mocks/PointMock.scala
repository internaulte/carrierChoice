package domain.entities.mocks

import core.Mock
import domain.entities.utils.Point
import domain.entities.utils.types.Latitude.Latitude
import domain.entities.utils.types.Longitude.Longitude
import domain.entities.utils.types.{Latitude, Longitude}

final class PointMock extends Mock[Point] {
  private var latitude: Latitude = Latitude.unsafe(5.0)
  private var longitude: Longitude = Longitude.unsafe(5.0)

  def setLatitude(latitude: Latitude): PointMock = {
    this.latitude = latitude
    this
  }

  def setLongitude(longitude: Longitude): PointMock = {
    this.longitude = longitude
    this
  }

  override def build(): Point = {
    Point(
      latitude = this.latitude,
      longitude = this.longitude
    )
  }
}
