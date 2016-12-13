package com.highbar.josephus

import java.io.ByteArrayOutputStream

import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import scala.annotation.tailrec

class MainSpec extends FunSpec with Matchers with GeneratorDrivenPropertyChecks {

  describe("Main") {
    it("prints the usage message when no parameters are given on the command line") {
      outputShouldBeUsage()
    }

    it("prints the usage message when only one parameter is given on the command line") {
      outputShouldBeUsage("1")
    }

    it("prints the usage message when non-integer parameters are given on the command line") {
      outputShouldBeUsage("a", "b")
    }

    it("prints the usage message when any of the two parameters are less than 1") {
      outputShouldBeUsage("-1", "-1")
      outputShouldBeUsage("0", "0")
      outputShouldBeUsage("0", "1")
      outputShouldBeUsage("1", "0")
    }

    def outputShouldBeUsage(ss: String*) = {
      val bos = new ByteArrayOutputStream()
      Console.withOut(bos) {
        Main.main(ss.toArray)
      }
      bos.toString.trim shouldEqual Main.usage.trim
    }
  }

  describe("Josephus(n, k)") {
    it("resolves to a cyclical binary shift by 1 left on n when k = 2") {
      forAll(genInt) {
        (n: Int) => {
          def shiftLeftCyclical(i: Int) = {
            val j = Integer.highestOneBit(i)
            ((i ^ j) << 1) | 1
          }
          Main.josephus(n, 2) shouldEqual shiftLeftCyclical(n)
        }
      }
    }

    it("produces an answer when n = k, but the significance escapes me :(") {
      forAll(genInt) {
        (n: Int) => {
          Main.josephus(n, n) should not be 0
        }
      }
    }

    it("resolves to 3 when n = 3 and k = 2") {
      Main.josephus(3, 2) shouldEqual 3
    }

    it("handles very large n and small k") {
      noException should be thrownBy Main.josephus(1e8.toInt, 9)
    }

    it("handles small n and very large k") {
      noException should be thrownBy Main.josephus(9, 1e8.toInt)
    }

    it("produces the same answer as the naive implementation") {
      forAll(genSmallInt, genSmallInt) {
        (n: Int, k: Int) => {
          Main.josephus(n, k) shouldEqual naiveJosephus(n, k)
        }
      }
    }
  }

  /**
    * The naive approach puts the items in a circular collection
    * and drops the kth element until there is only one item left
    * We'll use this for testing - generated input values will be
    * passed to both functions and the outputs should be equal
    */
  def naiveJosephus(n: Int, k: Int): Int = {
    @tailrec
    def drop(xs: Vector[Int], k: Int): Int = {
      if (xs.size == 1) xs(0)
      else if (xs.size < k) {
        val mod = k % xs.size
        if (mod == 0) drop(xs.dropRight(1), k)
        else drop(xs.drop(mod) ++ xs.take(mod - 1), k)
      }
      else drop(xs.drop(k) ++ xs.take(k - 1), k)
    }
    drop((1 to n).toVector, k)
  }
}