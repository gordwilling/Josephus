package com.highbar.josephus

import java.io.ByteArrayOutputStream

import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import scala.collection.mutable

class MainSpec extends FunSpec with Matchers with GeneratorDrivenPropertyChecks {

  def memoize[T, R](f: T => R): T => R = {
    new mutable.HashMap[T, R]() {
      override def apply(t: T): R = getOrElseUpdate(t, f(t))
    }
  }

  /**
    * This version isn't tail recursive and blows up
    * the stack on moderately large n ( ~ > 7500 on my machine)
    * However, we can use it to test against the other version
    * for n below this range
    */
  lazy val josephus: ((Int, Int)) => Int = memoize[(Int, Int), Int] {
    case (n, k) if n < 1 || k < 1 => throw new IllegalArgumentException("undefined where n < 1 or k < 1")
    case (n, _) if n == 1 => 1
    case (n, k) => ((josephus((n - 1, k)) + k - 1) % n) + 1
  }

  describe("Main") {
    def outputShouldBeUsage(emptyArray: Array[String]) = {
      val bos = new ByteArrayOutputStream()
      Console.withOut(bos) {
        Main.main(emptyArray)
      }
      bos.toString shouldEqual Main.usage
    }

    it("prints the usage message when no parameters are given on the command line") {
      outputShouldBeUsage(Array.empty[String])
    }

    it("prints the usage message when only one parameter is given on the command line") {
      outputShouldBeUsage(Array("1"))
    }

    it("prints the usage message when non-integer parameters are given on the command line") {
      outputShouldBeUsage(Array("a", "b"))
    }
  }

  describe("Josephus(n, k)") {
    it("resolves to a cycle-binary-shift 1 left on n when k = 2") {
      forAll(genN) {
        (n: Int) => {
          def leftCyclicalShift(i: Int) = {
            val j = Integer.highestOneBit(i)
            ((i ^ j) << 1) | 1
          }
          Main.josephus(n, 2) shouldEqual leftCyclicalShift(n)
        }
      }
    }

    it("resolves to 3 when n = 3 and k = 2") {
      Main.josephus(3, 2) shouldEqual 3
    }
  }
}