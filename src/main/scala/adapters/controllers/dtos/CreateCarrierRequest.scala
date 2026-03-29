package adapters.controllers.dtos

import upickle.default.*

protected[controllers] final case class CreateCarrierRequest(
    deliveryCategory: DeliveryCategoryDto,
    averageSpeed: Int,
    costPerRide: Long
) derives ReadWriter
