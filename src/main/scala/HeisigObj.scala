import upickle.default.{ReadWriter => RW, macroRW}

case class HeisigObj (cardNumber: Int,
                      cardName: String,
                      frontSide:String,
                      backSide:String,
                      infoPrimary: String,
                      infoSecondary: String,
                      notableCards: List[Int],
                      dateOfLastReview: String,
                      repetitionValue: Int)
