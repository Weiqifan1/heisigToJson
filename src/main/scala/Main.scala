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
    val source = scala.io.Source.fromFile("heisigStories/dictionariesAndLists/cedict_ts.txt")
    var data: Array[Array[String]] = source.getLines.map(_.split("\\s+")).toArray
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
    dyadArray.sortBy(_._1)
    //****

    var greatNestedBuffer: ListBuffer[ListBuffer[(String, String)]] = new ListBuffer[ListBuffer[(String, String)]]
    var tempListBuffer: ListBuffer[ (String, String)] = new ListBuffer[(String, String)]()
    for (itemNumber <- Range.inclusive(1, dyadArray.length-2)) {
      val previousItem: (String, String) = dyadArray(itemNumber-1)
      val currentItem: (String, String) = dyadArray(itemNumber)
      if (currentItem._1.equals("的")) {
        val test = "hello"
      }

      val nextItem: (String, String) = dyadArray(itemNumber+1)
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
      (eachNested(0)._1, eachNested.map(_._2).toList.mkString("\n"))
    }).toList


    var hashmap = new mutable.HashMap[String, String]()
    for(x: (String, String) <- reducedNestedList ){
      hashmap.put(x._1, x._2)
    }
    val finalObject: Map[String, String] = Map() ++ hashmap;
    return finalObject;
  }

  def main(args: Array[String]): Unit = {
    println("hello chr")
    //save person stories (only traditional)
    //create a dictionary hashmap

    val junda: Map[String, Int] = createJundaAndTzai("heisigStories/dictionariesAndLists/testJunda.txt")
    val tzai: Map[String, Int] = createJundaAndTzai("heisigStories/dictionariesAndLists/testTzai.txt")

    val cedictTradDictionary: Map[String, String] = createCedictMap(true)
    val cedictSimpDictionary: Map[String, String] = createCedictMap(false)



    println("dictionary hashmap created")
    val myStories: List[Array[String]] = createListOfStoryArrays()
    val traditionalChar = allChars()
    saveListOfStoriesToFileTradHanzi(myStories, traditionalChar, cedictTradDictionary, tzai)

    //save default character list
    allCharsSaveToFiles(cedictTradDictionary, tzai, cedictSimpDictionary, junda)
  }






}
