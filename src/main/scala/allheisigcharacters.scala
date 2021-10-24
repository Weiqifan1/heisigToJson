
import io.circe.Encoder
import io.circe.Encoder.AsObject.importedAsObjectEncoder
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import upickle._

import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import scala.util.Try
import upickle.default.{macroRW, ReadWriter => RW}
import upickle.legacy.write

import java.util
import javax.swing.text.AbstractDocument.Content
import scala.collection.mutable.ListBuffer

object allheisigcharacters {

  //  0     1     2     3  4   5  6     7          8            9            10       11      12           13         14
  //RTH #	RSH #	RTK #	TH	SH	K	H POS	RTH Keyword	RSH Keyword	RTK Keyword	RTH Read	TH Read	RTH Lesson	RSH Lesson	RTK Lesson	K and H check	SH and TH check	K and TH keywords	SH and TH keywords	K and H Visual Check
  //0001	0001	0001	一	一	一		one	one	one	yī	yī	RTH1-L01	RSH1-L01	RTK1v5-L01	K = SH = TH	SH = TH	K key = TH key	SH key = TH key



  def allChars(): HeisigCollection = {
    val source = scala.io.Source.fromFile("heisigStories/raw/heisigraw.csv")
    val lines: List[String] = source.getLines().toList
    val nestedLines: List[List[String]] = lines.map(_.split('\t').toList)
    println(lines.length)

    /*
                      cardNumber: Int,
                      cardName: String,
                      frontSide:String,
                      backSide:String,
                      infoPrimary: String,
                      infoSecondary: String,
                      notableCards: List[Int],
                      dateOfLastReview: String,
                      repetitionValue: Int
    */

    val previousTrad: ListBuffer[HeisigObj] = new ListBuffer[HeisigObj]
    val tradHanzi: HeisigCollection = HeisigCollection("HeisigTraditionalHanzi",
      nestedLines.filter(each => Try(each(0).toInt).isSuccess)
        .map(elem => new HeisigObj(elem(0).toInt, "", elem(7), elem(3), "", "", List[Int](), "0001-01-01", 0)).toArray,
      previousTrad.toArray)

    val previousSimp: ListBuffer[HeisigObj] = new ListBuffer[HeisigObj]
    val simpHanzi: HeisigCollection = HeisigCollection("HeisigSimplifiedHanzi",
      nestedLines.filter(each => Try(each(1).toInt).isSuccess)
        .map(elem => new HeisigObj(elem(1).toInt, "",  elem(8), elem(4), "", "", List[Int](), "0001-01-01", 0)).toArray,
      previousSimp.toArray)

    val previousKanji: ListBuffer[HeisigObj] = new ListBuffer[HeisigObj]
    val kanji: HeisigCollection = HeisigCollection("HeisigKanji",
      nestedLines.filter(each => Try(each(2).toInt).isSuccess)
        .map(elem => new HeisigObj(elem(2).toInt, "", elem(9), elem(5), "", "", List[Int](), "0001-01-01", 0)).toArray,
      previousKanji.toArray)


    val tradJson: String = Encoder[HeisigCollection].apply(tradHanzi).toString()
    val simpJson: String = Encoder[HeisigCollection].apply(simpHanzi).toString()
    val kanjiJson: String = Encoder[HeisigCollection].apply(kanji).toString()



/*
    Files.write(Paths.get("traditional.txt"), tradJson.getBytes(StandardCharsets.UTF_8))
    Files.write(Paths.get("simplified.txt"), simpJson.getBytes(StandardCharsets.UTF_8))
    Files.write(Paths.get("kanji.txt"), kanjiJson.getBytes(StandardCharsets.UTF_8))
*/

    val test = ""

    return tradHanzi//List(tradHanzi, simpHanzi, kanji)
  }

  /*nestedLines.filter(each => Try(each(1).toInt).isSuccess)
       .map(elem =>
         new HeisigObj(elem(1).toInt,"", "", elem(4), "","", List[Int](), "0001-01-01", 0)).toArray)*/
  def createdCollectionFromNested(nestedLines: List[List[String]], cedictTradDictionary: Map[String, String], tzai: Map[String, Int], isTraditional: Boolean): Array[HeisigObj] = {
    var finalReturn: ListBuffer[HeisigObj] = new ListBuffer[HeisigObj]()
    for (eachLines <- nestedLines) {
      if (isTraditional && Try(eachLines(0).toInt).isSuccess) {
        val character: String = eachLines(3).trim
        val cedictEntry: String = cedictTradDictionary.get(character).getOrElse("no cedict entry")
        val frequencyString: String = tzai.get(character).getOrElse("no frequency").toString
        val textToInclude: String = "frequency: " + frequencyString + " " + cedictEntry
        val item = new HeisigObj(eachLines(0).toInt, "", "", character, "", textToInclude, List[Int](), "0001-01-01", 0)
        finalReturn.addOne(item)
      }else if(!isTraditional && Try(eachLines(1).toInt).isSuccess) {
        val character: String = eachLines(4).trim
        val cedictEntry: String = cedictTradDictionary.get(character).getOrElse("no cedict entry")
        val frequencyString: String = tzai.get(character).getOrElse("no frequency").toString
        val textToInclude: String = "frequency: " + frequencyString + " " + cedictEntry
        val item = new HeisigObj(eachLines(1).toInt, "", "", character, "", textToInclude, List[Int](), "0001-01-01", 0)
        finalReturn.addOne(item)
      }
    }
    val finalResult: Array[HeisigObj] = finalReturn.toList.toArray
    return finalResult
  }

