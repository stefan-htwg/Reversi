package com.htwg.test

import org.specs2.mutable._
import com.htwg.reversi.model.Cell

class CellSpec extends SpecificationWithJUnit {
  "A new Cell initialized with 1" should {
    
    val cell = new Cell(1)

    "have the same value" in {
      cell.value must be_==(1)
    }
        
    "return isEmpty false" in {
      cell.isEmpty must be_==(false)
    }
     
    "return a string with 1 for ToString" in {
      cell.toString must be_==("1")
    }
  }

  "A new Cell initialized with 0" should {
        
    val cell = new Cell(0)

    "have the same value" in {
      cell.value must be_==(0)
    }
    
    "return isEmpty true" in {
      cell.isEmpty must be_==(true)
    }
    
    "return an emtpy string for ToString" in {
      cell.toString must be_==(" ")
    }
  }
}