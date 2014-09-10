object Lab1 extends jsy.util.JsyApplication {
  import jsy.lab1.ast._
  import jsy.lab1.Parser

  /*
   * CSCI 3155: Lab 1
   * Brandon Mikulka
   *
   * Partner: Manpreet Singh
   * Collaborators: <Any Collaborators>
   */

  /*
   * Fill in the appropriate portions above by replacing things delimited
   * by '<'... '>'.
   *
   * Replace the 'throw new UnsupportedOperationException' expression with
   * your code in each function.
   *
   * Do not make other modifications to this template, such as
   * - adding "extends App" or "extends Application" to your Lab object,
   * - adding a "main" method, and
   * - leaving any failing asserts.
   *
   * Your lab will not be graded if it does not compile.
   *
   * This template compiles without error. Before you submit comment out any
   * code that does not compile or causes a failing assert.  Simply put in a
   * 'throws new UnsupportedOperationException' as needed to get something
   * that compiles without error.
   */

  /*
   * Example with a Unit Test
   *
   * A convenient, quick-and-dirty way to experiment, especially with small code
   * fragments, is to use the interactive Scala interpreter.
   *
   * To run a selection in the interpreter in Eclipse, highlight the code of interest
   * and type Ctrl+Shift+X (on Windows) or Cmd+Shift+X (on Mac).
   *
   * Highlight the next few lines below to try it out.  The assertion passes, so
   * it appears that nothing happens.  You can uncomment the "bad test specification"
   * and see that a failed assert throws an exception.
   *
   * You can try calling 'plus' with some arguments, for example, plus(1,2).  You
   * should get a result something like 'res0: Int = 3'.
   *
   * As an alternative, the testPlus2 function takes an argument that has the form
   * of a plus function, so we can try it with different implementations.  For example,
   * uncomment the "testPlus2(badplus)" line, and you will see an assertion failure.
   *
   * Our convention is that these "test" functions are testing code that are not part
   * of the "production" code.
   *
   * While writing such testing snippets are convenient, it is not ideal.  For example,
   * the 'testPlus1()' call is run whenever this object is loaded, so in practice,
   * it should probably be deleted for "release".  A more robust way to maintain
   * unit tests is in a separate file.  For us, we use the convention of writing
   * tests in a file called LabXSpec.scala (i.e., Lab1Spec.scala for Lab 1).
   */

  def plus(x: Int, y: Int): Int = x + y
  def testPlus1() {
    assert(plus(1,1) == 2)
    //assert(plus(1,1) == 3) // bad test specification
  }
  testPlus1()

  def badplus(x: Int, y: Int): Int = x - y
  def testPlus2(plus: (Int, Int) => Int) {
    assert(plus(1,1) == 2)
  }
  //testPlus2(badplus)

  /* Exercises */

  def abs(n: Double): Double = if (n<0) (-1)*n else n

  def xor(a: Boolean, b: Boolean): Boolean = {
    if (a) if (b) false else true else if (b) true else false
  }

 def repeat(s: String, n :Int): String = {
   require(n >= 0)
   if (n==0) "" else s + repeat(s, n-1)
 }

  def sqrtStep(c: Double, xn: Double): Double = xn - (xn*xn - c) / (2*xn)

  def sqrtN(c: Double, x0: Double, n:Int): Double = {
    require(n >= 0)
    if (n==0) x0
    else sqrtN(c, sqrtStep(c, x0), n-1)
  }

  def sqrtErr(c: Double, x0: Double, epsilon: Double): Double = {
    require(epsilon > 0)
    if (abs(x0*x0 - c) < epsilon) x0
    else sqrtErr(c, sqrtStep(c, x0), epsilon)
  }

  def sqrt(c: Double): Double = {
    require(c >= 0)
    if (c == 0) 0 else sqrtErr(c, 1.0, 0.0001)
  }

  /* Search Tree */

  sealed abstract class SearchTree
  case object Empty extends SearchTree
  case class Node(l: SearchTree, d: Int, r: SearchTree) extends SearchTree

  def repOk(t: SearchTree): Boolean = {
    def check(t: SearchTree, min: Int, max: Int): Boolean = t match {
      case Empty => true
      case Node(l, d, r) => {
        if (min <= d && d < max) check(l, min, d) && check(r, d, max)
        else false
      }
    }
    check(t, Int.MinValue, Int.MaxValue)
  }

  def insert(t: SearchTree, n: Int): SearchTree = t match{
    case Empty => Node(Empty, n, Empty)
    case Node(l,d,r) => {
      if (n < d) Node(insert(l, n), d, r)
      else Node(l, d, insert(r, n))
    }
  }

  def deleteMin(t: SearchTree): (SearchTree, Int) = {
    require(t != Empty)
    (t: @unchecked) match {
      case Node(Empty, d, r) => (r, d)
      case Node(l, d, r) =>
        val (l1, m) = deleteMin(l)
        (Node(l1, d, r), m)
    }
  }

  def delete(t: SearchTree, n: Int): SearchTree = t match{
    case Empty => Empty
    case Node(l, d, r) =>{
      if (n == d){
        if(r != Empty){
          val (l1, m) = deleteMin(r)
          Node(l, m, l1)
        }
        else l
      }
      else if (n < d) Node(delete(l, n), d, r)
      else Node(l, d, delete(r, n))
    }
  }

  /* JavaScripty */

  def eval(e: Expr): Double = e match {
    case N(n) => n
    case Unary(Neg, N(n)) => n* -1
    case Binary(op, Unary(Neg,N(n)), N(n2)) => eval(Binary(op, N(-1*n), N(n2)))
    case Binary(op, N(n), Unary(Neg,N(n2))) => eval(Binary(op, N(n), N(-1*n2)))
    case Binary(Plus, e:Expr, e2:Expr) => eval(e) + eval(e2)
//    case Binary(Plus, N(num1), N(num2)) => num1 + num2
    case Binary(Minus, e:Expr, e2:Expr) => eval(e) - eval(e2)
//    case Binary(Minus, N(num1), N(num2)) => num1 - num2
    case Binary(Times, e:Expr, e2:Expr) => eval(e) * eval(e2)
//    case Binary(Times, N(num1), N(num2)) => num1 * num2
    case Binary(Div, N(num1), N(num2)) => {
      if(num2 == 0) if(num1<0) Double.NegativeInfinity else Double.PositiveInfinity
      else num1 / num2
    }
    case Binary(Div, e:Expr, e2:Expr) => eval(Binary(Div, N(eval(e)), N(eval(e2))))
    case _ => throw new UnsupportedOperationException
  }

 // Interface to run your interpreter from a string.  This is convenient
 // for unit testing.
 def eval(s: String): Double = eval(Parser.parse(s))



 /* Interface to run your interpreter from the command-line.  You can ignore the code below. */

 def processFile(file: java.io.File) {
    if (debug) { println("Parsing ...") }

    val expr = Parser.parseFile(file)

    if (debug) {
      println("\nExpression AST:\n  " + expr)
      println("------------------------------------------------------------")
    }

    if (debug) { println("Evaluating ...") }

    val v = eval(expr)

    println(v)
  }

}
