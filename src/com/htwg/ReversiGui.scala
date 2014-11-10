package com.htwg

import scala.swing.Frame

class ReversiGui(controller: Controller) extends Frame{
	listenTo(controller)
	
	reactions += {
	  case e: BoardChanged => println("Changed")
	}
}