package reactiveHash.stream

import akka.stream.scaladsl.Flow

import reactiveHash.hash.{Document, Column}

import reactiveHash.hash.ShingleHashing
import reactiveHash.hash.RowHashing.{RowHashSuite, Signature}
import reactiveHash.hash.MinHash
import reactiveHash.hash.LocalitySensitiveHashing
import reactiveHash.hash.LocalitySensitiveHashing.BucketList
object HashFlows {

  def docToColumn(k : Int) = {
    def docToCol(doc : Document) = ShingleHashing.docToColumn(doc, k)
    
    Flow[Document].map[Column](docToCol)
  }
  
  def columnToSignature(hashSuite : RowHashSuite) = 
    Flow[Column].map[Signature](MinHash.columnToSignature(hashSuite))
  
  def signatureToBucketList(bandSize : Int, bucketCount : Long) = 
    Flow[Signature].map[BucketList](LocalitySensitiveHashing.signatureToBucketList(_, bandSize, bucketCount))
    
}//end object HashFlows

//31337