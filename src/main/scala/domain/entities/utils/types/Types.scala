package domain.entities.utils.types

import domain.entities.utils.types.DistanceInMeters.DistanceInMeters
import domain.entities.utils.types.DurationInSeconds.DurationInSeconds
import domain.entities.utils.types.Latitude.Latitude
import domain.entities.utils.types.LatitudeInRadiants.LatitudeInRadiants
import domain.entities.utils.types.LongNatural
import domain.entities.utils.types.LongNatural.LongNatural
import domain.entities.utils.types.Longitude.Longitude
import domain.entities.utils.types.LongitudeInRadiants.LongitudeInRadiants
import domain.entities.utils.types.Natural.Natural
import domain.entities.utils.types.NonZeroNaturalInt.NonZeroNaturalInt
import domain.entities.utils.types.SpeedInMetersPerSecond.SpeedInMetersPerSecond
import domain.entities.utils.types.WeightInGram.WeightInGram
import domain.entities.utils.types.{LongNatural, NonZeroNaturalInt}

import scala.language.strictEquality

object SpeedInMetersPerSecond {
  opaque type SpeedInMetersPerSecond <: NonZeroNaturalInt = NonZeroNaturalInt

  def apply(nonZeroNaturalInt: NonZeroNaturalInt): SpeedInMetersPerSecond = nonZeroNaturalInt
}

object WeightInGram {
  opaque type WeightInGram <: LongNatural = LongNatural

  val zero: WeightInGram = WeightInGram(LongNatural.zero)

  def apply(longNatural: LongNatural): WeightInGram = longNatural

  extension (weightInGram: WeightInGram) {
    def addition(x: WeightInGram): WeightInGram = {
      val result = weightInGram.plus(x)
      WeightInGram(result)
    }

    def getMaximum(x: WeightInGram): WeightInGram = {
      val result = weightInGram.maximum(x)
      WeightInGram(result)
    }
  }
}

object VolumeInMillim3 {
  opaque type VolumeInMillim3 <: LongNatural = LongNatural

  val zero: VolumeInMillim3 = VolumeInMillim3(LongNatural.zero)

  def apply(longNatural: LongNatural): VolumeInMillim3 = longNatural

  extension (volumeInMillim3: VolumeInMillim3) {
    def addition(x: VolumeInMillim3): VolumeInMillim3 = {
      val result = volumeInMillim3.plus(x)
      VolumeInMillim3(result)
    }
  }
}

object CostInMillis {
  opaque type CostInMillis <: LongNatural = LongNatural

  def apply(longNatural: LongNatural): CostInMillis = longNatural

  val zero: CostInMillis = CostInMillis(LongNatural.zero)
}

object DistanceInMeters {
  opaque type DistanceInMeters <: Natural = Natural

  def getAbsoluteValue(number: Int): DistanceInMeters = {
    val checkedValue: Natural = if (number > 0) {
      Natural.unsafe(number)
    } else {
      Natural.unsafe(-number)
    }

    DistanceInMeters(checkedValue)
  }

  def apply(natural: Natural): DistanceInMeters = natural

  def unsafe(int: Int): DistanceInMeters = Natural.unsafe(int)
}

object DurationInSeconds {
  opaque type DurationInSeconds <: Natural = Natural

  def fromDistanceAndSpeed(
      travelDistance: DistanceInMeters,
      averageSpeed: SpeedInMetersPerSecond
  ): DurationInSeconds = {
    val valueAsInt = travelDistance / averageSpeed
    val valueAsNatural = Natural.unsafe(valueAsInt)

    DurationInSeconds(valueAsNatural)
  }

  def apply(natural: Natural): DurationInSeconds = natural
}

object Latitude {
  opaque type Latitude <: Double = Double

  def apply(value: Double): Option[Latitude] = {
    if (value >= -90.0 && value <= 90.0) Some(value)
    else None
  }

  def unsafe(value: Double): Latitude = {
    value
  }

  extension (latitude: Latitude) {
    def toLatitudeInRadiants: LatitudeInRadiants = LatitudeInRadiants.unsafe(latitude.toRadians)
  }
}

object LatitudeInRadiants {
  opaque type LatitudeInRadiants <: Double = Double

  def apply(value: Double): Option[LatitudeInRadiants] = {
    if (value >= -math.Pi / 2.0 && value <= math.Pi / 2.0) Some(value)
    else None
  }

  def unsafe(value: Double): LatitudeInRadiants = {
    value
  }

  def fromLatitude(latitude: Latitude): LatitudeInRadiants = {
    LatitudeInRadiants.unsafe(latitude.toRadians)
  }

  extension (latitudeInRadiants: LatitudeInRadiants) {
    def toLatitudeInDegrees: Latitude = Latitude.unsafe(latitudeInRadiants.toDegrees)
  }
}

object Longitude {
  opaque type Longitude <: Double = Double

  def apply(value: Double): Option[Longitude] =
    if (value >= -math.Pi && value <= math.Pi) Some(value)
    else None

  def unsafe(value: Double): Longitude = {
    value
  }

  extension (longitude: Longitude) {
    def toLongitudeInRadiants: LongitudeInRadiants = LongitudeInRadiants.unsafe(longitude.toRadians)
  }
}

object LongitudeInRadiants {
  opaque type LongitudeInRadiants <: Double = Double

  def apply(value: Double): Option[LongitudeInRadiants] =
    if (value >= -math.Pi / 2 && value <= math.Pi / 2) Some(value)
    else None

  def unsafe(value: Double): LongitudeInRadiants = {
    value
  }

  def fromLongitude(longitude: Longitude): LongitudeInRadiants = {
    LongitudeInRadiants.unsafe(longitude.toRadians)
  }

  extension (longitudeInRadiants: LongitudeInRadiants) {
    def toLongitudeInDegrees: Longitude = Longitude.unsafe(longitudeInRadiants.toDegrees)
  }
}
