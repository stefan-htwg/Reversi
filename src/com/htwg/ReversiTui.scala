package com.htwg

import scala.swing.Reactor

class ReversiTui(controller: Controller) extends Reactor {
  listenTo(controller)

  reactions += {
    case e: BoardChanged => println("Changed")
  }

  def startup(args: Array[String]): Unit =
    {
      var input = ""
      var startWithPlayer = 1 //1 OR 2

      println(controller.getModel.board.toString())
      
      while (true) {
        input = readLine();

        input match {
          case "q" => {
            println("Game stoped!")
            return
          }
          case "r" => {
            println("Game reset!")
            controller.getModel.doReset(startWithPlayer);
            print(controller.getModel.board.toString())
          }
          case "s1" => {
            println("s1")
            startWithPlayer = 1
          }
          case "s2" => {
            println("s2")
            startWithPlayer = 2
          }
          case _ => {
            println("input!")
            input.toList.filter(c => c != ' ').map(c => c.toString.toInt) match {
              case column :: row :: Nil => {
                println("column/row: " + column + "/" + row)
                controller.getModel.clickat(column, row)
                print(controller.getModel.board.toString())
              }
              case _ => println("False Input!!!")

            }
          }
        }
      }
    }
}