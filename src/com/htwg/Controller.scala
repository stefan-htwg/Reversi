package com.htwg

import scala.swing.Publisher
import scala.swing.event.Event
import scala.swing.Frame

case class GameStateChanged() extends Event
case class BoardChanged() extends Event

class Controller(var model: ReversiModel) extends Publisher {

  def reset(col: Int, row: Int, startWithPlayer: Integer) {
   model.reset(col, row, startWithPlayer)
   publish(new BoardChanged) // TODO move to model
  } 
  
  def reset(startWithPlayer: Integer) {
   model.reset(startWithPlayer)
   publish(new BoardChanged) // TODO move to model
  }
  
  def setCell(col: Integer, row: Integer) 
    {
      // TODO check bounds

      model.doMoveAt(col, row)
      publish(new GameStateChanged)
    }

  def getValueAt(column: Integer, row: Integer) = model.getCellValue(column, row)

  def getCurrentPlayer = model.getPlayer

  def getPlayer1Score = model.getPlayerScore(Player.Player1)

  def getPlayer2Score = model.getPlayerScore(Player.Player2)
  
  def getGameStatus = model.getGameStatus

  override def toString = model.toString
}