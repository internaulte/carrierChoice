package domain.entities

import core.UnitTestSpec
import domain.entities.utils.Point
import zio.prelude.newtypes.Natural

final class CarrierCompatibilityTest extends UnitTestSpec {
  describe("apply") {
    it("should return FullyCompatible if score max") {
      val scoreMax = Natural(4)

      assert(CarrierCompatibility(scoreMax) match {
        case FullyCompatible => true
        case PartiallyCompatible(_) => false
        case NotCompatible => false
      })
    }
    it("should return NotCompatible if score zero") {
      val scoreMin = Natural(0)

      assert(CarrierCompatibility(scoreMin) match {
        case FullyCompatible => false
        case PartiallyCompatible(_) => false
        case NotCompatible => true
      })
    }
    it("should return PartiallyCompatible if score between 0 and max") {
      val scoreMedium = Natural(3)

      assert(CarrierCompatibility(scoreMedium) match {
        case FullyCompatible => false
        case PartiallyCompatible(score) => scoreMedium == score
        case NotCompatible => false
      })
    }
  }
}
