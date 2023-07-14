package adapters.controllers.dtos

import domain.entities.utils.types.{VolumeInMillim3, WeightInGram}
import domain.entities.Package
import domain.entities.utils.types.VolumeInMillim3.VolumeInMillim3
import domain.entities.utils.types.WeightInGram.WeightInGram

protected[controllers] final case class PackageDto(weightInKg: WeightInGram, volume: VolumeInMillim3) {
  def toPackage: Package = Package(weightInKg, volume)
}
