package domain.entities.utils

object Types {
  type LongNatural = LongNatural.Type
  type PositiveOrZeroReal = PositiveOrZeroReal.Type
  type SpeedInKmH = PositiveOrZeroReal
  type WeightInKg = PositiveOrZeroReal
  type VolumeInCm = PositiveOrZeroReal

  type DistanceInKm = PositiveOrZeroReal
  type DurationInSeconds = LongNatural

  type Latitude = Double
  type Longitude = Double
}
