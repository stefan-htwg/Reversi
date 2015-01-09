package com.htwg.test

import org.specs2.mutable._
import com.htwg.Position
import com.htwg.Size
import com.htwg.GameEngine
import com.htwg.Player

class GameEngineSpec extends SpecificationWithJUnit {
  "A new game " should {
    val model = new GameEngine()

    "start with player 1" in {
      model.getPlayer must be_==(Player.One)
    }
  }

  "After the first move at 3, 5 player one" should {
    val model = new GameEngine()
    val player = model.getPlayer
    val nextPlayer = getNextPlayer(model.getPlayer)
    var score = model.getScoreFor(player)
    model.doMoveAt(new Position(3, 5))

    "own the field 3, 5" in {
      model.getCellValue(new Position(3, 5)) must be_==(player)
    }

    "own the field 4, 5" in {
      model.getCellValue(new Position(4, 5)) must be_==(player)
    }

    "have two more points" in {
      model.getScoreFor(player) must be_==(score + 2)
    }
  }

  "After the first move it " should {
    val model = new GameEngine()
    val player = model.getPlayer
    val nextPlayer = getNextPlayer(model.getPlayer)
    model.doMoveAt(new Position(3, 5))

    "be player 2 turn" in {
      model.getPlayer must be_==(nextPlayer)
    }
  }

  "If player on tries to make move at 1, 1 it" should {
    val model = new GameEngine()
    val player = model.getPlayer
    val score = model.getScoreFor(player)
    model.doMoveAt(new Position(1, 1))

    "be still player 1 turn" in {
      model.getPlayer must be_==(Player.One)
    }

    "not have been a successfull move" in {
      model.getCellValue(new Position(1, 1)) must be_==(0)
    }

    "not have changed the scores" in {
      model.getScoreFor(player) must be_==(score)
    }
  }

  private def getNextPlayer(current: Integer) = if (current == Player.One) Player.Two else Player.One
}