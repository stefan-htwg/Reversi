package com.htwg

import scala.swing.Action
import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.FlowPanel
import scala.swing.GridBagPanel
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.SimpleSwingApplication

class ReversiGui(controller: Controller) extends SimpleSwingApplication {

  val width = 8
  val height = 8

  listenTo(controller)

  var buttons: List[Tuple2[Position, Button]] = List()
  var currentPlayerLabel = new Label(controller.getCurrentPlayer.toString)

  reactions += {
    case e: BoardChanged => updateUI
  }

  def top = new MainFrame {
    title = "Reversi"
    contents =
      new BorderPanel {
    	add(new FlowPanel
    	    {
    			contents += new Label("Current player is: ")
    			contents += currentPlayerLabel 
    	    }, BorderPanel.Position.North)
        add(
          new GridBagPanel {
            val gbc = new Constraints()

            for (y <- 0 until width) {
              for (x <- 0 until height) {
                gbc.gridx = x
                gbc.gridy = y
                var button = new Button(Action(controller.getValueAt(x, y)toString) {
                  println("Try setting cell at: X: " + x + " Y: " + y)
                  controller.setCell(x+1, y+1)
                })
                buttons = Tuple2[Position, Button](new Position(x, y), button) :: buttons
                add(button, gbc)
              }
            }
          }, BorderPanel.Position.Center)
      }
  }

  def updateUI = {
    buttons.foreach(item =>
      {
        item._2.text = controller.getValueAt(item._1.x, item._1.y).toString
        item._2.repaint
      })
    currentPlayerLabel.text = controller.getCurrentPlayer.toString
    currentPlayerLabel.repaint
  }

  class Position(val x: Integer, val y: Integer) {

  }
}