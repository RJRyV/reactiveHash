package reactiveHash.hash


import scala.concurrent.ExecutionContext

object LocalitySensitiveHashing {
  
  import RowHashing.Signature
  
  def bandHash(band : Signature, bucketCount : Long) = 
    (0L /: band)((hash, b) => hash * 31L + b.hashCodeLong) % bucketCount
  
  def signatureToBucketList(signature : Signature, 
                            bandSize : Int,
                            bucketCount : Long) =
    signature.grouped(bandSize).map(bandHash(_, bucketCount)).toSet
  
    
  import MinHash.singleDocToSignatureByHash
  import RowHashing.createRowHashSuite
  def createBucketList(document : Document,
                       k : Int,
                       signatureCount : Int,
                       bandSize : Int,
                       bucketCount : Long) = {
    val signature =
      singleDocToSignatureByHash(createRowHashSuite(signatureCount))(document,k)
      
    signatureToBucketList(signature, bandSize, bucketCount)
  }
  
  import MinHash.docsToSigByHashAsync
  def docsToPossibleMatchesAsync(documents : Iterable[Document],
                                 k : Int,
                                 signatureCount : Int,
                                 bandSize : Int,
                                 bucketCount : Long)(implicit ec : ExecutionContext) = {
    val signatures = docsToSigByHashAsync(documents, k, signatureCount)
    
    val docsToBuckets = 
      documents.zip(signatures.map(_.map(signatureToBucketList(_, bandSize, bucketCount))))
    
    docsToBuckets map { docAndBuckets => 
      val docName = docAndBuckets._1.name
      docAndBuckets._2.map(buckets => buckets.map(b => b -> docName)
                                             .groupBy(_._1)
                                             .map(_._2.map(_._2))
                                             .reduce(_.union(_)))
    }
  }//end def docsToBucketListAsync

}///end object LocalitySensitiveHashing

//31337