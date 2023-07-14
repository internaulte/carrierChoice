package core

import org.scalatest.funspec.AsyncFunSpec
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatestplus.mockito.MockitoSugar

trait UnitTestSpec extends AsyncFunSpec with MockitoSugar with BeforeAndAfterAll with BeforeAndAfterEach
