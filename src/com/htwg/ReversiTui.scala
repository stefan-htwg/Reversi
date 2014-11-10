package com.htwg

import scala.swing.Reactor

class ReversiTui(controller: Controller) extends Reactor {
  listenTo(controller)

  reactions += {
    case e: BoardChanged => println("Changed")
  }

  def open: Unit =
    {
      var input = ""
      var reversi = new ReversiModel()
      var startWithPlayer=1 //1 OR 2

      reversi.doreset(startWithPlayer);

      print(reversi.board.toSting())

      while (true) {
        input = readLine();

        input match {
          case "q" => {
            println("Game stoped!")
            return
          }
          case "r" => {
            println("Game reset!")
            reversi.doreset(startWithPlayer);
            print(reversi.board.toSting())            
          }
          case "s1" => {
            println("s1")
            startWithPlayer=1
          }
          case "s2" => {
            println("s2")
            startWithPlayer=2
          }
          case _ => {
            println("input!")
            input.toList.filter(c => c != ' ').map(c => c.toString.toInt) match {
              case column :: row :: Nil => {
                println("column/row: " + column + "/" + row)
                reversi.clickat(column, row)
                print(reversi.board.toSting())
              }
              case _ => println("False Input!!!")

            }
          }
        }

      }
    }
}