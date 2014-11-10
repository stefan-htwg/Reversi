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
    
	  board.setCell(col, row, new Cell(col, row, board.player)) 
	  
	  publish(new BoardChanged)
    }
  
  def getValueAt(column : Integer, row : Integer) = board.cells(column)(row).value
  
  def getCurrentPlayer = board.player 
}

class Model(){
  
}

class View(controller: Controller) extends Frame{
	listenTo(controller)
	
	reactions += {
	  case e: BoardChanged => println("Changed")
	}
}
/*
class Game12(){
  
  var board = new Board(8, 8)
  var controller = new Controller(board)
  var view = new View(controller)
  
  def main(args: Array[String])
  {
    view.open
  }
}*/