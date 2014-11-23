package com.htwg

class ReversiModel(cols: Integer, rows: Integer) {

  // ---    start 		initialization
  private var max_cols = cols
  private var max_rows = rows

  private var gameStatus = GameStatus.NotStarted

  private var board = new Board(max_cols, max_rows)

  private var currentPlayer = Player.Player1
  private var numMoves = 0
  private val sqblank = 0
  private val debugMode = false

  //TODO active computer player by GUI
  private val computerPlaysAsPlayer2 = false;
  private val computerPlaysAsPlayer1 = false;

  reset(Player.Player1);

  // ---    end 		initialization

  // ---    start 		constructor
  
  def this() = this(8, 8)

  // ---    end 		constructor

  // ---    start 		public methods

  //register click on the board.
  def doMoveAt(x: Int, y: Int) = doMove(x, y);

  def reset(x: Integer, y: Integer, startWithPlayer: Integer) {
    numMoves = 0
    initializeBoard(x, y)
    gameStatus = GameStatus.InProgress
    currentPlayer = if (startWithPlayer == Player.Player1) Player.Player1 else Player.Player2
  }

  def getGameStatus = gameStatus 
  
  def reset(startWithPlayer: Int): Unit = reset(max_cols, max_rows, startWithPlayer)
  def reset(): Unit = reset(max_cols, max_rows, Player.Player1)

  def getPlayer = currentPlayer

  def getPlayerScore(player: Int) = calculateScore(player)

  def getCellValue(column: Int, row: Int): Int = getCell(column, row).value

  override def toString = board.toString

  // ---    end 		public methods

  // ---    start 		private methods

  private def getCell(column: Int, row: Int): Cell = board.cells(column - 1)(row - 1)

  private def setCellValueAt(column: Int, row: Int, value: Int) = getCell(column, row).set(value)

  private def initializeBoard(x: Integer, y: Integer) {
    board = new Board(x, y)
    setCellValueAt(max_cols / 2, max_rows / 2, Player.Player1)
    setCellValueAt((max_cols / 2) + 1, (max_rows / 2) + 1, Player.Player1)
    setCellValueAt(max_cols / 2, (max_rows / 2) + 1, Player.Player2)
    setCellValueAt((max_cols / 2) + 1, max_rows / 2, Player.Player2)
  }

  //private def getTotalScore() = {
  //  val wtscore = getPlayerScore(player2);
  //  val bkscore = getPlayerScore(player1);
  //  showScore(wtscore, bkscore);
  //}

  private def getScoreForPlayer(player: Int) = if (player == 1) getPlayerScore(Player.Player2) else getPlayerScore(Player.Player1)

  private def calculateScore(icol: Int): Int = {
    var sum = 0;
    for (column <- 1 to max_cols; row <- 1 to max_rows) {
      if (getCellValue(column, row) == icol) {
        sum += 1;
      }
    }
    sum
  }

  //TODO move to gui
  //private def showScore(wtscore: Int, bkscore: Int) {
  //  println("score white:" + wtscore + " black:" + bkscore)
  //}

  private def canCapture(x: Int, y: Int, n: Int): Boolean =
    {

      for (a <- -1 to 1; b <- -1 to 1) {
        if (!(a == 0 && b == 0)) {
          if (canCaptureDir(x, y, a, b, n))
            return true;
        }
      }
      return false;

    }

