package core

import org.scalatest.funspec.AsyncFunSpec
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}

trait UnitTestSpec extends AsyncFunSpec with BeforeAndAfterAll with BeforeAndAfterEach
