package com.htwg

class Size(val x: Integer, val y: Integer) {
  def center: Position = new Position((x / 2).toInt, (y / 2).toInt)
}