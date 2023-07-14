package domain.entities

import core.UnitTestSpec
import domain.entities.utils.Point
import domain.entities.utils.types.Natural

final class CarrierCompatibilityTest extends UnitTestSpec {
  describe("apply") {
    it("should return FullyCompatible if score max") {
      val scoreMax = Natural.unsafe(4)

      assert(CarrierCompatibility(scoreMax) match {
        case FullyCompatible => true
        case PartiallyCompatible(_) => false
        case NotCompatible => false
      })
    }
    it("should return NotCompatible if score zero") {
      val scoreMin = Natural.zero

      assert(CarrierCompatibility(scoreMin) match {
        case FullyCompatible => false
        case PartiallyCompatible(_) => false
        case NotCompatible => true
      })
    }
    it("should return PartiallyCompatible if score between 0 and max") {
      val scoreMedium = Natural.unsafe(3)

      assert(CarrierCompatibility(scoreMedium) match {
        case FullyCompatible => false
        case PartiallyCompatible(score) => scoreMedium == score
        case NotCompatible => false
      })
    }
  }
}
