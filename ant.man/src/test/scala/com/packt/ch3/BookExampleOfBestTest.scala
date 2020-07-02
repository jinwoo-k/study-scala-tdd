package com.packt.ch3

import java.io.{File, FileWriter}
import java.util.concurrent.ConcurrentHashMap

import org.scalatest.{fixture, BeforeAndAfter, BeforeAndAfterEach, Failed, FlatSpec, Suite, SuiteMixin}

import scala.collection.mutable.ListBuffer

class BookExampleOfBestTest extends FlatSpec //with fixture.FlatSpec
{

  ///////// Calling get-fixture methods
  def fixture = new {
    val builder = new StringBuilder("ScalaTest is ")
    val buffer = new ListBuffer[String]
  }
  "Testing" should "be easy" in {
    val f = fixture
    f.builder.append("easy!")
    assert(f.builder.toString === "ScalaTest is easy!")
    assert(f.buffer.isEmpty)
    f.buffer += "sweet"
  }
  it should "be fun" in {
    val f = fixture
    f.builder.append("fun!")
    assert (f.builder.toString === "ScalaTest is fun!")
    assert (f.buffer.isEmpty)
  }

  //////// Instantiating fixture-context objects

  trait Builder {
    val builder = new StringBuilder("ScalaTest is ")
  }
  trait Buffer {
    val buffer = ListBuffer("ScalaTest", "is")
  }
  // This test needs the StringBuilder fixture
  "Testing" should "be productive" in new Builder {
    builder.append("productive!")
    assert(builder.toString === "ScalaTest is productive!")
  }
  // This test needs the ListBuffer[String] fixture
  "Test code" should "be readable" in new Buffer {
    buffer += ("readable!")
    assert(buffer === List("ScalaTest", "is", "readable!"))
  }
  // This test needs both the StringBuilder and ListBuffer
  it should "be clear and concise" in new Builder with Buffer {
    builder.append("clear!")
    buffer += ("concise!")
    assert(builder.toString === "ScalaTest is clear!")
    assert(buffer === List("ScalaTest", "is", "concise!"))
  }

  ////////////// Overriding withFixture(NoArgTest)
  override def withFixture(test: NoArgTest) = {
    super.withFixture(test) match {
      case failed: Failed =>
        val currDir = new File(".")
        val fileNames = currDir.list()
        info("Dir snapshot: " + fileNames.mkString(", "))
        failed
      case other => other
    }
  }
  "This test" should "succeed" in {
    assert(1 + 1 === 2)
  }
//  it should "fail" in {
//    assert(1 + 1 === 3)
//  }

  ////////////////// Calling loan-fixture methods
  object DbServer {
    // Simulating a database server
    type Db = StringBuffer
    private val databases = new ConcurrentHashMap[String, Db]
    def createDb(name: String): Db = {
      val db = new StringBuffer
      databases.put(name, db)
      db
    }
    def removeDb(name: String) {
      databases.remove(name)
    }
  }