  def allCharsSaveToFiles(cedictTradDictionary: Map[String, String],
                          tzai: Map[String, Int],
                          cedictSimpDictionary: Map[String, String],
                          junda: Map[String, Int]) {
    val source = scala.io.Source.fromFile("heisigStories/raw/heisigraw.csv")
    val lines: List[String] = source.getLines().toList
    val nestedLines: List[List[String]] = lines.map(_.split('\t').toList)
    println(lines.length)

    /*case class HeisigObj (cardNumber: Int,
                     cardName: String,
                     backSide:String,
                     frontSide:String,
                     infoPrimary: String,
                     infoSecondary: String,
                     notableCards: List[Int],
                     dateOfLastReview: String,
                     repetitionValue: Int)*/
    //add cedict and frequency string

    val previousTrad: ListBuffer[HeisigObj] = new ListBuffer[HeisigObj]
    val tradCards: Array[HeisigObj] = createdCollectionFromNested(nestedLines, cedictTradDictionary, tzai, true)
    val tradHanzi: HeisigCollection = HeisigCollection("HeisigTraditionalHanzi",tradCards, previousTrad.toArray)
      /*nestedLines.filter(each => Try(each(0).toInt).isSuccess)
        .map(elem =>
          new HeisigObj(elem(0).toInt, "", "", elem(3), "", "", List[Int](), "0001-01-01", 0)
          ).toArray)*/

    val previousSimp: ListBuffer[HeisigObj] = new ListBuffer[HeisigObj]
    val simpCards: Array[HeisigObj] = createdCollectionFromNested(nestedLines, cedictSimpDictionary, junda, false)
    val simpHanzi: HeisigCollection = HeisigCollection("HeisigSimplifiedHanzi",simpCards,previousSimp.toArray)

      /*nestedLines.filter(each => Try(each(1).toInt).isSuccess)
        .map(elem =>
          new HeisigObj(elem(1).toInt,"", "", elem(4), "","", List[Int](), "0001-01-01", 0)).toArray)*/

    val previousKanji: ListBuffer[HeisigObj] = new ListBuffer[HeisigObj]
    val kanji: HeisigCollection = HeisigCollection("HeisigKanji",
      nestedLines.filter(each => Try(each(2).toInt).isSuccess)
        .map(elem =>
          new HeisigObj(elem(2).toInt,"", "", elem(5),"", "", List[Int](), "0001-01-01", 0)).toArray,
      previousKanji.toArray)

    val emptyTrad: ListBuffer[HeisigObj] = new ListBuffer[HeisigObj]
    val tradFinal: HeisigCollection = new HeisigCollection(tradHanzi.deckName, tradHanzi.cards.sortBy(_.cardNumber),emptyTrad.toArray)
    val emptySimp: ListBuffer[HeisigObj] = new ListBuffer[HeisigObj]
    val simpFinal: HeisigCollection = new HeisigCollection(simpHanzi.deckName, simpHanzi.cards.sortBy(_.cardNumber),emptySimp.toArray)
    val emptyKanji: ListBuffer[HeisigObj] = new ListBuffer[HeisigObj]
    val kanjiFinal: HeisigCollection = new HeisigCollection(kanji.deckName, kanji.cards.sortBy(_.cardNumber),emptyKanji.toArray)

    val tradJson: String = Encoder[HeisigCollection].apply(tradFinal).toString()
    val simpJson: String = Encoder[HeisigCollection].apply(simpFinal).toString()
    val kanjiJson: String = Encoder[HeisigCollection].apply(kanjiFinal).toString()

    Files.write(Paths.get("heisigtraditional.txt"), tradJson.getBytes(StandardCharsets.UTF_8))
    Files.write(Paths.get("heisigsimplified.txt"), simpJson.getBytes(StandardCharsets.UTF_8))
    Files.write(Paths.get("heisigkanji.txt"), kanjiJson.getBytes(StandardCharsets.UTF_8))

    val test = ""

  }

}
