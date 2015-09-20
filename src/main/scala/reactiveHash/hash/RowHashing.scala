package reactiveHash.hash

import scala.collection.immutable.IndexedSeq

import java.security.MessageDigest

import java.nio.ByteBuffer

object RowHashing {
  
  class RowHashValue(val bytes : Array[Byte]) {
    lazy val hashCodeLong : Long = 
      (0L /: bytes)((hash, byte) => hash*31L + byte.toLong)
    
    override def equals(x : Any) =
      x.isInstanceOf[RowHashValue] && 
        x.asInstanceOf[RowHashValue].hashCodeLong == hashCodeLong
  }//end class RowHashValue
  
  type Signature = IndexedSeq[RowHashValue]
  
  type RowHashFunction = (Row) => RowHashValue 
  
  val MD5 = "MD5"
  
  def createRowToByteArrayConverter : (Row) => Array[Byte]  = {
   val byteBuffer = ByteBuffer.allocate(ROW_LENGTH)
   (row : Row) => byteBuffer.putLong(row).array()
  }
  
  def createRowHashFunction(seed : Row) : RowHashFunction = {
    val messageDigest = MessageDigest.getInstance(MD5)
    
    def digest(bytes : Array[Byte]) = messageDigest.digest(bytes)
    
    val rowToByteArray = createRowToByteArrayConverter
    
    val seedBytes = rowToByteArray(seed)
    
    (row : Row) => new RowHashValue(digest(seedBytes ++rowToByteArray(row)))
  }
  
  type RowHashSuite = IndexedSeq[RowHashFunction]
  
  def createRowHashSuite(size : Int) : RowHashSuite = 
    (0 until size).map(_.toLong) map createRowHashFunction
    
}//end object RowHashing

//31337