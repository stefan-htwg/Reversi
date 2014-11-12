package com.htwg.tests

import org.junit.Test
import org.junit.Assert
import com.htwg.ReversiModel

class ReversiModelTests {
  
  @Test def testDefaultValues={
    var model = new ReversiModel()

    Assert.assertSame(model.getPlayer, 0)
  }
}