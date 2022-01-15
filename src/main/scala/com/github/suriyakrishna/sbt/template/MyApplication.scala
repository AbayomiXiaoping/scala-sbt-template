package com.github.suriyakrishna.sbt.template

import scala.annotation.varargs

object MyApplication {

  @varargs
  def sumNums(a: Int, b: Int, c: Int*): Int = {
    a + b + c.sum
  }

  def main(args: Array[String]): Unit = {
    println("Hello World")
  }
}
