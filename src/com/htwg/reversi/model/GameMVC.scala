package com.htwg.reversi.model

import com.htwg.reversi.controller.Controller
import com.htwg.reversi.ui.gui.ReversiGui

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