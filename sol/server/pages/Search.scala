package guizilla.sol.server.pages

import guizilla.src.Page
import java.net.Socket
import java.net.ServerSocket
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import scala.util.matching.Regex
import java.io.IOException

/**
 * Class tpo implement search integration
 */
class Search extends Page {

  /**
   * a regex needed to parse response
   */
  private val regex = new Regex("""\s[^\d]+[\t\n]""")
  
  /**
   * a list of titles
   */
  private var titleList = List[String]()
  
  /**
   * the query given
   */
  private var query: Option[String] = None
    
  override def defaultHandler(inputs: Map[String, String], sessionID: String): String = {
    giveQuery(inputs, sessionID)
  }
  
  /**
   * method to output html for page
   * 
   * @param inputs: a map[String, String] holding any form data needed
   * @param sessionID - a String with the Unique ID for the page
   * 
   * @return a string of html
   */
  def giveQuery(inputs: Map[String, String], sessionID: String): String = {
    "<html><body><p>Search</p>" +
    "<form method=\"post\" action=\"/id:" + sessionID + "/displayResults\">" +
    "<p> What's your query? </p>" +
    "<input type=\"text\" name=\"query\" />" +
    "<input type=\"submit\" value=\"submit\" />" +
    "</form></body></html>"
  }
  
  /**
   * method to output html for page
   * 
   * @param inputs: a map[String, String] holding any form data needed
   * @param sessionID - a String with the Unique ID for the page
   * 
   * @return a string of html
   */
  def displayResults(inputs: Map[String, String], sessionID: String): String = {
      query = Some(inputs("query"))
      val freeQuery = query.get
      val sock = new Socket("eckert", 8081) 
      val iStream = sock.getInputStream
      val oStream = sock.getOutputStream
      val bRead = new BufferedReader(new InputStreamReader(iStream))
      val bWrite = new BufferedWriter(new OutputStreamWriter(oStream))
      bWrite.write(s"""REQUEST\t$freeQuery\n""")
      bWrite.flush
      val response = bRead.readLine
      bWrite.close
      bRead.close
      sock.close
      val resArr = regex.findAllMatchIn(response)
      for (elt <- resArr) {
        if (!elt.matched.contains("RESPONSE")) {
          titleList = titleList:::List(elt.matched.trim)
        }
      }
      var links = ""
      for (i <- 0 to titleList.size - 1) {
        val title = titleList.apply(i)
        links = links + s"""<a href="/id:$sessionID/title$i">$title</a>"""
      }
      s"""<html><body><p> Here are the Results: $links</p></body></html>"""
  }
  
  /**
   * method to grab text of a page given a title
   * 
   * @param inputs: a map[String, String] holding any form data needed
   * @param sessionID - a String with the Unique ID for the page
   * 
   * @return the string of text associated with the title
   */
  def title0(inputs: Map[String, String], sessionID: String): String = {
    val title = titleList.apply(0)
    val sock = new Socket("eckert", 8082) 
    val iStream = sock.getInputStream
    val oStream = sock.getOutputStream
    val bRead = new BufferedReader(new InputStreamReader(iStream))
    val bWrite = new BufferedWriter(new OutputStreamWriter(oStream))
    bWrite.write(title + "\n")
    bWrite.flush
    var continue = true
    var page = ""
    var intChar = bRead.read()
    while(intChar != -1) {
      val char = intChar.toChar
      page = page + char
      intChar = bRead.read
    }
    s"""<html><body><p>$page</p></body></html>"""
  }
  
