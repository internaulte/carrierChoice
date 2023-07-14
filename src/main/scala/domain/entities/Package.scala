package domain.entities

import domain.entities.utils.types.VolumeInMillim3.VolumeInMillim3
import domain.entities.utils.types.WeightInGram.WeightInGram

final case class Package(weightInGram: WeightInGram, volume: VolumeInMillim3)
