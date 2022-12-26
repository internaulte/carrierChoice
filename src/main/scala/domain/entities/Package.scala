package domain.entities

import domain.entities.utils.Types.{VolumeInMillim3, WeightInGram}

final case class Package(weightInGram: WeightInGram, volume: VolumeInMillim3)
