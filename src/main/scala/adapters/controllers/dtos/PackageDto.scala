package adapters.controllers.dtos

import domain.entities.utils.types.{LongNatural, VolumeInMillim3, WeightInGram}
import domain.entities.Package
import domain.entities.utils.types.VolumeInMillim3.VolumeInMillim3
import domain.entities.utils.types.WeightInGram.WeightInGram

protected[controllers] final case class PackageDto(weightInGram: Long, volume: Long) {
  def toPackage: Option[Package] = {
    for {
      finalWeightInGram <- WeightInGram(weightInGram)
      finalVolume <- VolumeInMillim3(volume)
    } yield Package(finalWeightInGram, finalVolume)
  }
}
