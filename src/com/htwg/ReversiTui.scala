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
      var size =10;
      
      
      controller.getModel.init(size,size)
      controller.getModel.doReset(startWithPlayer);
      
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
            controller.getModel.init(size,size)
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
          case "size1" => {
            println("change size 4x4")
            size=4
          }
          case "size2" => {
            println("change size 6x6")
            size=6
          }
          case "size3" => {
            println("change size 8x8")
            size=6
          }
          case _ => {
            println("input!")
            input.toList.filter(c => c != ' ').map(c => c.toString.toInt) match {
              case column :: row :: Nil => {
                println("column/row: " + column + "/" + row)
                controller.getModel.clickAt(column, row)
                print(controller.getModel.board.toString())
              }
              case _ => println("False Input!!!")

            }
          }
        }
      }
    }
}