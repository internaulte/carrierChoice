package domain.entities

import domain.entities.utils.Point
import zio.prelude.NonEmptySet

import java.util.UUID

final case class Delivery(
    id: UUID,
    withdrawal: Point,
    destination: Point,
    deliveryRange: DeliveryRange,
    packages: NonEmptySet[Package]
)
