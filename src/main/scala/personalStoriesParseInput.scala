import scala.collection.mutable.ListBuffer
import io.circe.Encoder
import io.circe.Encoder.AsObject.importedAsObjectEncoder
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import upickle._
import java.nio.file.{Paths, Files}
import java.nio.charset.StandardCharsets

import scala.util.Try
import upickle.default.{macroRW, ReadWriter => RW}
import upickle.legacy.write

object personalStoriesParseInput {

  def createListOfStoryArrays(): List[Array[String]] = {
    val source = scala.io.Source.fromFile("heisigStories/raw/ChrLykke_stories_backupcopy.csv")
    val data: Array[Array[String]] = source.getLines.map(_.split("\t")).toArray
    source.close
    val singleArray: Array[String] = data.map(singleArr => singleArr.mkString(""))
    //val firstLine: Boolean = startOfCSVEntry(singleArray(1))
    val finalListOfString: List[String] = listOfPrivateStories(singleArray)
    val finalArr: List[Array[String]] = linesToArr(finalListOfString)
    return finalArr
  }

  def saveListOfStoriesToFileTradHanzi(privateStories: List[Array[String]], tradHeisig: HeisigCollection) {

    val newListOfHeisig: Array[HeisigObj] = tradHeisig.content.map(eachObj => {
      val story: String = getStoryByCharacter(eachObj.character, privateStories)
      HeisigObj(eachObj.number, eachObj.character, "", story, List[String](), eachObj.dateOfLastReview, eachObj.repetitionValue)
    })
    val returnCollection: HeisigCollection = HeisigCollection(tradHeisig.characterset, newListOfHeisig)
    val collToJson: String = Encoder[HeisigCollection].apply(returnCollection).toString()
    Files.write(Paths.get("NoKeyPersonalStories.txt"), collToJson.getBytes(StandardCharsets.UTF_8))


    val str = "ertert"

    /*
    val newListOfHeisig: Array[PersonalObj] = privateStories.map(eachArrayString => {
      val story: String = getStoryByCharacter(eachArrayString(0), privateStories)
      PersonalObj()
    })
    val returnCollection: HeisigCollection = HeisigCollection(tradHeisig.characterset, newListOfHeisig)
    val collToJson: String = Encoder[HeisigCollection].apply(returnCollection).toString()
    Files.write(Paths.get("shortPersonalStories.txt"), collToJson.getBytes(StandardCharsets.UTF_8))
    */
  }

  def getStoryByCharacter(character: String, privateStories: List[Array[String]]): String = {
    for (obj <- privateStories) {
      if (obj(0).equals(character)) {
        return obj(1)
      }
    }
    //println("there are no objects for this character: " + character)
    return ""
  }


  //this methods expects a raw line from a csv file in the form of a string.
  //if the string is the start of a csv entry, then return true, else false
  def startOfCSVEntry(rawArrayLine: String): Boolean ={
    val splitString: Array[String] = rawArrayLine.split(",")
    if (splitString.length < 6) { return false }

    val firstSubstring_frameNumber: String = splitString(0)
    val secondSubstring_kanji: String = splitString(1)
    val thirdSubstring_keyword: String = splitString(2)
    val forthSubstring_public: String = splitString(3)
    val fifthSubstring_lastEdited: String = splitString(4)
    val sixthSubstring_storyPart1: String = splitString(5)

    //test if first string is a digit
    val firstSubstringIsDigit: Boolean = firstSubstring_frameNumber.forall(_.isDigit)
    if (!firstSubstringIsDigit) { return false }

    //test if second string is a single kanji (might be a higher plane character)
    val hanziUnicodeValues: List[Int] = secondSubstring_kanji.map(eachChar => eachChar.toString.codePointAt(0)).toList//forall(_.getNumericValue)
    if (!(hanziUnicodeValues.length == 1) || (hanziUnicodeValues(0) < 11904)) { return false }

    //test if keyword is not empty
    if (thirdSubstring_keyword.length < 1) { return false }

    //test if public is a number, 0 or 1
    if (!forthSubstring_public.forall(_.isDigit) || forthSubstring_public.length != 1) {return false}

    //jeg tror det er nok at teste hertil

    return true
  }

  def linesToArr(input: List[String]): List[Array[String]] = {
    val endList: List[Array[String]] = input.map(elem => splitLineIntoArrayWithCharAndStory(elem))
    return endList
  }

  //this method expects a string of the following format:
  //9,九,"nine",0,2012-08-18 19:08:59,"as a primitive, this means a baseball bat (my-heisig keyword)."
  def splitLineIntoArrayWithCharAndStory(input: String): Array[String] = {
    val splitString: Array[String] = input.split(",")

    val kanji: String = splitString(1);
    splitString(0) = ""
    splitString(1) = ""
    splitString(2) = ""
    splitString(3) = ""
    splitString(4) = ""
    val story: String = splitString.mkString("");

    val newarray = Array(kanji, story);

    return newarray
    return null;
  }


  def listOfPrivateStories(singleArray: Array[String]): List[String] = {
    var listOfLists = new ListBuffer[List[String]]();
    var eachHeisigStory = new ListBuffer[String]
    for(line: String <- singleArray.tail){
      val thisisANewLine: Boolean = startOfCSVEntry(line)
      if (eachHeisigStory.length == 0 && thisisANewLine) {
        eachHeisigStory.addOne(line)
      } else if (thisisANewLine) {
        listOfLists.addOne(eachHeisigStory.toList)
        eachHeisigStory = new ListBuffer[String]
        eachHeisigStory.addOne(line)
      }else{
        eachHeisigStory.addOne(line)
      }
    }
    var finalList = listOfLists.toList
    var finalListOfString: List[String] = listOfLists.map(_.mkString("\n")).toList
    return finalListOfString
  }


}
