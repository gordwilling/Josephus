package com.highbar

import org.scalacheck.Gen

package object josephus  {

  val TenMillion = 10000000

  implicit val genInt: Gen[Int] = Gen.choose[Int](1, TenMillion)

  implicit val genSmallInt: Gen[Int] = Gen.choose[Int](1, 1000)
}
