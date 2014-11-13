package com.htwg

class Board(cols: Int, rows: Int) {
  var max_cols =cols
  var max_rows =rows
  private var _cells = Array.ofDim[Cell](max_cols, max_rows) 

  
  def init(cols: Int, rows: Int): Unit = {
    max_cols =cols
    max_rows =rows
    _cells = Array.ofDim[Cell](max_cols, max_rows)  
  }

  // Getter 
  def cells = _cells

  // Setter 
  def setCell(cell: Cell): Unit = {
    _cells(cell.col)(cell.row) = cell
  }

  def findEmpty(col: Int): Integer = {
    if (col >= max_cols) return -1

    for (index <- 0 until max_rows) {
      if (_cells(col)(index) == null) return index
    }
    return -1
  }

  override def toString(): String = {
    var ret = ""
    for (row <- 0 until max_rows; column <- 0 until max_cols) {

      if (row == 0 && column == 0) {
        ret += "-" * ((max_cols * 2) + 1)
        ret += "\n";
      }
      if (column == 0) ret += "|"

      if (cells(column)(row) != null && !cells(column)(row).empty) {
        ret += cells(column)(row).toString()
      } else {
        ret += " "
      }
      ret += "|"

      if (column == (max_cols - 1)) ret += "\n";

      if (row == (max_rows - 1) && column == (max_cols - 1)) {
        ret += "-" * ((max_cols * 2) + 1)
        ret += "\n";
      }
    }
    ret
  }

}