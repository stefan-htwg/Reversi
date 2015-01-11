package com.htwg.reversi.controller

import scala.swing.Publisher
import scala.swing.event.Event
import com.htwg.reversi.model.GameEngine
import com.htwg.reversi.model.Position
import com.htwg.reversi.model.Size
import com.htwg.reversi.model.Player;

case class GameStateChanged() extends Event
case class BoardChanged() extends Event

class Controller(var engine: GameEngine) extends Publisher {

  def reset(col: Int, row: Int, startWithPlayer: Integer) {
   engine.reset(new Size(col, row), startWithPlayer)
   publish(new BoardChanged)
  } 
  
  def reset = {
   engine.reset
   publish(new BoardChanged)
  }
  
  def setValueAt(position: Position) {
      engine.doMoveAt(position)
      publish(new GameStateChanged)
    }

  def getValueAt(position: Position) = engine.getCellValue(position)
  
  def getCurrentPlayer = engine getPlayer

  def getPlayer1Score = engine getScoreFor(Player One)

  def getPlayer2Score = engine getScoreFor(Player Two)
  
  def getGameStatus = engine getGameStatus

  override def toString = engine toString
}



