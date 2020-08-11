package com.packt.ch8

import org.specs2.Specification

class ExampleAcceptanceSpec extends Specification { def is =
  args(sequential = true) ^
  "Our example specification" ^
    "and we should run t1 here" ! t1 ^
    "and we should run t2 here" ! t2
  def t1 = success
  def t2 = pending
}