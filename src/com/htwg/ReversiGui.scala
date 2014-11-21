package com.htwg

import scala.swing.Action
import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.FlowPanel
import scala.swing.GridBagPanel
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.SimpleSwingApplication

//TODO show game state running / finished

class ReversiGui(controller: Controller) extends SimpleSwingApplication {

  val width = 8
  val height = 8

  listenTo(controller)

  var buttons: List[Tuple2[Position, Button]] = List()
  var currentPlayerLabel = new Label(controller.getCurrentPlayer.toString)
  var player1Score = new Label(controller.getPlayer1Score.toString)
  var player2Score = new Label(controller.getPlayer2Score.toString)
  var gameStatus = new Label()

  reactions += {
    case e: BoardChanged => updateUI
  }

  def top = new MainFrame {
    title = "Reversi"
    contents =
      new BorderPanel {
        add(new FlowPanel {
          contents += new Label("Current player is: ")
          contents += currentPlayerLabel
          contents += new Label("Player 1 score: ")
          contents += player1Score
          contents += new Label("Player 2 score: ")
          contents += player2Score
        }, BorderPanel.Position.North)
        add(
          new GridBagPanel {
            val gbc = new Constraints()

            for (y <- 1 to width) {
              for (x <- 1 to height) {
                gbc.gridx = x
                gbc.gridy = y
                var button = new Button(Action(controller.getValueAt(x, y).toString) {
                  println("Try setting cell at: X: " + x + " Y: " + y)
                  controller.setCell(x, y)
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
    player1Score.text = controller.getPlayer1Score.toString
    player2Score.text = controller.getPlayer2Score.toString
    currentPlayerLabel.repaint
  }

  class Position(val x: Integer, val y: Integer)
}