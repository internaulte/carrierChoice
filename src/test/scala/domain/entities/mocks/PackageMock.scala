package domain.entities.mocks

import core.Mock
import domain.entities.Package
import domain.entities.utils.types.{VolumeInMillim3, WeightInGram}
import domain.entities.utils.types.VolumeInMillim3.VolumeInMillim3
import domain.entities.utils.types.WeightInGram.WeightInGram

final class PackageMock extends Mock[Package] {
  private var weightInGram: WeightInGram = WeightInGram.zero
  private var volume: VolumeInMillim3 = VolumeInMillim3.zero

  def setWeight(weightInGram: WeightInGram): PackageMock = {
    this.weightInGram = weightInGram
    this
  }

  def setVolume(volume: VolumeInMillim3): PackageMock = {
    this.volume = volume
    this
  }

  override def build(): Package = {
    Package(
      weightInGram = this.weightInGram,
      volume = this.volume
    )
  }
}
