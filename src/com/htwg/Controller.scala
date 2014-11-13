package com.htwg

import scala.swing.Publisher
import scala.swing.event.Event
import scala.swing.Frame

case class BoardChanged() extends Event

class Controller(var model: ReversiModel) extends Publisher{
  
  def resetGame(col:Int,row:Int){
    model.init(col,row)
  }
  
  def setCell(col: Integer, row: Integer) = 
    {
	  // TODO check bounds
    
	  model.clickat(col, row) 
	  publish(new BoardChanged)
    }
  
  def getValueAt(column : Integer, row : Integer) = model.getCell(column,row).toString

    
  def getCurrentPlayer = model.getPlayer() 
  
  def getModel =  model
  
  def getBlackPlayerScore =  model.getPlayerScore(model.sqblack)
  
  def getWhitePlayerScore =  model.getPlayerScore(model.sqwhite)
  
}