package com.htwg.test

import org.specs2.mutable._
import com.htwg.reversi.model.Board
import com.htwg.reversi.model.Size
import com.htwg.reversi.model.Position

class BoardSpec extends SpecificationWithJUnit {
  "A new Board with size 8, 8" should {
    val board = new Board(new Size(8, 8))

    "have an empty cell for position 1, 1" in {
      board.getCell(new Position(1, 1)).isEmpty must be_==(true)
    }
  }
}