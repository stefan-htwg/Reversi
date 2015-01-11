package com.htwg.reversi.ui.gui

import java.awt.Color

import scala.swing.Action
import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.Dialog
import scala.swing.FlowPanel
import scala.swing.GridBagPanel
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.SimpleSwingApplication

import com.htwg.reversi.controller.Controller
import com.htwg.reversi.controller.GameStateChanged
import com.htwg.reversi.model.GameStatus
import com.htwg.reversi.model.Player
import com.htwg.reversi.model.Position
import com.htwg.reversi.ui.ReversiUi

class ReversiGui(controller: Controller) extends SimpleSwingApplication with ReversiUi {

  val width = 8
  val height = 8

  listenTo(controller)

  var buttons: List[Tuple2[Position, Button]] = List()
  var currentPlayerLabel = new Label(controller.getCurrentPlayer.toString)
  var player1Score = new Label(controller.getPlayer1Score.toString)
  var player2Score = new Label(controller.getPlayer2Score.toString)
  var gameStatus = new Label()

  def run(args: Array[String]) {
    startup(args)
  }

  reactions += {
    case e: GameStateChanged => updateUI
  }

  def top = new MainFrame {
    title = "Reversi"
    contents =
      new BorderPanel {
        add(createHeaderArea, BorderPanel.Position.North)
        add(createWestArea, BorderPanel.Position.West)
        add(createEastArea, BorderPanel.Position.East)
        add(createPlayingField, BorderPanel.Position.Center)
      }
    updateUI
  }

  def updateUI = {

    if (controller.getGameStatus == GameStatus.GameOver) {
      var result = showGameOverDialog
      if (result == Dialog.Result.No)
        sys.exit(0)

      controller reset;
    }

    buttons.foreach(item =>
      {
        var column = item._1.column
        var row = item._1.row
        var cellValue = controller.getValueAt(new Position(column, row))
        item._2.foreground = getPlayerColor(cellValue)
        item._2.text = if(cellValue == 0) "" else cellValue.toString
        item._2.repaint
      })
    currentPlayerLabel.text = controller.getCurrentPlayer.toString
    player1Score.text = controller.getPlayer1Score.toString
    player2Score.text = controller.getPlayer2Score.toString
    currentPlayerLabel.repaint
  }

  def createHeaderArea(): FlowPanel = {
    return new FlowPanel {
      contents += new Label("Current player is: ")
      contents += currentPlayerLabel
    }
  }

  def showGameOverDialog(): scala.swing.Dialog.Result.Value = {
    var playerOneScore = controller.getPlayer1Score
    var playerTwoScore = controller.getPlayer2Score
    var winner = if (playerOneScore > playerTwoScore) "Player 1" else "Player 2"
    var title = "Congratulations to " + winner
    var text = "You won with: " + playerOneScore + " Points. Do you want to restart the game?"

    return Dialog.showConfirmation(top.contents.head,
      text,
      optionType = Dialog.Options.YesNo,
      title = title)
  }

  def createPlayingField(): GridBagPanel = {
    return new GridBagPanel {
      val gbc = new Constraints()

      for (y <- 1 to width) {
        for (x <- 1 to height) {
          gbc.gridx = x
          gbc.gridy = y
          var button = new Button(Action(controller.getValueAt(new Position(x, y)).toString) {
            println("Try setting cell at: X: " + x + " Y: " + y)
            controller.setValueAt(new Position(x, y))
          })
          buttons = Tuple2[Position, Button](new Position(x, y), button) :: buttons
          add(button, gbc)
        }
      }
    }
  }

  def createWestArea: GridBagPanel = {
    return new GridBagPanel {
      val gbc = new Constraints()

      gbc.gridx = 0
      gbc.gridy = 0
      add(new Label("Player 1: "), gbc)

      gbc.gridx = 0
      gbc.gridy = 1
      add(player1Score, gbc)
    }
  }

  def createEastArea(): GridBagPanel = {
    return new GridBagPanel {
      val gbc = new Constraints()

      gbc.gridx = 0
      gbc.gridy = 0
      add(new Label("Player 2: "), gbc)

      gbc.gridx = 0
      gbc.gridy = 1
      add(player2Score, gbc)
    }
  }

  def getPlayerColor(cellValue: Integer) = if (cellValue == Player.One) Color.red else if (cellValue == Player.Two) Color.blue else Color.black
}