package com.htwg

object GameMVC {
  
  var board = new Board(8, 8)
  var controller = new Controller(board)
  var view = new View(controller)
  
  def main(args: Array[String])
  {
    view.open
  }
}