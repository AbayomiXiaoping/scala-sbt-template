package com.github.suriyakrishna.sbt.template.test

import com.github.suriyakrishna.sbt.template.MyApplication
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

class MyApplicationTest extends AnyFunSuite with BeforeAndAfterAll {
  test("Sum of 2 and 3 should be 5") {
    assertResult(5)(MyApplication.sumNums(2, 3))
  }

  test("printColor with Red should print Color is Red") {
    MyApplication.printColor("Red")
  }

  test("printColor with Blue should print Color is Blue") {
    MyApplication.printColor("BLUE")
  }

  test("printColor with Green should print Color not defined") {
    MyApplication.printColor("Green")
  }

  test("main method is executable") {
    MyApplication.main(Array.empty)
  }
}
