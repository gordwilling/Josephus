package com.highbar.josephus

import scala.annotation.tailrec
import scala.util.Try

object Main {
  def main(args: Array[String]): Unit = {

    val result = for {
      n <- Try(Integer.parseInt(args(0)))
      k <- Try(Integer.parseInt(args(1)))
    } yield josephus(n, k)

    println(result.getOrElse(usage))
  }

  def josephus(n: Int, k: Int): Int = {
    if (n < 1 || k < 1) throw new IllegalArgumentException("undefined where n < 1 or k < 1")
    @tailrec
    def loop(i: Int, acc: Int): Int = {
      if (n < i) acc + 1
      else loop(i + 1, (acc + k) % i)
    }
    loop(1, 0)
  }

  def usage: String = {
    s"""
       |Usage: run n k   where n is the number of people in the circle,
       |                       k is the step size,
       |                       0 < n; n is an Integer
       |                       0 < k; k is an Integer
       |
      """.stripMargin
  }
}