import allheisigcharacters.{allChars, allCharsSaveToFiles}
import personalStoriesParseInput.{createListOfStoryArrays, linesToArr, listOfPrivateStories, saveListOfStoriesToFileTradHanzi, startOfCSVEntry}

import scala.collection.immutable.HashMap
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source

object Main {

  def createJundaAndTzai(filePath: String): Map[String, Int] = {
    val source = scala.io.Source.fromFile(filePath)
    val data: Array[String] = source.mkString("").split("")

    var hashmap = new mutable.HashMap[String, Int]()
    for(x: Int <- 0 to (data.length-1)){
      val each: String = data(x)
      hashmap.put(each, x)
    }
    val finalObject: Map[String, Int] = Map() ++ hashmap;
    return finalObject;
  }

  def createCedictMap(getTraditional: Boolean): Map[String, String] = {
    //val source = scala.io.Source.fromFile("heisigStories/dictionariesAndLists/cedict_ts.txt")//cedict20211029064558GMT
    val source = scala.io.Source.fromFile("heisigStories/dictionariesAndLists/cedict20211029064558GMT.u8")
    var data: Array[Array[String]] = source.getLines.map(_.split("\\s+")).toArray
      .drop(30)
    source.close
    var dyadArray: Array[(String, String)] = null
    if (getTraditional) {
      dyadArray = data.map(eachArr => {
        val firstitem: String = eachArr(0)
        val allChars: String = eachArr.mkString(" ")
        (firstitem, allChars)
      })
    }else {
      dyadArray = data.map(eachArr => {
        val firstitem: String = eachArr(1)
        val allChars: String = eachArr.mkString(" ")
        (firstitem, allChars)
      })
    }
    dyadArray = dyadArray.sortBy(_._1)
    //****

    //*********************
    //test if I have sorted the characters properly
    val indexOfDeInDyadArray: List[(String, String)] = if(dyadArray.filter(each => each._1.equals("的")).size > 0) (dyadArray.filter(each => each._1.equals("的")).toList) else null
    val indexOfFaInDyadArrayV1: List[(String, String)] = if (dyadArray.filter(each => each._1.equals("發")).size > 0) (dyadArray.filter(each => each._1.equals("發"))).toList else null
    val indexOfFaInDyadArrayV2: List[(String, String)] = if(dyadArray.filter(each => each._1.equals("发")).size > 0) (dyadArray.filter(each => each._1.equals("发"))).toList else null

    //******************

    var greatNestedBuffer: ListBuffer[ListBuffer[(String, String)]] = new ListBuffer[ListBuffer[(String, String)]]
    var tempListBuffer: ListBuffer[ (String, String)] = new ListBuffer[(String, String)]()
    for (itemNumber <- Range.inclusive(1, dyadArray.length-2)) {
      val previousItem: (String, String) = dyadArray(itemNumber-1)
      val currentItem: (String, String) = dyadArray(itemNumber)
      val nextItem: (String, String) = dyadArray(itemNumber+1)
      //if (currentItem._1.equals("发")) {
      //  val test = ""
      //}
      if (currentItem._1.equals("的")) {
        val test = "hello"
      }
      if (previousItem._1.equals(currentItem._1) && nextItem._1.equals(currentItem._1)) { // previous and current and next are the same
        tempListBuffer.addOne(currentItem)
      }else if (previousItem._1.equals(currentItem._1) && !nextItem._1.equals(currentItem._1)) { //previous and current are the same, but the next is a new one
        tempListBuffer.addOne(currentItem)
        greatNestedBuffer.addOne(tempListBuffer)
        tempListBuffer = new ListBuffer[(String, String)]()
      }else if (!previousItem._1.equals(currentItem._1) && nextItem._1.equals(currentItem._1)) { //previous was different, but the next is the same
        tempListBuffer = new ListBuffer[(String, String)]()
        tempListBuffer.addOne(currentItem)
      }else {                                                                       //previous was different and next is different
        tempListBuffer = new ListBuffer[(String, String)]()
        tempListBuffer.addOne(currentItem)
        greatNestedBuffer.addOne(tempListBuffer)
        tempListBuffer = new ListBuffer[(String, String)]()
      }
    }
    val reducedNestedList: List[(String, String)] = greatNestedBuffer.map(eachNested => {
      (eachNested(0)._1, eachNested.map(_._2).toList.mkString("\n").replaceAll("/", "\n"))
    }).toList

    var hashmap = new mutable.HashMap[String, String]()
    for(x: (String, String) <- reducedNestedList ){
      hashmap.put(x._1, x._2)
    }
    val finalObject: Map[String, String] = Map() ++ hashmap;
    return finalObject;
  }

