package com.htwg.test

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import com.htwg.reversi.model.GameEngine
import com.htwg.reversi.model.Position
import com.htwg.reversi.model.Player;

class GameEngineTests {
  val noPlayer = 0
  var startPlayer = Player.One;

  @Before def initialize() {
    val model = new GameEngine()
    model.reset(startPlayer)
  }

  @Test def testDefaultValues = {
    val model = new GameEngine()
    Assert.assertSame(startPlayer, model.getPlayer)
  }

  @Test def testOneStarter = {
    val model = new GameEngine()
    Assert.assertSame(Player.One, model.getPlayer)
  }

  @Test def testWhiteStarter = {
    val model = new GameEngine()
    model.reset(Player.Two)
    Assert.assertSame(Player.Two, model.getPlayer)
  }

  @Test def testFirstMove = {
    val model = new GameEngine()
    val player = model.getPlayer
    val nextPlayer = getNextPlayer(model.getPlayer)

    Assert.assertSame(nextPlayer, model.getCellValue(new Position(4, 5)))

    model.doMoveAt(new Position(3, 5))

    Assert.assertSame(player, model.getCellValue(new Position(3, 5)))

    Assert.assertSame(player, model.getCellValue(new Position(4, 5)))
  }

  @Test def testChangePlayer = {
    val model = new GameEngine()
    val player = model.getPlayer

    model.doMoveAt(new Position(3, 5))

    Assert.assertNotSame(player, model.getPlayer)
  }

  @Test def testAddScore = {
    val model = new GameEngine()
    val player = model.getPlayer
    val nextPlayer = getNextPlayer(model.getPlayer)
    var score = model.getScoreFor(player)
    var nextScore = model.getScoreFor(nextPlayer)

    model.doMoveAt(new Position(3, 5))

    Assert.assertSame(score + 2, model.getScoreFor(player))

    Assert.assertSame(nextScore - 1, model.getScoreFor(nextPlayer))
  }

  @Test def testRemoveScore = {
    val model = new GameEngine()
    val nextPlayer = getNextPlayer(model.getPlayer)
    var nextScore = model.getScoreFor(nextPlayer)

    model.doMoveAt(new Position(3, 5))

    Assert.assertSame(nextScore - 1, model.getScoreFor(nextPlayer))
  }

  @Test def testWrongMove = {
    val model = new GameEngine()
    model.doMoveAt(new Position(1, 1))

    Assert.assertSame(Player.One, Player.One)	
    Assert.assertNotSame(Player.One, model.getCellValue(new Position(1, 1)))
  }

  @Test def testWrongMoveNoChange = {
    val model = new GameEngine()
    val player = model.getPlayer
    val score = model.getScoreFor(player)

    model.doMoveAt(new Position(1, 1))

    Assert.assertSame(player, model.getPlayer)
    Assert.assertSame(score, model.getScoreFor(player))
  }
  
  private def getNextPlayer(current: Integer) = if (current == Player.One) Player.Two else Player.One
}