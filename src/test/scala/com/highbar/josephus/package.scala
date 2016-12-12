package com.highbar

import org.scalacheck.Gen

package object josephus  {

  val TenMillion = 100000000
  val TenThousand = 10000

  implicit val genN: Gen[Int] = Gen.choose[Int](1, TenMillion)

  implicit val genK: Gen[Int] = Gen.choose[Int](1, TenThousand)

}