  /**
   * method to grab text of a page given a title
   * 
   * @param inputs: a map[String, String] holding any form data needed
   * @param sessionID - a String with the Unique ID for the page
   * 
   * @return the string of text associated with the title
   */
  def title1(inputs: Map[String, String], sessionID: String): String = {
    val title = titleList.apply(1)
    val sock = new Socket("eckert", 8082) 
    val iStream = sock.getInputStream
    val oStream = sock.getOutputStream
    val bRead = new BufferedReader(new InputStreamReader(iStream))
    val bWrite = new BufferedWriter(new OutputStreamWriter(oStream))
    bWrite.write(title + "\n")
    bWrite.flush
    var continue = true
    var page = ""
    var intChar = bRead.read()
    while(intChar != -1) {
      val char = intChar.toChar
      page = page + char
      intChar = bRead.read
    }
    s"""<html><body><p>$page</p></body></html>"""
  }
  
  /**
   * method to grab text of a page given a title
   * 
   * @param inputs: a map[String, String] holding any form data needed
   * @param sessionID - a String with the Unique ID for the page
   * 
   * @return the string of text associated with the title
   */
  def title2(inputs: Map[String, String], sessionID: String): String = {
    val title = titleList.apply(2)
    val sock = new Socket("eckert", 8082) 
    val iStream = sock.getInputStream
    val oStream = sock.getOutputStream
    val bRead = new BufferedReader(new InputStreamReader(iStream))
    val bWrite = new BufferedWriter(new OutputStreamWriter(oStream))
    bWrite.write(title + "\n")
    bWrite.flush
    var continue = true
    var page = ""
    var intChar = bRead.read()
    while(intChar != -1) {
      val char = intChar.toChar
      page = page + char
      intChar = bRead.read
    }
    s"""<html><body><p>$page</p></body></html>"""
  }
  
  /**
   * method to grab text of a page given a title
   * 
   * @param inputs: a map[String, String] holding any form data needed
   * @param sessionID - a String with the Unique ID for the page
   * 
   * @return the string of text associated with the title
   */
  def title3(inputs: Map[String, String], sessionID: String): String = {
    val title = titleList.apply(3)
    val sock = new Socket("eckert", 8082) 
    val iStream = sock.getInputStream
    val oStream = sock.getOutputStream
    val bRead = new BufferedReader(new InputStreamReader(iStream))
    val bWrite = new BufferedWriter(new OutputStreamWriter(oStream))
    bWrite.write(title + "\n")
    bWrite.flush
    var continue = true
    var page = ""
    var intChar = bRead.read()
    while(intChar != -1) {
      val char = intChar.toChar
      page = page + char
      intChar = bRead.read
    }
    s"""<html><body><p>$page</p></body></html>"""
  }
  
  /**
   * method to grab text of a page given a title
   * 
   * @param inputs: a map[String, String] holding any form data needed
   * @param sessionID - a String with the Unique ID for the page
   * 
   * @return the string of text associated with the title
   */
  def title4(inputs: Map[String, String], sessionID: String): String = {
    val title = titleList.apply(4)
    val sock = new Socket("eckert", 8082) 
    val iStream = sock.getInputStream
    val oStream = sock.getOutputStream
    val bRead = new BufferedReader(new InputStreamReader(iStream))
    val bWrite = new BufferedWriter(new OutputStreamWriter(oStream))
    bWrite.write(title + "\n")
    bWrite.flush
    var continue = true
    var page = ""
    var intChar = bRead.read()
    while(intChar != -1) {
      val char = intChar.toChar
      page = page + char
      intChar = bRead.read
    }
    s"""<html><body><p>$page</p></body></html>"""
  }
  
  /**
   * method to grab text of a page given a title
   * 
   * @param inputs: a map[String, String] holding any form data needed
   * @param sessionID - a String with the Unique ID for the page
   * 
   * @return the string of text associated with the title
   */
  def title5(inputs: Map[String, String], sessionID: String): String = {
    val title = titleList.apply(5)
    val sock = new Socket("eckert", 8082) 
    val iStream = sock.getInputStream
    val oStream = sock.getOutputStream
    val bRead = new BufferedReader(new InputStreamReader(iStream))
    val bWrite = new BufferedWriter(new OutputStreamWriter(oStream))
    bWrite.write(title + "\n")
    bWrite.flush
    var continue = true
    var page = ""
    var intChar = bRead.read()
    while(intChar != -1) {
      val char = intChar.toChar
      page = page + char
      intChar = bRead.read
    }
    s"""<html><body><p>$page</p></body></html>"""
  }
  
