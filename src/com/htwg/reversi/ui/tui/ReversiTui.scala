package com.htwg.reversi.ui.tui

import scala.swing.Reactor
import com.htwg.reversi.controller.GameStateChanged
import com.htwg.reversi.controller.Controller
import com.htwg.reversi.controller.BoardChanged
import com.htwg.reversi.model.Player;
import com.htwg.reversi.model.Position
import com.htwg.reversi.ui.ReversiUi

class ReversiTui(controller: Controller) extends Reactor with ReversiUi{
  listenTo(controller)

  reactions += {
    case e: GameStateChanged => println("Changed")
    case e: BoardChanged => reDraw
  }
  
  def reDraw(){
    print(controller.toString())
  }
  
  def run(args: Array[String]) {
      var input = ""
      var startWithPlayer = controller.getCurrentPlayer
      var size = 8;

      controller.reset(size, size, startWithPlayer);

      println("Current player is: Player " + controller.getCurrentPlayer)
      println(controller.toString)

      while (true) {
        input = readLine

        input match {
          case "q" => {
            println("Game stoped!")
            return
          }
          case "r" => {
            println("Game reset!")
            controller.reset(size,size,startWithPlayer)
          }
          case "black" => {
            println("black starts")
            startWithPlayer = Player.One
          }
          case "white" => {
            println("white starts")
            startWithPlayer = Player.Two
          }
          case "size1" => {
            size=4
            println("change size "+size+"x"+size)
             controller.reset(size,size,startWithPlayer)
          }
          case "size2" => {
            size=6
            println("change size "+size+"x"+size)
             controller.reset(size,size,startWithPlayer)
          }
          case "size3" => {
            size=8
            println("change size "+size+"x"+size)
            controller.reset(size,size,startWithPlayer)
          }
          case _ => {
            println("input!")
            input.toList.filter(c => c != ' ').map(c => c.toString.toInt) match {
              case column :: row :: Nil => {
                println("column/row: " + column + "/" + row)
                controller.setValueAt(new Position(column, row))
                println("Current player is: Player " + controller.getCurrentPlayer)
                print(controller.toString)
              }
              case _ => println("False Input!!!")

            }
          }
        }
      }
    }
}