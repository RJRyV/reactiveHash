package reactiveHash.hash

import scala.collection.immutable.{IndexedSeq,Map}

import java.security.MessageDigest

object ShingleHashing {
  
  type ShingleHashFunction = (Shingle) => ShingleHashValue
  
  def createShingleHashFunction(seed : Long = 0) : ShingleHashFunction = 
    (shingle : Shingle) => (seed /: shingle)((hash, char) => hash * 31L + char.toLong)
    
  def docToColumn(document : Document, k : Int) : Column = {
    val shingleHash = createShingleHashFunction()
    document.shingles(k) map shingleHash
  }
    
  def createCharacteristicMatrix(docs : Iterable[Document], k : Int) : CharacteristicMatrix =
    docs.map(doc => doc.name -> docToColumn(doc, k))
        .toMap
  
}//end object Hashing

//31337