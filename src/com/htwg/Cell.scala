package com.htwg

class Cell(val value: Int) {
  def this() = this(0)
  def isEmpty = value == 0
  
  override def toString = value.toString.replace('0', ' ')
}
