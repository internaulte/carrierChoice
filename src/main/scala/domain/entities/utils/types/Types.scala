package domain.entities.utils.types

import domain.entities.utils.types.LongNatural.wrap
import domain.entities.utils.types.{LongNatural, NonZeroNaturalInt}
import zio.prelude.Subtype
import zio.prelude.newtypes.Natural

type NonZeroNaturalInt = NonZeroNaturalInt.Type
type LongNatural = LongNatural.Type

object SpeedInMetersPerSecond extends Subtype[NonZeroNaturalInt]
type SpeedInMetersPerSecond = SpeedInMetersPerSecond.Type

object WeightInGram extends Subtype[LongNatural]
type WeightInGram = WeightInGram.Type

object VolumeInMillim3 extends Subtype[LongNatural]
type VolumeInMillim3 = VolumeInMillim3.Type

object CostInMillis extends Subtype[LongNatural]
type CostInMillis = CostInMillis.Type

object DistanceInMeters extends Subtype[Natural] {
  def getAbsoluteValue(number: Int): DistanceInMeters = {
    val checkedValue: Natural = if (number > 0) {
      Natural.make(number).toOption.get
    } else {
      Natural.make(-number).toOption.get
    }

    wrap(checkedValue)
  }

  def fromInt(int: Int): Option[DistanceInMeters] = {
    Natural.make(int).toOption match
      case Some(value) => Some(DistanceInMeters(value))
      case None => None
  }
}
type DistanceInMeters = DistanceInMeters.Type

object DurationInSeconds extends Subtype[Natural] {
  def fromDistanceAndSpeed(travelDistance: DistanceInMeters, averageSpeed: SpeedInMetersPerSecond): DurationInSeconds = {
    val valueAsInt = DistanceInMeters.unwrap(travelDistance) / SpeedInMetersPerSecond.unwrap(averageSpeed)
    val valueAsNatural = Natural.make(valueAsInt).toOption.get
    wrap(valueAsNatural)
  }
}
type DurationInSeconds = DurationInSeconds.Type

object Latitude extends Subtype[Double]
type Latitude = Latitude.Type

object LatitudeInRadiants extends Subtype[Double] {
  def fromLatitude(latitude: Latitude): LatitudeInRadiants = {
    wrap(Latitude.unwrap(latitude).toRadians)
  }
}
type LatitudeInRadiants = LatitudeInRadiants.Type

object Longitude extends Subtype[Double]
type Longitude = Longitude.Type

object LongitudeInRadiants extends Subtype[Double] {
  def fromLongitude(longitude: Longitude): LongitudeInRadiants = {
    wrap(Longitude.unwrap(longitude).toRadians)
  }
}
type LongitudeInRadiants = LongitudeInRadiants.Type