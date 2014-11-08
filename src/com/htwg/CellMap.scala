package com.htwg

class CellMap(size: Int) {
	val cells=new Array[Cell](size)  
	
	def add (value:Int):Boolean={
	  val freerow:Int=findEmpty()
	  if(freerow>(-1)){
	    cells(freerow).value = value
	    true
	  }
	  false
	}
	
	def findEmpty():Integer = {
	  for (index <- 0 until size) {
		  if(cells(index) != null) return index
	  }
	  return -1
	}
}