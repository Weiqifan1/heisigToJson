import upickle.default.{ReadWriter => RW, macroRW}

case class HeisigObj (cardNumber: Int,
                      cardName: String,
                      frontSide:String,
                      backSide:String,
                      primaryInfo: String,
                      secondaryInfo: String,
                      notableCards: List[Int],
                      dateOfLastReview: String,
                      repetitionValue: Int)
