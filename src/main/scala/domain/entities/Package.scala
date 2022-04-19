package domain.entities

import domain.entities.utils.Types.{VolumeInCm, WeightInKg}

final case class Package(weightInKg: WeightInKg, volume: VolumeInCm)
