package com.htwg

import scala.swing.Publisher
import scala.swing.event.Event
import scala.swing.Frame

case class BoardChanged() extends Event

class Controller(var board: Board) extends Publisher{
  
  var currentPlayer = 1; 
  
  def setCell(col: Integer, row: Integer) = 
    {
	  // TODO check bounds
    
	  board.setCell(new Cell(col, row, board.player)) 
	  
	  publish(new BoardChanged)
    }
  
  def getValueAt(column : Integer, row : Integer) = board.cells(column)(row).value
  
  def getCurrentPlayer = board.player 
}