package com.htwg.reversi.ui.gui

import scala.swing._
import scala.swing.event._
import com.htwg.reversi.controller.Controller
import com.htwg.reversi.model.Player
import com.htwg.reversi.model.Position
import com.htwg.reversi.model.Logger

class ButtonExt(position: Position, controller: Controller) extends FlowPanel {

  val playerOneColor = java.awt.Color.red
  val playerTwoColor = java.awt.Color.blue
  val defaultColor = background

  init

  reactions += {
    case ButtonClicked(source) =>
      println("Try setting cell at: Column: " + position.column + " Row: " + position.row)
      controller.setValueAt(position)
  }

  def redraw: Unit = {
    contents.clear

    if (controller.getValueAt(position) != 0) {
      background = getPlayerColor(controller getValueAt (position))
      repaint
      return
    }

    val cellButton = new Button {
      text = "set"
      preferredSize = new Dimension(40, 40)
      background = getPlayerColor(controller getValueAt (position))
    }
    background = defaultColor
    contents += cellButton
    listenTo(cellButton)
  }

  def init = {
    preferredSize = new Dimension(50, 50)
    redraw
  }

  def getPlayerColor(cellValue: Integer) = if (cellValue == Player.One) playerOneColor else if (cellValue == Player.Two) playerTwoColor else defaultColor

}