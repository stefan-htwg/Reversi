package com.htwg

import scala.swing.Publisher
import scala.swing.event.Event
import scala.swing.Frame

case class BoardChanged() extends Event

class Controller(var model: ReversiModel) extends Publisher{
  
  def setCell(col: Integer, row: Integer) = 
    {
	  // TODO check bounds
    
	  model.clickat(col, row) 
	  publish(new BoardChanged)
    }
  
  def getValueAt(column : Integer, row : Integer) = model.getAt(column,row)
  
  def getCurrentPlayer = model.getPlayer() 
  
  def getModel =  model
}