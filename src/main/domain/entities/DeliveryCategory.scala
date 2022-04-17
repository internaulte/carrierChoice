package domain.entities

import domain.entities.utils.Types.{VolumeInCm, WeightInKg}

final case class DeliveryCategory(
    deliveryRange: DeliveryRange,
    deliveryArea: Area,
    maxWeightKg: WeightInKg,
    maxVolume: VolumeInCm,
    maxHandlingWeight: WeightInKg
)
