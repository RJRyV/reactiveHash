package reactiveHash.hash

import scala.concurrent.{Future, ExecutionContext}
import scala.collection.immutable.{Set, IndexedSeq}

object MinHash {
  
  import RowHashing.{RowHashValue, createRowHashSuite, Signature, RowHashSuite}
  
  def minHash(sig1 : Signature, sig2 : Signature) = 
    sig1.zip(sig2).map { t => 
      if(t._1.hashCodeLong < t._2.hashCodeLong) t._1 else t._2
    }
  
  def columnToSignature(hashSuite : RowHashSuite)(column : Column) : Signature = {
    //iterator is finally spun through with the reduce...
    column.map(row => hashSuite.map(_(row))) reduce minHash
  }//end def columnToSignatureByHash
    
  import ShingleHashing.{docToColumn}
  
  def singleDocToSignatureByHash(hashSuite : RowHashSuite)(document : Document, k : Int) : Signature = 
    columnToSignature(hashSuite)(docToColumn(document, k))
  
  
  def docsToSigByHash(documents : Iterable[Document], 
                      k : Int,
                      signatureCount : Int) : Iterable[Signature] = {
    val hashSuite = createRowHashSuite(signatureCount)
    
    def docToSig(doc : Document) = singleDocToSignatureByHash(hashSuite)(doc, k)
    
    documents map docToSig
  }//end def docsToSigByHash
  
  def docsToSigByHashAsync(documents : Iterable[Document], 
                           k : Int = 9,
                           signatureCount : Int = 100)(implicit ec : ExecutionContext) : Iterable[Future[Signature]] = {
    def newHashSuite = createRowHashSuite(signatureCount)
    
    def docToSig(doc : Document) = 
      singleDocToSignatureByHash(newHashSuite)(doc, k)
    
    documents.map(doc => Future(docToSig(doc))) 
  }//end def docsToSigByHashAsync
}//end object MinHash

//31337