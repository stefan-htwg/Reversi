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
  var compwhite = true;
  var compblack = false;

  doreset(sqwhite);
 
  def setAt(column: Int, row: Int, value: Int) {
    getCell(column,row).set(value)
  }
  
  def getAt(column: Int, row: Int) : Int ={
    getCell(column,row).value 
  }
  
  def getCell(column: Int, row: Int) : Cell ={
    board.cells(column-1)(row-1) 
  }
  
  def setCell(cell :Cell) {
    board.setCell(cell); 
  }
  
  def doreset(firstmove: Int) {
    for (column <- 0 until max_cols; row <- 0 until max_rows) {
      setCell(new Cell(column, row,sqblank))
    }

    setAt(4,4,sqblack)
    setAt(5,5,sqblack)
    setAt(4,5,sqwhite)
    setAt(5,4,sqwhite)

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
    for (column <- 1 to max_cols; row <- 1 to max_rows) {
      if (getAt(column,row) == icol) {
        sum += 1;
      }
    }
    sum
  }

  //TODO move to gui
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

      if (getAt(x + xoff,y + yoff) == sqblank) {
        return false;
      }

      //one square of opposite colour before a square of the same colour.
      if (getAt(x + xoff,y + yoff) == thatcolor &&
        (
          getAt(x + xoff + xoff,y + yoff + yoff) == thiscolor || cancapturedir(x + xoff, y + yoff, xoff, yoff, thiscolor))) {
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
    if (getAt(columns,rows) != sqblank) {
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

     //TODO move to gui
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
         //TODO move to gui
    	 println("computer move: ")

        docomputersmove(whoseturn);
      } else {
         //TODO move to gui
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

    if (getAt(x + xoff,y + yoff) == thatcolor) {
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
    if (getAt(x + xoff,y + yoff) == thatcolor) {
      setAt(x + xoff, y + yoff, thiscolor);
      doinbetweensdir(x + xoff, y + yoff, xoff, yoff, thiscolor);
    }

  }

  def domove(x: Int, y: Int, n: Int) {

    if (isvalidmove(x, y, n)) {
      setAt(x, y, whoseturn);

      doinbetweens(x, y, n);

      nextmove();
    } else {
       //TODO move to gui
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

     //TODO move to gui
    println("cp move:" + highx + "/" + highy);
    domove(highx, highy, icol)
  }

  //register click on the board.
  def clickat(x: Int, y: Int) {
    domove(x, y, whoseturn);
  }

  def getPlayer() = whoseturn

}