  //http://www.edrdg.org/wiki/index.php/KANJIDIC_Project
  def readEdictJapaneseDictionary(): Map[String, String] = {
    val source = scala.io.Source.fromFile("heisigStories/dictionariesAndLists/kanjidic2.xml", "UTF-8")
    var data: Array[String] = source.getLines.toArray
    var data2: String = source.mkString("")

    val filterMeaningReadingAndLiteralL: Array[String] = data.filter(eachLine => {
        eachLine.startsWith("<reading r_type=\"ja") |
        eachLine.startsWith("<literal>") |
        eachLine.startsWith("<meaning>") |
        eachLine.startsWith("<stroke_count>")
    })
    val BigString: String = filterMeaningReadingAndLiteralL.mkString("\n")
    val eachChar: Array[String] = BigString.split("<literal>")
    val nestedList: Array[String] = eachChar.map(eachCharString => {
      eachCharString.toString.replaceAll("\\<.*?\\>", "")
    }).toArray

    //2021 10 27 kl. 23.06 - jeg har nu de rigtige linjer til at lave en hashmap, nu skal jeg bare tage tegnene ud saa jeg kan lave dem til keys
    val tupleArray: Array[(String, String)]  = nestedList.map(eachString => {
      (eachString.split("\n")(0), eachString)
    })

    var hashmap = new mutable.HashMap[String, String]()
    for(x: (String, String) <- tupleArray ){
      hashmap.put(x._1, x._2)
    }
    val finalObject: Map[String, String] = Map() ++ hashmap;
    return finalObject
  }

  def main(args: Array[String]): Unit = {
    println("hello chr")
    //save person stories (only traditional)
    //create a dictionary hashmap

    val junda: Map[String, Int] = createJundaAndTzai("heisigStories/dictionariesAndLists/testJunda.txt")
    val tzai: Map[String, Int] = createJundaAndTzai("heisigStories/dictionariesAndLists/testTzai.txt")

    val cedictTradDictionary: Map[String, String] = createCedictMap(true)
    val cedictSimpDictionary: Map[String, String] = createCedictMap(false)
    val edictJapaneseDictionary: Map[String, String] = readEdictJapaneseDictionary()

    val getDeCharacterTrad: String = if (cedictTradDictionary.contains("的"))  cedictTradDictionary("的") else "no result"
    val getDeCharacterSimp: String = if (cedictSimpDictionary.contains("的"))  cedictSimpDictionary("的") else "no result"
    val getFaCharacterTrad: String = if (cedictTradDictionary.contains("發"))  cedictTradDictionary("發") else "no result"
    val getFaCharacterSimp: String = if (cedictSimpDictionary.contains("发"))  cedictSimpDictionary("发") else "no result"

    val testTzai: String = if (tzai.contains("發")) tzai("發").toString else "no frequency"
    val testJunda: String = if (junda.contains("发")) junda("发").toString else "no frequency"

    println("dictionary hashmap created")
    val myStories: List[Array[String]] = createListOfStoryArrays()
    val traditionalChar = allChars()
    saveListOfStoriesToFileTradHanzi(myStories, traditionalChar, cedictTradDictionary, tzai)

    //save default character list
    allCharsSaveToFiles(cedictTradDictionary, tzai, cedictSimpDictionary, junda, edictJapaneseDictionary, false)
  }






}
