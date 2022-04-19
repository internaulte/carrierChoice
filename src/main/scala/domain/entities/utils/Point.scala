package domain.entities.utils

import domain.entities.utils.Types.{DistanceInKm, Latitude, Longitude, PositiveOrZeroReal}
import zio.prelude.ZValidation

import scala.math.*

final case class Point(latitude: Latitude, longitude: Longitude) {
  lazy val latitudeAsRadians: Latitude = math.toRadians(latitude)
  lazy val longitudeAsRadians: Latitude = math.toRadians(longitude)

  def distanceInKmTo(otherPoint: Point): DistanceInKm = {
    val (dLat, dLon) = (otherPoint.latitudeAsRadians, otherPoint.longitudeAsRadians)
    val deltaLat = dLat - this.latitudeAsRadians
    val deltaLon = dLon - this.longitudeAsRadians
    val hav = pow(sin(deltaLat / 2), 2) + cos(this.latitudeAsRadians) * cos(dLat) * pow(sin(deltaLon / 2), 2)
    val greatCircleDistance = 2 * atan2(sqrt(hav), sqrt(1 - hav))
    val earthRadiusMiles = 3958.761
    val earthRadiusMeters = earthRadiusMiles / 0.00062137
    val distanceInMeters = earthRadiusMeters * greatCircleDistance
    val result = distanceInMeters / 1000

    if (result < 0) {
      PositiveOrZeroReal.unsafeMake(-result)
    } else {
      PositiveOrZeroReal.unsafeMake(result)
    }
  }

}
