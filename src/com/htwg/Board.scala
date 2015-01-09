package com.htwg

class Board(size: Size) {
  private val _cells = Array.ofDim[Cell](size.x, size.y)

  init

  def setCell(position: Position, cell: Cell): Unit = setCellInternal(position.column - 1, position.row - 1, cell)
  private def setCellInternal(column: Integer, row: Integer, cell: Cell) = {
    _cells(column)(row) = cell
  }
  def getCell(position: Position) =  _cells(position.column - 1)(position.row - 1)

  def init() {
    for (column <- 0 until size.x; row <- 0 until size.y) {
      setCellInternal(column, row, new Cell)
    }
  }

  override def toString(): String = {
    var ret = ""
    for (row <- 0 until size.y; column <- 0 until size.x) {

      if (row == 0 && column == 0) {
        ret += "-" * ((size.x * 2) + 1)
        ret += "\n";
      }
      if (column == 0) ret += "|"

      if (_cells(column)(row) != null && !_cells(column)(row).isEmpty) {
        ret += _cells(column)(row).toString()
      } else {
        ret += " "
      }
      ret += "|"

      if (column == (size.x - 1)) ret += "\n";

      if (row == (size.y - 1) && column == (size.x - 1)) {
        ret += "-" * ((size.x * 2) + 1)
        ret += "\n";
      }
    }
    ret
  }

}