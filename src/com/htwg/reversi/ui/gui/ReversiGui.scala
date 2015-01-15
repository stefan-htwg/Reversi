package com.htwg.reversi.ui.gui

import java.awt.Color
import scala.swing.Action
import scala.swing.BorderPanel
import scala.swing.Dialog
import scala.swing.FlowPanel
import scala.swing.GridBagPanel
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.Menu
import scala.swing.MenuBar
import scala.swing.MenuItem
import scala.swing.SimpleSwingApplication
import scala.swing.event.Key
import com.htwg.reversi.controller.Controller
import com.htwg.reversi.controller.GameStateChanged
import com.htwg.reversi.model.GameStatus
import com.htwg.reversi.model.Player
import com.htwg.reversi.model.Position
import com.htwg.reversi.ui.ReversiUi

class ReversiGui(controller: Controller) extends SimpleSwingApplication with ReversiUi {
  listenTo(controller)
  type dim = java.awt.Dimension

  var buttonsExt: List[Tuple2[Position, ButtonExt]] = List()

  val currentPlayerLabel = new Label(controller.getCurrentPlayer.toString)
  val player1Score = new Label(controller.getPlayer1Score.toString)
  val player2Score = new Label(controller.getPlayer2Score.toString)
  val gameStatus = new Label()

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

    menuBar = new MenuBar {
      contents += new Menu("Options") {
        mnemonic = Key.F
        contents += new MenuItem(Action("New") {
          controller.reset
          updateUI
        })
        contents += new MenuItem(Action("Close") { System.exit(0) })
      }
    }
    size = new dim(600, 600)
    updateUI
  }

  def updateUI = {

    if (controller.getGameStatus == GameStatus.GameOver) {
      var result = showGameOverDialog
      if (result == Dialog.Result.No)
        sys.exit(0)

      controller reset;
    }

    buttonsExt.foreach(item => {
      item._2.redraw
    })
    currentPlayerLabel.text = getPlayerColor(controller.getCurrentPlayer)
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
      for (y <- 1 to controller.getGameSize.y; x <- 1 to controller.getGameSize.x) {
        gbc.gridx = x
        gbc.gridy = y

        var buttonExt = new ButtonExt(new Position(x, y), controller)
        add(buttonExt, gbc);
        buttonsExt = Tuple2[Position, ButtonExt](new Position(x, y), buttonExt) :: buttonsExt
      }
    }
  }

  def createWestArea: GridBagPanel = {
    return new GridBagPanel {
      val gbc = new Constraints()

      gbc.gridx = 0
      gbc.gridy = 0
      add(new Label("Player Red: "), gbc)

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
      add(new Label("Player Blue: "), gbc)

      gbc.gridx = 0
      gbc.gridy = 1
      add(player2Score, gbc)
    }
  }

  def getPlayerColor(cellValue: Integer) = if (cellValue == Player.One) "Red" else if (cellValue == Player.Two) "Blue" else ""
}