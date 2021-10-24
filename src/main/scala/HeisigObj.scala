import upickle.default.{ReadWriter => RW, macroRW}

case class HeisigObj (number: Int,
                      character:String,
                      keyword:String,
                      story: String,
                      notableOtherCharacters: List[String],
                      dateOfLastReview: String,
                      repetitionValue: Int)
