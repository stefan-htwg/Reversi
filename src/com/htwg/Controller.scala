package com.htwg

import scala.swing.Publisher
import scala.swing.event.Event
import scala.swing.Frame

case class BoardChanged() extends Event

class Controller(var board: Board) extends Publisher{
  
  var reversi = new Reversi()
  reversi.doreset(1);
  
  def setCell(col: Integer, row: Integer) = 
    {
	  // TODO check bounds
    
	  reversi.clickat(col, row) 
	  
	  publish(new BoardChanged)
    }
  
  def getValueAt(column : Integer, row : Integer) = reversi.board.cells(column)(row).value
  
  def getCurrentPlayer = reversi.board.player 
}