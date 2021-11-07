import upickle.default.{macroRW, ReadWriter => RW}

import scala.collection.immutable.HashMap

case class HeisigCollection(deckName: String,
                            deckInfo: String,
                            settings: HashMap[String,String],
                            tags: HashMap[String, String],
                            cards: Array[HeisigObj])

//export interface FlashCardDeck {
//    deckName: string;
//    deckInfo: string;
//    settings: Map<string, string>;
//    tags:  Map<string, string>;
//    cards: FlashCard[];
//}



//object HeisigCollection{
//  implicit val rw: RW[HeisigCollection] = macroRW
//}


/*
object HeisigObj {
  implicit val decoder: Decoder[HeisigObj] = deriveDecoder[HeisigObj]
  implicit val encoder: Encoder[HeisigObj] = deriveEncoder[HeisigObj]
}
*/
