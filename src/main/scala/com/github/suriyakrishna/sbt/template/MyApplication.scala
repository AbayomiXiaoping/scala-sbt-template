package com.github.suriyakrishna.sbt.template

object MyApplication {

  def sumNums(a: Int, b: Int): Int = {
    a + b
  }

  def printColor(color: String): Unit = {
    color.trim.toLowerCase match {
      case "red"  => println("Color is Red")
      case "blue" => println("Color is Blue")
      case _      => println("Color not defined")
    }
  }

  def main(args: Array[String]): Unit = {
    println("Hello World")
  }
}
