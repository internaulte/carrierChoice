import adapters.controllers.interfaces.CarrierController
import cask.*

object Bootstrap extends Main {
  private val controller = CarrierController.instance

  override val allRoutes: Seq[Routes] = Seq(controller)
}
