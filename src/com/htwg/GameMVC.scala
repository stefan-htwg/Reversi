package com.htwg

object GameMVC {
  
  var board = new Board(8, 8)
  var controller = new Controller(board)
  
  
  var view = new ReversiGui(controller)
  //var view = new ReversiTui(controller)
  
  def main(args: Array[String])
  {
    view.startup(args)
  }
}