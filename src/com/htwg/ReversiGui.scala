package com.htwg

import scala.swing.Action
import scala.swing.Button
import scala.swing.GridBagPanel
import scala.swing.MainFrame
import scala.swing.SimpleSwingApplication
import javax.xml.soap.Detail

class ReversiGui(controller: Controller) extends SimpleSwingApplication {

  val width = 8
  val height = 8

  listenTo(controller)

  reactions += {
    case e: BoardChanged => updateUI
  }

  def top = new MainFrame {
    title = "Reversi"
    contents =
      new GridBagPanel {
        val gbc = new Constraints()

        for (y <- 0 until width) {
          for (x <- 0 until height) {
            gbc.gridx = x
            gbc.gridy = y
            var button = new Button(Action(controller.getValueAt(x, y)toString) {
              controller.setCell(x, y)
            })
            add(button, gbc)
          }
        }
      }
  }

  def updateUI = {
    top.contents(0) match {
      case grid: GridBagPanel => grid.contents.foreach(item =>
        {
          item match {
            case button: Button => {
              button.text = "refreshed"
              button.repaint
            }
            case _ => throw new ClassCastException
          }
        })
      case _ => throw new ClassCastException
    }

  }
}