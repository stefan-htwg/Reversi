package com.htwg.tests

import org.junit.Assert
import org.junit.Before
import org.junit.Test

import com.htwg.ReversiModel

class ReversiModelTests {

  val model = new ReversiModel()
  val noPlayer = 0
  var startPlayer = model.sqblack;

  @Before def initialize() {
    model.doReset(startPlayer)
  }

  @Test def testDefaultValues = {
    Assert.assertSame(startPlayer, model.getPlayer)
  }

  @Test def testBlackStarter = {
    //model.doReset(model.sqblack)
    Assert.assertSame(model.sqblack, model.getPlayer)
  }

  @Test def testWhiteStarter = {
    model.doReset(model.sqwhite)
    Assert.assertSame(model.sqwhite, model.getPlayer)
  }

  @Test def testFirstMove = {
    //model.doReset(model.sqblack)

    val player = model.getPlayer
    val nextPlayer = if (model.getPlayer == model.sqblack) model.sqwhite else model.sqblack

    Assert.assertSame(nextPlayer, model.getAt(4, 5))

    model.clickAt(3, 5)

    Assert.assertSame(player, model.getAt(3, 5))

    Assert.assertSame(player, model.getAt(4, 5))
  }

  @Test def testChangePlayer = {
    val player = model.getPlayer

    model.clickAt(3, 5)

    Assert.assertNotSame(player, model.getPlayer)
  }

  @Test def testAddScore = {
    val player = model.getPlayer
    val nextPlayer = if (model.getPlayer == model.sqblack) model.sqwhite else model.sqblack
    var score = model.getPlayerScore(player)
    var nextScore = model.getPlayerScore(nextPlayer)

    model.clickAt(3, 5)

    Assert.assertSame(score + 2, model.getPlayerScore(player))

    Assert.assertSame(nextScore - 1, model.getPlayerScore(nextPlayer))
  }

  @Test def testRemoveScore = {
    val nextPlayer = if (model.getPlayer == model.sqblack) model.sqwhite else model.sqblack
    var nextScore = model.getPlayerScore(nextPlayer)

    model.clickAt(3, 5)

    Assert.assertSame(nextScore - 1, model.getPlayerScore(nextPlayer))
  }

  @Test def testWrongMove = {

    model.clickAt(1, 1)

    Assert.assertSame(model.sqblank , model.getAt(1, 1))
  }

  @Test def testWrongMoveNoChange = {
    val player = model.getPlayer
    val score = model.getPlayerScore(player)

    model.clickAt(1, 1)

    Assert.assertSame(player, model.getPlayer)
    Assert.assertSame(score, model.getPlayerScore(player))
  }
}