  /**
   * method to grab text of a page given a title
   * 
   * @param inputs: a map[String, String] holding any form data needed
   * @param sessionID - a String with the Unique ID for the page
   * 
   * @return the string of text associated with the title
   */
  def title6(inputs: Map[String, String], sessionID: String): String = {
    val title = titleList.apply(6)
    val sock = new Socket("eckert", 8082) 
    val iStream = sock.getInputStream
    val oStream = sock.getOutputStream
    val bRead = new BufferedReader(new InputStreamReader(iStream))
    val bWrite = new BufferedWriter(new OutputStreamWriter(oStream))
    bWrite.write(title + "\n")
    bWrite.flush
    var continue = true
    var page = ""
    var intChar = bRead.read()
    while(intChar != -1) {
      val char = intChar.toChar
      page = page + char
      intChar = bRead.read
    }
    s"""<html><body><p>$page</p></body></html>"""
  }
  
  /**
   * method to grab text of a page given a title
   * 
   * @param inputs: a map[String, String] holding any form data needed
   * @param sessionID - a String with the Unique ID for the page
   * 
   * @return the string of text associated with the title
   */
  def title7(inputs: Map[String, String], sessionID: String): String = {
    val title = titleList.apply(7)
    val sock = new Socket("eckert", 8082) 
    val iStream = sock.getInputStream
    val oStream = sock.getOutputStream
    val bRead = new BufferedReader(new InputStreamReader(iStream))
    val bWrite = new BufferedWriter(new OutputStreamWriter(oStream))
    bWrite.write(title + "\n")
    bWrite.flush
    var continue = true
    var page = ""
    var intChar = bRead.read()
    while(intChar != -1) {
      val char = intChar.toChar
      page = page + char
      intChar = bRead.read
    }
    s"""<html><body><p>$page</p></body></html>"""
  }
  
  /**
   * method to grab text of a page given a title
   * 
   * @param inputs: a map[String, String] holding any form data needed
   * @param sessionID - a String with the Unique ID for the page
   * 
   * @return the string of text associated with the title
   */
  def title8(inputs: Map[String, String], sessionID: String): String = {
    val title = titleList.apply(8)
    val sock = new Socket("eckert", 8082) 
    val iStream = sock.getInputStream
    val oStream = sock.getOutputStream
    val bRead = new BufferedReader(new InputStreamReader(iStream))
    val bWrite = new BufferedWriter(new OutputStreamWriter(oStream))
    bWrite.write(title + "\n")
    bWrite.flush
    var continue = true
    var page = ""
    var intChar = bRead.read()
    while(intChar != -1) {
      val char = intChar.toChar
      page = page + char
      intChar = bRead.read
    }
    s"""<html><body><p>$page</p></body></html>"""
  }
  
  /**
   * method to grab text of a page given a title
   * 
   * @param inputs: a map[String, String] holding any form data needed
   * @param sessionID - a String with the Unique ID for the page
   * 
   * @return the string of text associated with the title
   */
  def title9(inputs: Map[String, String], sessionID: String): String = {
    val title = titleList.apply(9)
    val sock = new Socket("eckert", 8082) 
    val iStream = sock.getInputStream
    val oStream = sock.getOutputStream
    val bRead = new BufferedReader(new InputStreamReader(iStream))
    val bWrite = new BufferedWriter(new OutputStreamWriter(oStream))
    bWrite.write(title + "\n")
    bWrite.flush
    var continue = true
    var page = ""
    var intChar = bRead.read()
    while(intChar != -1) {
      val char = intChar.toChar
      page = page + char
      intChar = bRead.read
    }
    s"""<html><body><p>$page</p></body></html>"""
  }
}  