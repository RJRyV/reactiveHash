package reactiveHash

import scala.collection.immutable.{Map, Set, Stream}

package object hash {
  
  type Shingle = Stream[Char]
  
  type Name = String
  type Body = String
  
  type ShingleHashValue = Long
  
  type Column = Iterator[ShingleHashValue]
  
  type CharacteristicMatrix = Map[Name, Column]
  
  type Row = Long
  val ROW_LENGTH = 8

}//end package object hash

//31337