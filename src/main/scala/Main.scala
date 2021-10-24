import allheisigcharacters.{allChars, allCharsSaveToFiles}
import personalStoriesParseInput.{createListOfStoryArrays, linesToArr, listOfPrivateStories, saveListOfStoriesToFileTradHanzi, startOfCSVEntry}

import scala.collection.mutable.ListBuffer
import scala.io.Source

object Main {

  /*
  var i=0
var length=0
val data=Source.fromFile(file)
for (line <- data.getLines) {
  val cols = line.split(",").map(_.trim)
  length = cols.length
  while(i<length){
    //println(cols(i))
    i=i+1
  }
  i=0
}
  */

  def main(args: Array[String]): Unit = {
    println("hello chr")
    //save person stories (only traditional)
    //create a dictionary hashmap

    println("dictionary hashmap created")
    val myStories: List[Array[String]] = createListOfStoryArrays()
    val traditionalChar = allChars()
    //saveListOfStoriesToFileTradHanzi(myStories, traditionalChar)

    //save default character list
    //allCharsSaveToFiles()



  }






}
