import upickle.default.{ReadWriter => RW, macroRW}

case class HeisigObj (cardNumber: Int,
                      cardName: String,
                      frontSide:String,
                      backSide:String,
                      primaryInfo: String,
                      secondaryInfo: String,
                      notableCards: List[Int],
                      dateOfLastReview: String,
                      repetitionValue: Int,
                      repetitionHistory: List[Int],
                      tags: List[String])

//export interface FlashCard {
//    cardNumber: number;
//    cardName: string;
//    frontSide: string;
//    backSide: string;
//    primaryInfo: string;
//    secondaryInfo: string;
//    notableCards: number[];
//    dateOfLastReview: string;
//    repetitionValue: number;
//    repetitionHistory: number[];
//}