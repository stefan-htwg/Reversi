package com.htwg

object GameMVC {
  
  var engine = new GameEngine();
  var controller = new Controller(engine)
  
  var view = new ReversiGui(controller)
  //var view = new ReversiTui(controller)
  
  def main(args: Array[String])
  {
    view.run(args)
  }
}