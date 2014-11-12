package com.htwg

class ReversiModel(val max_cols: Integer, val max_rows: Integer) {
  def this() {
    this(8, 8)
  }

  var board = new Board(max_cols, max_rows)

  var whoseturn = 0
  var sqblack = 1
  var sqwhite = 2
  var sqblank = 0
  var debugMode = false
  var compwhite = false;
  var compblack = false;

  def doreset(firstmove: Int) {
    for (column <- 0 until max_cols; row <- 0 until max_rows) {
      board.setCell(new Cell(column, row))
    }

    // TODO default values should not be set here
    // think about moving it
    board.cells(3)(3).set(1);
    board.cells(4)(4).set(1);
    board.cells(3)(4).set(2);
    board.cells(4)(3).set(2);

    if (firstmove == sqblack)
      whoseturn = sqblack;
    else
      whoseturn = sqwhite;

    nextmove()
  }

  def getscore() {
    val wtscore = calculatescore(sqwhite);
    val bkscore = calculatescore(sqblack);
    showscore(wtscore, bkscore);
  }

  def calculatescore(icol: Int): Int = {
    var sum = 0;
    for (column <- 0 until max_cols; row <- 0 until max_rows) {
      if (board.cells(column)(row).value == icol) {
        sum += 1;
      }
    }
    sum
  }

  def showscore(wtscore: Int, bkscore: Int) {
    println("score white:" + wtscore + " black:" + bkscore)
  }

  def cancapture(x: Int, y: Int, n: Int): Boolean =
    {

      for (a <- -1 to 1; b <- -1 to 1) {
        if (!(a == 0 && b == 0)) {
          if (cancapturedir(x, y, a, b, n))
            return true;
        }
      }
      return false;

    }

  def cancapturedir(x: Int, y: Int, xoff: Int, yoff: Int, n: Int): Boolean =
    {
      var thiscolor = n;
      var thatcolor = 0;
      val result = false;

      if (thiscolor == 1) {
        thatcolor = 2
      }
      if (thiscolor == 2) { thatcolor = 1 }

      if (x + xoff + xoff < 1 ||
        x + xoff + xoff > max_cols ||
        y + yoff + yoff < 1 ||
        y + yoff + yoff > max_rows ) {
        return false;

      }

      if (board.cells(x + xoff - 1)(y + yoff - 1).value == sqblank) {
        return false;
      }

      //one square of opposite colour before a square of the same colour.
      if (board.cells(x + xoff - 1)(y + yoff - 1).value == thatcolor &&
        (
          board.cells(x + xoff + xoff - 1)(y + yoff + yoff - 1).value == thiscolor || cancapturedir(x + xoff, y + yoff, xoff, yoff, thiscolor))) {
        return true;
      }
      return false;
    }

  def log(str: String) {
    if (debugMode) println(str)
  }

  def isvalidmove(columns: Int, rows: Int, n: Int): Boolean = {

    if (columns < 1 || columns > max_cols  || rows < 1 || rows > max_rows ) {
      log("out of bounce");
      return false;
    }
    if (board.cells(columns - 1)(rows - 1).value != sqblank) {
      log("not empty");
      return false;
    }

    if (cancapture(columns, rows, n) != true) {
      log("nothing is captured");
      return false;
    }

    log("isvalidmove: x/y=player" + columns + "/" + rows + "=" + n)

    return true;
  }

  def setsquare(x: Int, y: Int, n: Int) {
    board.cells(x - 1)(y - 1).set(n)
  }

  def validmovesexist(n: Int): Boolean =
    {
      for (i <- 1 to max_cols ; j <- 1 to max_rows ) {
        if (isvalidmove(i, j, n)) {
          return true;
        }
      }
      return false;
    }

  def gameover() {
    var wtscore = calculatescore(sqwhite);
    var bkscore = calculatescore(sqblack);
    var winner = ""

    if (wtscore > bkscore) {
      winner = "Winner is White";
    } else if (bkscore > wtscore) {
      winner = "Winner is Black";
    } else if (wtscore == bkscore) {
      winner = "Draw";
    }

    println("Game over!\n\nScore is:\nwhite " + calculatescore(sqwhite) + "\nblack " + calculatescore(sqblack) + "\n" + winner);
  }

