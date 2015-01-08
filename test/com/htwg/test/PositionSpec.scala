package com.htwg.test
 
import org.specs2.mutable._

import com.htwg.Position;

class PositionSpec extends SpecificationWithJUnit {
   "A new Position set to 0 " should {
    val position = new Position(0,0)
    
    "have value 0" in {
      position.row must be_==(0)
    }
   }
}