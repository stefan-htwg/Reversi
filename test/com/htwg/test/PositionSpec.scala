package com.htwg.test

import org.specs2.mutable._
import com.htwg.reversi.model.Position;
import com.htwg.reversi.model.Size

class PositionSpec extends SpecificationWithJUnit {
  "A new Position set to 0 " should {
    val position = new Position(0, 0)

    "have row value 0" in {
      position.row must be_==(0)
    }

    "have column value 0" in {
      position.column must be_==(0)
    }
  }

  "A Position with value 1, 2 added by 2, 1" should {
    val position = new Position(1, 2)
    val offset = new Position(2, 1)

    val result = position add offset;

    "have row value 3" in {
      result.row must be_==(3)
    }

    "have column value 3" in {
      result.column must be_==(3)
    }
  }

  "A Position with value 1, 2" should {
    val position = new Position(1, 2)

    "not be out of bounce for size 3 3" in {
      val size = new Size(3, 3)
      position isOutOfBounce size must be_==(false)
    }

    "be out of bounce for size 1 1" in {
      val size = new Size(1, 1)
      position isOutOfBounce size must be_==(true)
    }

  }

}