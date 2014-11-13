package com.htwg.tests

import org.junit.Test
import org.junit.Assert
import com.htwg.ReversiModel

class ReversiModelTests {
  
  @Test def testDefaultValues={
    
    var model = new ReversiModel()

    Assert.assertSame(model.getPlayer, 1)
  }
  
  
    @Test def testFirstMove={
    
    val model = new ReversiModel()
    val player = model.getPlayer
    
    model.clickat(3, 5);
    
    Assert.assertSame(model.getAt(3,5), player)
  }
  
}