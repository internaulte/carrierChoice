package adapters.controllers.dtos

import domain.entities.Package
import domain.entities.utils.types.{VolumeInMillim3, WeightInGram}
import upickle.default.*

protected[controllers] final case class PackageDto(weightInGram: Long, volume: Long) derives ReadWriter {
  def toPackage: Option[Package] = {
    for {
      finalWeightInGram <- WeightInGram(weightInGram)
      finalVolume <- VolumeInMillim3(volume)
    } yield Package(finalWeightInGram, finalVolume)
  }
}
