package com.htwg.reversi

import com.htwg.reversi.controller.Controller
import com.htwg.reversi.model.GameEngine

import com.htwg.reversi.ui.gui.ReversiGui
import com.htwg.reversi.ui.tui.ReversiTui

object Reversi {
  
  var engine = new GameEngine();
  var controller = new Controller(engine)
  
  var view = new ReversiGui(controller)
  //var view = new ReversiTui(controller)
  
  def main(args: Array[String])
  {
    view.run(args)
  }
}