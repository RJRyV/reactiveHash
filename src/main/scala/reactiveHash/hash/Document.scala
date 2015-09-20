package reactiveHash.hash

import scala.collection.immutable.Stream

case class Document(val name : Name, val body : () => Stream[Char]) {
  def shingles(k : Int) : Iterator[Shingle] = body().sliding(k)
}

//31337 