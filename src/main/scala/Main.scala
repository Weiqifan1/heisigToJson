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
    val data: Array[Array[String]] = source.getLines.map(_.split("\\s+")).toArray
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
    var hashmap = new mutable.HashMap[String, String]()
    for(x: (String, String) <- dyadArray ){
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
