package domain.entities

import domain.entities.utils.Types.{PositiveOrZeroReal, SpeedInKmH, VolumeInCm, WeightInKg}
import zio.prelude.newtypes.Natural

import java.util.UUID

final case class Carrier(
    id: UUID,
    deliveryCategory: DeliveryCategory,
    averageSpeedKmH: SpeedInKmH,
    costPerRide: PositiveOrZeroReal
)
