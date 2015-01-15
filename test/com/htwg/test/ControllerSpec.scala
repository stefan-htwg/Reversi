package com.htwg.test

import org.specs2.mutable._
import com.htwg.reversi.model.Position
import com.htwg.reversi.model.Size
import com.htwg.reversi.model.GameEngine
import com.htwg.reversi.model.Player
import com.htwg.reversi.controller.Controller

class ControllerSpec extends SpecificationWithJUnit {
  "A new game " should {
    val model = new GameEngine()
    val controller = new Controller(model)

    "start with player 1" in {
      controller.getCurrentPlayer must be_==(Player.One)
    }
  }

  "After the first move at 3, 5 player one" should {
    val controller = new Controller(new GameEngine())
    val player = controller.getCurrentPlayer
    val nextPlayer = getNextPlayer(controller.getCurrentPlayer)
    var score = controller.getPlayer1Score
    controller.setValueAt(new Position(3, 5))

    "own the field 3, 5" in {
      controller.getValueAt(new Position(3, 5)) must be_==(player)
    }

    "own the field 4, 5" in {
      controller.getValueAt(new Position(4, 5)) must be_==(player)
    }

    "have two more points" in {
      controller.getPlayer1Score must be_==(score + 2)
    }
  }

  "After the first move it " should {
    val controller = new Controller(new GameEngine())
    val player = controller.getCurrentPlayer
    val nextPlayer = getNextPlayer(controller.getCurrentPlayer)
    var score = controller.getPlayer1Score
    controller.setValueAt(new Position(3, 5))

    "be player 2 turn" in {
      controller.getCurrentPlayer must be_==(nextPlayer)
    }
  }

  "If player on tries to make move at 1, 1 it" should {
    val controller = new Controller(new GameEngine())
    val player = controller.getCurrentPlayer
    var score = controller.getPlayer1Score
    controller.setValueAt(new Position(1, 1))

    "be still player 1 turn" in {
      controller.getCurrentPlayer must be_==(Player.One)
    }

    "not have been a successfull move" in {
      controller.getValueAt(new Position(1, 1)) must be_==(0)
    }

    "not have changed the scores" in {
      controller.getPlayer1Score must be_==(score)
    }
  }

  private def getNextPlayer(current: Integer) = if (current == Player.One) Player.Two else Player.One
}