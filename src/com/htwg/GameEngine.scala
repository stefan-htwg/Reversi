package com.htwg

import scalaz.IsEmpty

class GameEngine(boardSize: Size) {
  private var gameStatus = GameStatus.NotStarted
  private var board = new Board(boardSize)

  private var currentPlayer = Player.One
  private var numMoves = 0
  private val debugMode = false

  private val computerPlaysAsTwo = false;
  private val computerPlaysAsOne = false;

  reset(Player.One);

  def this() = this(new Size(8, 8))

  // ---    start 		public methods

  def doMoveAt(position: Position) = doMove(position);

  def reset(size: Size, startWithPlayer: Integer) {
    if (size.x < 1 || size.y < 1)
      return
    if (startWithPlayer != Player.One && startWithPlayer != Player.Two)
      return

    numMoves = 0
    initializeBoard(size)
    gameStatus = GameStatus.InProgress
    currentPlayer = startWithPlayer
  }

  def getGameStatus = gameStatus

  def reset(startWithPlayer: Int): Unit = reset(boardSize, startWithPlayer)
  def reset(): Unit = reset(boardSize, Player.One)

  def getPlayer = currentPlayer

  def getScoreFor(player: Int) = calculateScore(player)

  def getCellValue(position: Position): Int = getCell(position).value

  override def toString = board.toString

  // ---    end 		public methods

  // ---    start 		private methods

  private def getCell(position: Position): Cell = board.getCell(position)
  private def setCell(position: Position, cell: Cell) = board.setCell(position, cell)
  private def setCellValueAt(position: Position, value: Int) = setCell(position, new Cell(value))

  private def cellIsEmpty(position: Position): Boolean = getCell(position).isEmpty
  private def cellIsNotEmpty(position: Position): Boolean = !cellIsEmpty(position)

  private def initializeBoard(size: Size) {
    board = new Board(size)
    var center = boardSize.center
    setCellValueAt(center, Player.One)
    setCellValueAt(center add (new Position(1, 1)), Player.One)
    setCellValueAt(center add (new Position(0, 1)), Player.Two)
    setCellValueAt(center add (new Position(1, 0)), Player.Two)
  }

  private def getScoreForPlayer(player: Int) = if (player == Player.One) getScoreFor(Player.One) else getScoreFor(Player.Two)

  private def calculateScore(player: Int): Int = {
    var sum = 0;
    for (column <- 1 to boardSize.x; row <- 1 to boardSize.y) {
      if (getCellValue(new Position(column, row)) == player) {
        sum += 1;
      }
    }
    sum
  }

  private def canCapture(position: Position, player: Int): Boolean = {
    for (a <- -1 to 1; b <- -1 to 1) {
      var offset = new Position(a, b)
      if (isNotAEmptyOffset(offset)) {
        if (canCaptureDir(position, offset, player))
          return true;
      }
    }
    return false;
  }

  private def canCaptureDir(position: Position, offset: Position, player: Int): Boolean = {
    var current = player;
    var opponent = 0;
    if (current == 1) { opponent = 2 }
    if (current == 2) { opponent = 1 }

    var positionWithOffset = position add (offset)
    var positionWithTwoTimesTheOffset = positionWithOffset add (offset)

    if (positionWithTwoTimesTheOffset isOutOfBounce (boardSize)) return false
    if (cellIsEmpty(positionWithOffset)) return false;

    if (getCellValue(positionWithOffset) == opponent) {
      if (getCellValue(positionWithTwoTimesTheOffset) == current
        || canCaptureDir(positionWithOffset, offset, current)) {
        return true
      }
    }
    return false;
  }

  private def isValidMove(position: Position, player: Int): Boolean = {

    if (position isOutOfBounce (boardSize)) return false

    if (cellIsNotEmpty(position)) {
      Logger.info("Is not a valid move at: Column: " + position.column + " Row: " + position.row + ". Cell was not emtpy.");
      return false;
    }

    if (!canCapture(position, player)) return false

    Logger.info("Is a valid move at: Column: " + position.column + " Row: " + position.row + ". For Player: " + currentPlayer + ".")
    return true;
  }
  private def isNotAValidMove(position: Position, player: Int): Boolean = !isValidMove(position, player)

