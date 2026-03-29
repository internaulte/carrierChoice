package adapters.controllers.interfaces

import adapters.controllers.CarrierControllerImpl
import domain.usecases.interfaces.CarrierUseCases

object CarrierController {
  lazy val instance: cask.Routes = new CarrierControllerImpl(carrierUseCases = CarrierUseCases.instance)
}