  import DbServer._
  import java.util.UUID.randomUUID
  def withDatabase(testCode: Db => Any) {
    val dbName = randomUUID.toString
    val db = createDb(dbName)
    // create the fixture
    try {
      db.append("ScalaTest is ")
      // perform setup
      testCode(db)
      // "loan" the fixture to the test
    }
    finally removeDb(dbName)
    // clean up the fixture
  }
  def withFile(testCode: (File, FileWriter) => Any) {
    val file = File.createTempFile("hello", "world")
    // create the fixture
    val writer = new FileWriter(file)
    try {
      writer.write("ScalaTest is ")
      // set up the fixture
      testCode(file, writer)
      // "loan" the fixture to the test
    }
    finally writer.close()
    // clean up the fixture
  }
  // This test needs the file fixture
  "Testing" should "be productive2" in withFile {
    (file, writer) =>
      writer.write("productive!")
      writer.flush()
      assert(file.length === 24)
  }
  // This test needs the database fixture
  "Test code" should "be readable2" in withDatabase {
    db => db.append("readable!")
      assert(db.toString === "ScalaTest is readable!")
  }
  // This test needs both the file and the database
  it should "be clear and concise2" in withDatabase {
    db => withFile { (file, writer) =>
      // loan-fixture methods compose
      db.append("clear!")
      writer.write("concise!")
      writer.flush()
      assert(db.toString === "ScalaTest is clear!")
      assert(file.length === 21)
    }
  }


}
//////// Overriding withFixture(OneArgTest)
class ExampleSpec extends fixture.FlatSpec {
  case class FixtureParam(file: File, writer: FileWriter)
  def withFixture(test: OneArgTest) = {
    val file = File.createTempFile("hello", "world") // create the fixture
    val writer = new FileWriter(file)
    val theFixture = FixtureParam(file, writer)
    try {
      writer.write("ScalaTest is ")
      // set up the fixture
      withFixture(test.toNoArgTest(theFixture))
      // "loan" the fixture to the test
    } finally writer.close()
    // clean up the fixture
  }
  "Testing" should "be easy" in {
    f => f.writer.write("easy!")
      f.writer.flush()
      assert(f.file.length === 18)
  }
  it should "be fun" in {
    f => f.writer.write("fun!")
      f.writer.flush()
      assert(f.file.length === 17)
  }
}

////////// Mixing in BeforeAndAfter
class ExampleSpec2 extends FlatSpec with BeforeAndAfter {
  val builder = new StringBuilder
  val buffer = new ListBuffer[String]
  before {
    builder.append("ScalaTest is ")
  }
  after {
    builder.clear()
    buffer.clear()
  }
  "Testing" should "be easy" in {
    builder.append("easy!")
    assert(builder.toString === "ScalaTest is easy!")
    assert(buffer.isEmpty)
    buffer += "sweet"
  }
  it should "be fun" in {
    builder.append("fun!")
    assert(builder.toString === "ScalaTest is fun!")
    assert(buffer.isEmpty)
  }
}

//////////// Composing fixtures by stacking traits
trait Builder extends SuiteMixin {
  this: Suite =>
  val builder = new StringBuilder
  abstract override def withFixture(test: NoArgTest) = {
    builder.append("ScalaTest is ")
    try super.withFixture(test)
      // To be stackable, must call super.withFixture
    finally builder.clear()
  }
}
trait Buffer extends SuiteMixin {
  this: Suite =>
  val buffer = new ListBuffer[String]
  abstract override def withFixture(test: NoArgTest) = {
    try super.withFixture(test)
      // To be stackable, must call super.withFixture
    finally buffer.clear()
  }
}
class ExampleSpec3 extends FlatSpec with Builder with Buffer {
  "Testing" should "be easy" in {
    builder.append("easy!")
    assert(builder.toString === "ScalaTest is easy!")
    assert(buffer.isEmpty)
    buffer += "sweet"
  }
  it should "be fun" in {
    builder.append("fun!")
    assert(builder.toString === "ScalaTest is fun!")
    assert(buffer.isEmpty)
    buffer += "clear"
  }
}


/////

trait Builder2 extends BeforeAndAfterEach {
  this: Suite =>
  val builder = new StringBuilder
  override def beforeEach() {
    builder.append("ScalaTest is ")
    super.beforeEach()
    // To be stackable, must call super.beforeEach
  }
  override def afterEach() {
    try super.afterEach()
      // To be stackable, must call super.afterEach
    finally builder.clear()
  }
}
trait Buffer2 extends BeforeAndAfterEach {
  this: Suite =>
  val buffer = new ListBuffer[String]
  override def afterEach() {
    try super.afterEach()
      // To be stackable, must call super.afterEach
    finally buffer.clear()
  }
}

class ExampleSpec33 extends FlatSpec with Builder2 with Buffer2 {
  "Testing" should "be easy" in {
    builder.append("easy!")
    assert(builder.toString === "ScalaTest is easy!")
    assert(buffer.isEmpty)
    buffer += "sweet"
  }
  it should "be fun" in {
    builder.append("fun!")
    assert(builder.toString === "ScalaTest is fun!")
    assert(buffer.isEmpty)
    buffer += "clear"
  }
}