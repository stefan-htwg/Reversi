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
      var max_rows = 6;
      var max_cols = 7;
      var reversi = new Reversi()

      reversi.doreset(1);

      print(reversi.board.toSting())

      while (true) {
        input = readLine();

        input match {
          case "q" => {
            println("Game stoped!")
            return
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