  def nextmove() {
    getscore();

    if (whoseturn == sqwhite && validmovesexist(sqblack)) {
      whoseturn = sqblack;
      log("validmovesexist sqblack=1!")
    } else {
      if (whoseturn == sqblack && validmovesexist(sqwhite)) {
        whoseturn = sqwhite;
        log("validmovesexist sqwhite=2!")
      }
    }

    if (!(validmovesexist(sqwhite) || validmovesexist(sqblack))) {
      gameover();
    } else {

      if (iscomputersmove(whoseturn) == true) {
        println("computer move: ")

        docomputersmove(whoseturn);
      } else {
        println("nextmove: " + whoseturn)
      }
    }

  }

  def scoreinbetweensdir(x: Int, y: Int, xoff: Int, yoff: Int, n: Int): Int = {
    var thiscolor = n;
    var thatcolor = 0;
    var result = 0;
    if (thiscolor == 1) { thatcolor = 2; }
    if (thiscolor == 2) { thatcolor = 1; }

    if (board.cells(x + xoff - 1)(y + yoff - 1).value == thatcolor) {
      result += 1;
      result += scoreinbetweensdir(x + xoff, y + yoff, xoff, yoff, thiscolor);
    }
    return result;
  }

  def getvalue(columns: Int, rows: Int, icol: Int): Int = {
    var score = 0;
    if (isvalidmove(columns, rows, icol)) {
      score = 1;

      for (x <- -1 to 1; y <- -1 to 1) {
        if (!(x == 0 && y == 0)) {
          if (cancapturedir(columns, rows, x, y, icol)) {
            score = score + scoreinbetweensdir(columns, rows, x, y, icol);
          }
        }
      }
    }
    return score
  }

  def doinbetweens(x: Int, y: Int, n: Int) {
    for (i <- -1 to 1; j <- -1 to 1) {
      if (!(i == 0 && j == 0)) {
        if (cancapturedir(x, y, i, j, n)) {
          doinbetweensdir(x, y, i, j, n);
        }
      }
    }
  }

  def doinbetweensdir(x: Int, y: Int, xoff: Int, yoff: Int, n: Int) {
    var thiscolor = n;
    var thatcolor = 0;

    if (thiscolor == 1) { thatcolor = 2; }
    if (thiscolor == 2) { thatcolor = 1; }
    if (board.cells(x + xoff - 1)(y + yoff - 1).value == thatcolor) {
      setsquare(x + xoff, y + yoff, thiscolor);
      doinbetweensdir(x + xoff, y + yoff, xoff, yoff, thiscolor);
    }

  }

  def domove(x: Int, y: Int, n: Int) {

    if (isvalidmove(x, y, n)) {
      setsquare(x, y, whoseturn);

      doinbetweens(x, y, n);

      nextmove();
    } else {
      println("not a valid move");
    }
  }

  def iscomputersmove(test: Int): Boolean = {
    //if whoseturn is computer's move return true

    if (compwhite == true && test == sqwhite) { return true; }
    if (compblack == true && test == sqblack) { return true; }

    return false;
  }

  def docomputersmove(icol: Int) {
    var highscore = 0.0
    var lowscore = 0.0
    var lowx = 0
    var lowy = 0

    var highx = 0
    var highy = 0

    for (columns <- 1 to max_cols ; rows <- 1 to max_rows ) {
      var currscore = getvalue(columns, rows, icol);

      if (currscore > highscore) {
        highx = columns;
        highy = rows;
        highscore = currscore;
      }

      if (currscore > 0 && (lowscore == 0.0 || currscore < highscore)) {
        lowx = columns;
        lowy = rows;
        lowscore = currscore;
      }

    }

    println("cp move:" + highx + "/" + highy);
    domove(highx, highy, icol)
  }

  //register click on the board.
  def clickat(x: Int, y: Int) {
    domove(x, y, whoseturn);
  }

  def getPlayer() = whoseturn

}