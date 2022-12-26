package domain.entities.utils

import domain.entities.utils.Types.{DistanceInMeters, Latitude, Longitude, LatitudeInRadiants}
import domain.entities.utils.LongNatural

import zio.prelude.ZValidation

import scala.math.*

final case class Point(latitude: Latitude, longitude: Longitude) {
  lazy val latitudeAsRadians: LatitudeInRadiants = math.toRadians(latitude)
  lazy val longitudeAsRadians: LatitudeInRadiants = math.toRadians(longitude)

  def distanceInMetersTo(otherPoint: Point): DistanceInMeters = {
    val (dLat, dLon) = (otherPoint.latitudeAsRadians, otherPoint.longitudeAsRadians)
    val deltaLat = dLat - this.latitudeAsRadians
    val deltaLon = dLon - this.longitudeAsRadians
    val hav = pow(sin(deltaLat / 2), 2) + cos(this.latitudeAsRadians) * cos(dLat) * pow(sin(deltaLon / 2), 2)
    val greatCircleDistance = 2 * atan2(sqrt(hav), sqrt(1 - hav))
    val earthRadiusMiles = 3958.761
    val earthRadiusMeters = earthRadiusMiles / 0.00062137
    val distanceInMeters = earthRadiusMeters * greatCircleDistance

    LongNatural.getAbsoluteValue(distanceInMeters.toLong)
  }

}
