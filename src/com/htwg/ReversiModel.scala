package com.htwg

class ReversiModel(cols: Integer, rows: Integer) {

  var max_cols =cols
  var max_rows =rows
  
  val board = new Board(max_cols, max_rows)

  def this() {
    this(8, 8)
  }

  
  def init(cols: Integer, rows: Integer) {
    max_cols =cols
    max_rows =rows
    board.init(max_cols, max_rows)
  }

  var whoseturn = 0
  val sqblack = 1
  val sqwhite = 2
  val sqblank = 0
  val debugMode = false
  
  //TODO active comp player by gui
  val compwhite = true;
  val compblack = false;

  doReset(sqwhite);
 
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
  
  def doReset(firstmove: Int) {
    for (column <- 0 until max_cols; row <- 0 until max_rows) {
      setCell(new Cell(column, row,sqblank))
    }

    setAt(max_cols/2,max_rows/2,sqblack)
    setAt((max_cols/2)+1,(max_rows/2)+1,sqblack)
    setAt(max_cols/2,(max_rows/2)+1,sqwhite)
    setAt((max_cols/2)+1,max_rows/2,sqwhite)

    if (firstmove == sqblack)
      whoseturn = sqblack;
    else
      whoseturn = sqwhite;

    nextMove()
  }

  
  def getPlayerScore(player:Int) = {
    calculateScore(player)
  }
  
  def getTotalScore() {
    val wtscore = getPlayerScore(sqwhite);
    val bkscore = getPlayerScore(sqblack);
    showScore(wtscore, bkscore);
  }

  def calculateScore(icol: Int): Int = {
    var sum = 0;
    for (column <- 1 to max_cols; row <- 1 to max_rows) {
      if (getAt(column,row) == icol) {
        sum += 1;
      }
    }
    sum
  }

  //TODO move to gui
  def showScore(wtscore: Int, bkscore: Int) {
    println("score white:" + wtscore + " black:" + bkscore)
  }

  def canCapture(x: Int, y: Int, n: Int): Boolean =
    {

      for (a <- -1 to 1; b <- -1 to 1) {
        if (!(a == 0 && b == 0)) {
          if (canCaptureDir(x, y, a, b, n))
            return true;
        }
      }
      return false;

    }

  def canCaptureDir(x: Int, y: Int, xoff: Int, yoff: Int, n: Int): Boolean =
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
          getAt(x + xoff + xoff,y + yoff + yoff) == thiscolor || canCaptureDir(x + xoff, y + yoff, xoff, yoff, thiscolor))) {
        return true;
      }
      return false;
    }

  def log(str: String) {
    if (debugMode) println(str)
  }

  def isValidMove(columns: Int, rows: Int, n: Int): Boolean = {

    if (columns < 1 || columns > max_cols  || rows < 1 || rows > max_rows ) {
      log("out of bounce");
      return false;
    }
    if (getAt(columns,rows) != sqblank) {
      log("not empty");
      return false;
    }

    if (canCapture(columns, rows, n) != true) {
      log("nothing is captured");
      return false;
    }

    log("isValidMove: x/y=player" + columns + "/" + rows + "=" + n)

    return true;
  }

  

  def validMovesExist(n: Int): Boolean =
    {
      for (i <- 1 to max_cols ; j <- 1 to max_rows ) {
        if (isValidMove(i, j, n)) {
          return true;
        }
      }
      return false;
    }

  def gameover() {
    var wtscore = calculateScore(sqwhite);
    var bkscore = calculateScore(sqblack);
    var winner = ""

    if (wtscore > bkscore) {
      winner = "Winner is White";
    } else if (bkscore > wtscore) {
      winner = "Winner is Black";
    } else if (wtscore == bkscore) {
      winner = "Draw";
    }

     //TODO move to gui
    println("Game over!\n\nScore is:\nwhite " + calculateScore(sqwhite) + "\nblack " + calculateScore(sqblack) + "\n" + winner);
  }

  def nextMove() {
    getTotalScore();

    if (whoseturn == sqwhite && validMovesExist(sqblack)) {
      whoseturn = sqblack;
      log("validmovesexist sqblack=1!")
    } else {
      if (whoseturn == sqblack && validMovesExist(sqwhite)) {
        whoseturn = sqwhite;
        log("validmovesexist sqwhite=2!")
      }
    }

    if (!(validMovesExist(sqwhite) || validMovesExist(sqblack))) {
      gameover();
    } else {

      if (isComputersMove(getPlayer) == true) {
         //TODO move to gui
    	 println("computer move: ")

        doComputersMove(getPlayer);
      } else {
         //TODO move to gui
    	 println("nextMove: " + getPlayer)
      }
    }

  }

  def scoreInBetweensDir(x: Int, y: Int, xoff: Int, yoff: Int, n: Int): Int = {
    var thiscolor = n;
    var thatcolor = 0;
    var result = 0;
    if (thiscolor == 1) { thatcolor = 2; }
    if (thiscolor == 2) { thatcolor = 1; }

    if (getAt(x + xoff,y + yoff) == thatcolor) {
      result += 1;
      result += scoreInBetweensDir(x + xoff, y + yoff, xoff, yoff, thiscolor);
    }
    return result;
  }

  def getValue(columns: Int, rows: Int, icol: Int): Int = {
    var score = 0;
    if (isValidMove(columns, rows, icol)) {
      score = 1;

      for (x <- -1 to 1; y <- -1 to 1) {
        if (!(x == 0 && y == 0)) {
          if (canCaptureDir(columns, rows, x, y, icol)) {
            score = score + scoreInBetweensDir(columns, rows, x, y, icol);
          }
        }
      }
    }
    return score
  }

  def doInBetweens(x: Int, y: Int, n: Int) {
    for (i <- -1 to 1; j <- -1 to 1) {
      if (!(i == 0 && j == 0)) {
        if (canCaptureDir(x, y, i, j, n)) {
          doInBetweensDir(x, y, i, j, n);
        }
      }
    }
  }

  def doInBetweensDir(x: Int, y: Int, xoff: Int, yoff: Int, n: Int) {
    var thiscolor = n;
    var thatcolor = 0;

    if (thiscolor == 1) { thatcolor = 2; }
    if (thiscolor == 2) { thatcolor = 1; }
    if (getAt(x + xoff,y + yoff) == thatcolor) {
      setAt(x + xoff, y + yoff, thiscolor);
      doInBetweensDir(x + xoff, y + yoff, xoff, yoff, thiscolor);
    }

  }

  def doMove(x: Int, y: Int, n: Int) {

    if (isValidMove(x, y, n)) {
      setAt(x, y, getPlayer);

      doInBetweens(x, y, n);

      nextMove();
    } else {
       //TODO move to gui
    	println("not a valid move");
    }
  }

  def isComputersMove(test: Int): Boolean = {
    //if whoseturn is computer's move return true

    if (compwhite == true && test == sqwhite) { return true; }
    if (compblack == true && test == sqblack) { return true; }

    return false;
  }

  def doComputersMove(icol: Int) {
    var highscore = 0.0
    var lowscore = 0.0
    var lowx = 0
    var lowy = 0

    var highx = 0
    var highy = 0

    for (columns <- 1 to max_cols ; rows <- 1 to max_rows ) {
      var currscore = getValue(columns, rows, icol);

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
    doMove(highx, highy, icol)
  }

  //register click on the board.
  def clickat(x: Int, y: Int) {
    doMove(x, y, getPlayer());
  }

  def getPlayer() = whoseturn

}