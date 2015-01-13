package com.htwg.test

import org.specs2.mutable._
import com.htwg.reversi.model.Position;
import com.htwg.reversi.model.Size

class SizeSpec extends SpecificationWithJUnit {
  "A Size set to 3, 3 " should {
    val size = new Size(3, 3)

    "have the center column value 1" in {
      size.center.column must be_==(1)
    }

    "have the center row value 1" in {
      size.center.row must be_==(1)
    }
  }
}