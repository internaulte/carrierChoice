package domain.entities

import domain.entities.utils.types.{VolumeInMillim3, WeightInGram}

final case class Package(weightInGram: WeightInGram, volume: VolumeInMillim3)
