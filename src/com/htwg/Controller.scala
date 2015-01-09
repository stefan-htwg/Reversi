package com.htwg

import scala.swing.Publisher
import scala.swing.event.Event
import scala.swing.Frame

case class GameStateChanged() extends Event
case class BoardChanged() extends Event

class Controller(var model: GameEngine) extends Publisher {

  def reset(col: Int, row: Int, startWithPlayer: Integer) {
   model.reset(new Size(col, row), startWithPlayer)
   publish(new BoardChanged)
  } 
  
  def reset = {
   model.reset
   publish(new BoardChanged)
  }
  
  def setValueAt(position: Position) {
      model.doMoveAt(position)
      publish(new GameStateChanged)
    }

  def getValueAt(position: Position) = model.getCellValue(position)
  
  def getCurrentPlayer = model getPlayer

  def getPlayer1Score = model getScoreFor(Player One)

  def getPlayer2Score = model getScoreFor(Player Two)
  
  def getGameStatus = model getGameStatus

  override def toString = model toString
}