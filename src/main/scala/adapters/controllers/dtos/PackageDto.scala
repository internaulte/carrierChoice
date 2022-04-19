package adapters.controllers.dtos

import domain.entities.utils.Types.{VolumeInCm, WeightInKg}
import domain.entities.Package

protected[controllers] final case class PackageDto(weightInKg: WeightInKg, volume: VolumeInCm) {
  def toPackage: Package = {
    Package(weightInKg, volume)
  }
}
