package com.htwg

object GameMVC {
  
  var model = new ReversiModel();
  var controller = new Controller(model)
  
  
  //var view = new ReversiGui(controller)
  var view = new ReversiTui(controller)
  
  def main(args: Array[String])
  {
    view.startup(args)
  }
}