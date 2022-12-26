package adapters.controllers.dtos

import domain.entities.utils.Types.{VolumeInMillim3, WeightInGram}
import domain.entities.Package

protected[controllers] final case class PackageDto(weightInKg: WeightInGram, volume: VolumeInMillim3) {
  def toPackage: Package = {
    Package(weightInKg, volume)
  }
}