  private def canCaptureDir(x: Int, y: Int, xoff: Int, yoff: Int, n: Int): Boolean =
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
        y + yoff + yoff > max_rows) {
        return false;

      }

      if (getCellValue(x + xoff, y + yoff) == sqblank) {
        return false;
      }

      //one square of opposite color before a square of the same color.
      if (getCellValue(x + xoff, y + yoff) == thatcolor &&
        (
          getCellValue(x + xoff + xoff, y + yoff + yoff) == thiscolor || canCaptureDir(x + xoff, y + yoff, xoff, yoff, thiscolor))) {
        return true;
      }
      return false;
    }

  private def log(str: String) {
    if (debugMode) println(str)
  }

  private def isValidMove(columns: Int, rows: Int, n: Int): Boolean = {

    if (columns < 1 || columns > max_cols || rows < 1 || rows > max_rows) {
      log("out of bounce");
      return false;
    }
    if (getCellValue(columns, rows) != sqblank) {
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

  private def validMovesExist(n: Int): Boolean =
    {
      for (i <- 1 to max_cols; j <- 1 to max_rows) {
        if (isValidMove(i, j, n)) {
          return true;
        }
      }
      return false;
    }

  /*
  private def gameover() {
    var wtscore = calculateScore(player2);
    var bkscore = calculateScore(player1);
    var winner = ""

    if (wtscore > bkscore) {
      winner = "Winner is White";
    } else if (bkscore > wtscore) {
      winner = "Winner is Black";
    } else if (wtscore == bkscore) {
      winner = "Draw";
    }

    //TODO move to gui
    println("Game over!\n\nScore is:\nwhite " + calculateScore(player2) + "\nblack " + calculateScore(player1) + "\n" + winner);
  }
  */

  private def prepareNextMove() {
    //getTotalScore();

    if (isGameOver) {
      gameStatus = GameStatus.GameOver //gameover();
      return
    }

    if (currentPlayer == Player.Player2 && validMovesExist(Player.Player1)) {
      currentPlayer = Player.Player1;
      log("validmovesexist player1=1!")
    } else {
      if (currentPlayer == Player.Player1 && validMovesExist(Player.Player2)) {
        currentPlayer = Player.Player2
        log("validmovesexist player2=2!")
      }
    }

    if (isComputersMove) {
      //TODO move to gui
      println("computer move: ")

      doComputersMove;
    } else {
      //TODO move to gui
      println("nextMove: " + currentPlayer)
    }

    numMoves += 1;
  }

  private def isGameOver = !validMovesExist(Player.Player1) && !validMovesExist(Player.Player2)

  private def scoreInBetweensDir(x: Int, y: Int, xoff: Int, yoff: Int, n: Int): Int = {
    var thiscolor = n;
    var thatcolor = 0;
    var result = 0;
    if (thiscolor == 1) { thatcolor = 2; }
    if (thiscolor == 2) { thatcolor = 1; }

    if (getCellValue(x + xoff, y + yoff) == thatcolor) {
      result += 1;
      result += scoreInBetweensDir(x + xoff, y + yoff, xoff, yoff, thiscolor);
    }
    return result;
  }

  private def getValue(columns: Int, rows: Int, icol: Int): Int = {
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

  private def doInBetweens(x: Int, y: Int, n: Int) {
    for (i <- -1 to 1; j <- -1 to 1) {
      if (!(i == 0 && j == 0)) {
        if (canCaptureDir(x, y, i, j, n)) {
          doInBetweensDir(x, y, i, j, n);
        }
      }
    }
  }

  private def doInBetweensDir(x: Int, y: Int, xoff: Int, yoff: Int, n: Int) {
    var thiscolor = n;
    var thatcolor = 0;

    if (thiscolor == 1) { thatcolor = 2; }
    if (thiscolor == 2) { thatcolor = 1; }
    if (getCellValue(x + xoff, y + yoff) == thatcolor) {
      setCellValueAt(x + xoff, y + yoff, thiscolor);
      doInBetweensDir(x + xoff, y + yoff, xoff, yoff, thiscolor);
    }

  }

  private def doMove(x: Int, y: Int) {

    if (isValidMove(x, y, currentPlayer)) {
      setCellValueAt(x, y, currentPlayer);

      doInBetweens(x, y, currentPlayer);

      prepareNextMove();
    } else {
      //TODO move to gui
      //println("not a valid move");
    }
  }

  private def isComputersMove(): Boolean = {
    //if whoseturn is computer's move return true

    if (computerPlaysAsPlayer2 == true && currentPlayer == Player.Player2) { return true; }
    if (computerPlaysAsPlayer1 == true && currentPlayer == Player.Player1) { return true; }

    return false;
  }

  private def doComputersMove {
    var highscore = 0.0
    var lowscore = 0.0
    var lowx = 0
    var lowy = 0

    var highx = 0
    var highy = 0

    for (columns <- 1 to max_cols; rows <- 1 to max_rows) {
      var currscore = getValue(columns, rows, currentPlayer);

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
    doMove(highx, highy)
  }

  // ---    end 		private methods
}

object GameStatus extends Enumeration {
  type GameStatus = Value
  val NotStarted, InProgress, GameOver = Value
}

object Player {
  val Player1 = 1
  val Player2 = 2
}