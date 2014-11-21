package com.htwg

import scala.swing.Publisher
import scala.swing.event.Event
import scala.swing.Frame

case class BoardChanged() extends Event

class Controller(var model: ReversiModel) extends Publisher {

  def reset(col: Int, row: Int, startWithPlayer: Integer) = model.reset(col, row, startWithPlayer)
  def reset(startWithPlayer: Integer) = model.reset(startWithPlayer)

  def setCell(col: Integer, row: Integer) =
    {
      // TODO check bounds

      model.doMoveAt(col, row)
      publish(new BoardChanged)
    }

  def getValueAt(column: Integer, row: Integer) = model.getCell(column, row).toString

  def getCurrentPlayer = model.getPlayer

  def getModel = model

  def getPlayer1Score = model.getPlayerScore(Player.Player1)

  def getPlayer2Score = model.getPlayerScore(Player.Player2)

  override def toString = model.toString

}