package com.htwg

class Board(max_cols:Int, max_rows:Int) {

  private val _cells = Array.ofDim[Cell](max_cols,max_rows)
  var player : Int =1;
  
   // Getter 
  def cells = _cells

  // Setter 
  def setCell(col: Int, row: Int, cell:Cell): Unit = {
    _cells(col)(row) = cell
  }
  
  def nextPlayer() {
    if(player==1) {
      player=2
    }else{
      player=1
    }    
  }
  
  def add(col: Int): Boolean = {
    var row  = findEmpty(col)
    if(row>(-1) && row<max_rows) {
      _cells(col)(row) = new Cell(col,row,player)
      nextPlayer()
      return true
    }
    false
  }
  
  def findEmpty(col:Int):Integer = {
	  if(col>=max_cols) return -1
    
	  for (index <- 0 until max_rows) {
		  if(_cells(col)(index) == null) return index
	  }
	  return -1
	}
  
  def toSting(): String = {
    var ret =""
     for (row <- 0 until max_rows; column <- 0 until max_cols) {
      
      if(row==0 && column==0){
        ret += "-"*((max_cols*2)+1)
        ret +="\n";
      }
      if(column==0) ret += "|"

      if(cells(column)(row)!=null && !cells(column)(row).empty) {
        ret +=cells(column)(row).toString()
      }else{
        ret +=" "
      }
      ret +="|"
      
      if(column == (max_cols-1))  ret +="\n";
      
      if(row==(max_rows-1) && column == (max_cols-1)){
        ret+="-"*((max_cols*2)+1)
        ret +="\n";
      }
    }
    ret
  }
  
}