  private def validMoveExistsFor(player: Int): Boolean = {
    for (column <- 1 to boardSize.x; row <- 1 to boardSize.y) {
      if (isValidMove(new Position(column, row), player)) return true
    }
    return false;
  }

  private def validMoveDoesNotExistFor(player: Int): Boolean = !validMoveExistsFor(player)

  private def prepareNextMove() {
    if (gameIsOver) {
      gameStatus = GameStatus.GameOver
      return
    }

    numMoves += 1;
    if (currentPlayer == Player.Two) currentPlayer = Player.One
    else currentPlayer = Player.Two

    if (isComputersMove) {
      Logger.info("computers move: ")
      doComputersMove
    } else {
      Logger.info("nextMove: " + currentPlayer)
    }
  }

  private def gameCanBeContinued: Boolean =
    return (currentPlayer == Player.Two && validMoveExistsFor(Player.One)) ||
      (currentPlayer == Player.One && validMoveExistsFor(Player.Two))

  private def gameIsOver: Boolean = !gameCanBeContinued

  private def scoreInBetweensDir(position: Position, offset: Position, player: Int): Int = {
    var current = player;
    var opponent = 0;
    var result = 0;
    if (current == 1) opponent = 2
    if (current == 2) opponent = 1

    if (getCellValue(position add (offset)) == opponent) {
      result += 1;
      result += scoreInBetweensDir(position add (offset), offset, current);
    }
    return result;
  }

  private def getScore(position: Position, player: Int): Int = {
    if (isNotAValidMove(position, player)) return 0

    var score = 1;
    for (columnOffset <- -1 to 1; rowOffset <- -1 to 1) {
      var offset = new Position(columnOffset, rowOffset)
      if (isNotAEmptyOffset(offset)) {
        if (canCaptureDir(position, offset, player)) {
          score = score + scoreInBetweensDir(position, offset, player);
        }
      }
    }
    return score
  }

  private def doInBetweens(position: Position, player: Int) {
    for (columnOffset <- -1 to 1; rowOffset <- -1 to 1) {
      var offset = new Position(columnOffset, rowOffset)
      if (isNotAEmptyOffset(offset)) {
        if (canCaptureDir(position, offset, player)) {
          doInBetweensDir(position, offset, player);
        }
      }
    }
  }

  private def isEmptyOffset(offset: Position): Boolean = offset.column == 0 && offset.row == 0
  private def isNotAEmptyOffset(offset: Position): Boolean = !isEmptyOffset(offset)

  private def doInBetweensDir(position: Position, offset: Position, player: Int) {
    var current = player;
    var opponent = 0;
    if (current == 1) { opponent = 2; }
    if (current == 2) { opponent = 1; }

    if (getCellValue(position add (offset)) == opponent) {
      setCellValueAt(position add (offset), current);
      doInBetweensDir(position add (offset), offset, current);
    }
  }

  private def doMove(position: Position) {
    if (!isValidMove(position, currentPlayer)) {
      Logger.info("not a valid move");
      return ;
    }

    setCellValueAt(position, currentPlayer);
    doInBetweens(position, currentPlayer);
    prepareNextMove();
  }

  private def isComputersMove(): Boolean = {
    if (computerPlaysAsTwo && currentPlayer == Player.Two) return true
    if (computerPlaysAsOne && currentPlayer == Player.One) return true
    return false;
  }

  private def doComputersMove {
    var bestPosition = findBestPosition
    Logger.info("cp move:" + bestPosition.column + "/" + bestPosition.row);
    doMove(bestPosition)
  }

  private def findBestPosition: Position = {
    var highscore = 0.0
    var bestPosition = new Position(0, 0)

    for (column <- 1 to boardSize.x; row <- 1 to boardSize.y) {
      var position = new Position(column, row)
      var score = getScore(position, currentPlayer);

      if (score > highscore) {
        bestPosition = position
        highscore = score;
      }
    }
    return bestPosition
  }

  // ---    end 		private methods
}

object GameStatus extends Enumeration {
  type GameStatus = Value
  val NotStarted, InProgress, GameOver = Value
}