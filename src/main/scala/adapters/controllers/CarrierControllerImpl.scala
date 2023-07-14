package adapters.controllers

import adapters.controllers.dtos.*
import adapters.controllers.interfaces.CarrierController
import domain.entities.utils.types.*
import domain.usecases.interfaces.CarrierUseCases

import java.util.UUID
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import cask.model.Response

import scala.concurrent.ExecutionContext.Implicits.global

class CarrierControllerImpl(override protected val carrierUseCases: CarrierUseCases) extends CarrierController
