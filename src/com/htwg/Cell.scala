package com.htwg

class Cell(c: Int, r: Int, v: Int) {
  var value = v

  def this(c: Int, r: Int) {
    this(c, r, 0)
  }

  // Getter 
  def row = r
  def col = c
  def empty = value == 0

  def set(v: Int): Unit = {
   value = v
  }
  
  override def toString = value.toString.replace('0', ' ')
}