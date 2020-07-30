package com.packt.ch6

import junit.framework.TestCase

import org.jmock.Mockery
import org.jmock.Expectations


// http://jmock.org/
class JmockTest extends TestCase {

  import scala.collection.mutable

  val context = new Mockery

  def testOneSubscriberReceivesAMessage(): Unit = { // set up
    val subscriber = context.mock(classOf[mutable.Subscriber[_, _]])
//    val publisher = new Publisher
//    publisher.add(subscriber)
    val message = "message"
    // expectations
    context.checking(new Expectations() {})
    // execute
//    publisher.publish(message)
    // verify
    context.assertIsSatisfied
  }


}

trait Subscriber {
  def receive(message: String): Unit
}