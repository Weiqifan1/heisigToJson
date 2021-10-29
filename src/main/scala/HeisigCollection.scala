import upickle.default.{macroRW, ReadWriter => RW}

import scala.collection.immutable.HashMap

case class HeisigCollection(deckName: String, deckInfo: String, settings: HashMap[String,String], cards: Array[HeisigObj])
//object HeisigCollection{
//  implicit val rw: RW[HeisigCollection] = macroRW
//}


/*
object HeisigObj {
  implicit val decoder: Decoder[HeisigObj] = deriveDecoder[HeisigObj]
  implicit val encoder: Encoder[HeisigObj] = deriveEncoder[HeisigObj]
}
*/
