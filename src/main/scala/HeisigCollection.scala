import upickle.default.{ReadWriter => RW, macroRW}

case class HeisigCollection(characterset: String, content: Array[HeisigObj])
//object HeisigCollection{
//  implicit val rw: RW[HeisigCollection] = macroRW
//}


/*
object HeisigObj {
  implicit val decoder: Decoder[HeisigObj] = deriveDecoder[HeisigObj]
  implicit val encoder: Encoder[HeisigObj] = deriveEncoder[HeisigObj]
}
*/
