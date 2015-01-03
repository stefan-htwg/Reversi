package com.htwg

import scala.swing.Action
import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.FlowPanel
import scala.swing.GridBagPanel
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.SimpleSwingApplication
import java.awt.{ Color }
import javax.swing.JFrame

//TODO show game state running / finished

class ReversiGui(controller: Controller) extends SimpleSwingApplication {

  val width = 8
  val height = 8

  listenTo(controller)
  
  var buttons: List[Tuple2[Position, Button]] = List()
  var currentPlayerLabel = new Label(controller.getCurrentPlayer.toString)
  var player1Score = new Label(controller.getPlayer1Score.toString)
  var player2Score = new Label(controller.getPlayer2Score.toString)
  var gameStatusLabel = new Label(controller.getGameStatus.toString)
  var gameStatus = new Label()

  reactions += {
    case e: GameStateChanged => updateUI
  }

  def top = new MainFrame {
    title = "Reversi"
    contents =
      new BorderPanel {
        add(createHeaderArea, BorderPanel.Position.North)
        add(createScoreArea, BorderPanel.Position.East)
        add(createPlayingField, BorderPanel.Position.Center)
      }
    updateUI
  }

  def updateUI = {
    buttons.foreach(item =>
      {
        var cellValue = controller.getValueAt(item._1.x, item._1.y)
        item._2.foreground = getPlayerColor(cellValue)
        item._2.text = controller.getValueAt(item._1.x, item._1.y).toString
        item._2.repaint
      })
    currentPlayerLabel.text = controller.getCurrentPlayer.toString
    player1Score.text = controller.getPlayer1Score.toString
    player2Score.text = controller.getPlayer2Score.toString
    gameStatusLabel.text = controller.getGameStatus.toString
    currentPlayerLabel.repaint
  }

  def createHeaderArea(): FlowPanel = {
    return new FlowPanel {
      contents += new Label("The game is: ")
      contents += gameStatusLabel
      contents += new Label("Current player is: ")
      contents += currentPlayerLabel
      contents += new Button(Action("Game Over") {
        var newFrame = new JFrame("Game Over") {
          contents += new Label("The Winner is: Player 1")
        }
        newFrame.getContentPane().add(new java.awt.Label("The game is: "), java.awt.BorderLayout.CENTER);
        newFrame.pack();
        newFrame.setVisible(true);
      })
    }
  }

  def createPlayingField(): GridBagPanel = {
    return new GridBagPanel {
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
    }
  }

  def createScoreArea(): GridBagPanel = {
    return new GridBagPanel {
      val gbc = new Constraints()

      gbc.gridx = 0
      gbc.gridy = 0
      add(new Label("Current score: "), gbc)

      gbc.gridx = 0
      gbc.gridy = 1
      add(new Label("Player 1: "), gbc)

      gbc.gridx = 0
      gbc.gridy = 2
      add(player1Score, gbc)

      gbc.gridx = 0
      gbc.gridy = 3
      add(new Label("Player 2: "), gbc)

      gbc.gridx = 0
      gbc.gridy = 4
      add(player2Score, gbc)

    }
  }

  def getPlayerColor(cellValue: Integer) = if (cellValue == Player.Player1) Color.red else if (cellValue == Player.Player2) Color.blue else Color.black

  class Position(val x: Integer, val y: Integer)
}