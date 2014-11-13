package com.htwg.tests

import org.junit.Assert
import org.junit.Before
import org.junit.Test

import com.htwg.ReversiModel

class ReversiModelTests {

  val model = new ReversiModel()
  var startPlayer = 1;

  @Before def initialize() {
  }

  @Test def testDefaultValues = {
    Assert.assertSame(model.getPlayer, startPlayer)
  }

  @Test def testFirstMove = {

    val player = model.getPlayer
    val nextPlayer = if (player == 1) 2 else 1

    Assert.assertSame(model.getAt(4, 5), nextPlayer)

    model.clickat(3, 5);

    Assert.assertSame(model.getAt(3, 5), player)

    Assert.assertSame(model.getAt(4, 5), player)
  }

}