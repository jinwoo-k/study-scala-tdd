package com.packt.ch9

import com.packt.ch7.UnitSpec
import org.scalatest.concurrent.{Eventually, IntegrationPatience}
import org.scalatest.time.{Millis, Seconds, Span}

class EventuallyTraitTest extends UnitSpec with Eventually with IntegrationPatience {

  val alphabets = 'a' to 'z'
  val iterator = alphabets.iterator
//  eventually { iterator.next should be ('z') }


//  val alphabets = 'a' to 'z'
//  val iterator = alphabets.iterator
//  eventually { Thread.sleep(50); iterator.next should be ('c') }

  implicit override val patienceConfig =
    PatienceConfig(timeout = scaled(Span(5, Seconds)), interval = scaled(Span(3, Millis)))

  // Change timeout:
  eventually (timeout(Span(2, Seconds))) { iterator.next should be ('p') }

  // Change interval:
  eventually (interval(Span(3, Millis))) { iterator.next should be ('p') }

  // Change both:
  eventually (timeout(Span(2, Seconds)), interval(Span(3, Millis))) { iterator.next should be ('p') }
}
