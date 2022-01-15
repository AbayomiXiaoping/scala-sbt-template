package com.github.suriyakrishna.sbt.template.test

import com.github.suriyakrishna.sbt.template.MyApplication
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

class MyApplicationTest extends AnyFunSuite with BeforeAndAfterAll {
  test("Sum of 2 and 3 should be 5") {
    assertResult(5)(MyApplication.sumNums(2, 3))
  }

  test("Should be able to pass more than 2 number to sumNums Method") {}
  assertResult(14)(MyApplication.sumNums(2, 3, 4, 5))

  test("main method is executable") {
    MyApplication.main(Array.empty)
  }
}
