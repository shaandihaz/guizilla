package guizilla.sol.server.pages

import java.net.ServerSocket
import java.net.Socket
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import scala.util.matching.Regex
import guizilla.src.Page


class Listener {
  
  /**
   * a mutable map from a unique Integer to a specific Page
   */
  val sessionMap = scala.collection.mutable.Map[Int, Page]()
  
  /**
   * a regex used to parse
   */
  val regexNum = new Regex("""\s\d+\b""")
  val regexURL = new Regex("""/[\w/:-]+""")
  val regexID = new Regex("""[\d-]+""")
  val RNG = scala.util.Random
  var formLength: Option[String] = None
  var formData: Option[String] = None
  var classAndMethod: Option[String] = None
  
  /**
   * method to run a server socket continuously
   */
  def run () {
  val dispatch = new Dispatcher(this)
  val servSock = new ServerSocket(8080)
  while(true) {
   val clientSock = servSock.accept
   val iStream = clientSock.getInputStream
   val oStream = clientSock.getOutputStream
   val bRead = new BufferedReader(new InputStreamReader(iStream))
   val bWrite = new BufferedWriter(new OutputStreamWriter(oStream))
   request(bRead, bWrite, dispatch, clientSock)
  }
  }
  
  /**
   * method to receive a request from a client socket
   * 
   * @param bRead - a Buffered Reader to read the request
   * @param bWrite - a buffered writer to write a response
   * @param dispatch - a Dispatcher used to call the write class and method
   * @param clientSock - the client socket
   * 
   */
  def request(bRead: BufferedReader, bWrite: BufferedWriter, dispatch: Dispatcher, clientSock: Socket) {
    val firstLine = bRead.readLine()
   classAndMethod = Some(regexURL.findFirstMatchIn(firstLine).get.matched)
   if (classAndMethod == None) {
     val s = "HTTP/1.0 400 Bad Request"
     val response = "<html><body><p>Sorry, there was an error with your request</p>" +
     "<p><a href=\"/Index\">Return to Index</a></p></body></html>"
     sendResponse((s, response), bWrite)
     bWrite.close
   }
   if (firstLine.contains("GET")) {
     if (classAndMethod.get.contains("id:")) {
       val id = regexID.findFirstMatchIn(classAndMethod.get).get.matched
       val response = dispatch.reflection(classAndMethod.get, Map[String,String](), id)
       sendResponse(response, bWrite)
       bWrite.close
     } else { 
       var randomInt = RNG.nextInt
       while(sessionMap.contains(randomInt)) {
         randomInt = RNG.nextInt
       }
       val response = dispatch.reflection(classAndMethod.get, Map[String,String](), randomInt.toString)
       sendResponse(response, bWrite)
       bWrite.flush
       bWrite.close
       clientSock.close
     }
   } else if (firstLine.contains("POST")) {
     var continue = true
     var line = bRead.readLine()
    while (continue) {
      if (line == "") {
        if (formLength == None) {
          val s = "HTTP/1.0 500 Internal Server Error"
          val response = "<html><body><p>Sorry, there was an error with your request</p>" +
          "<p><a href=\"/Index\">Return to Index</a></p></body></html>"
          sendResponse((s, response), bWrite)
          bWrite.close
        } else {
          var data = ""
          while(data.length < formLength.get.toInt) {
            line = bRead.readLine
            data = data + line
          }
          formData = Some(data.take(formLength.get.toInt))
          continue = false
        }
      } else if (line.contains("Content-Length:")) {
        if (regexNum.findFirstMatchIn(line) == None) {
          val s = "HTTP/1.0 500 Internal Server Error"
          val response = "<html><body><p>Sorry, there was an error with your request</p>" +
          "<p><a href=\"/Index\">Return to Index</a></p></body></html>"
          sendResponse((s, response), bWrite)
          bWrite.close
        } else {
          formLength = Some(regexNum.findFirstMatchIn(line).get.matched.trim)
          line = bRead.readLine
        }
      } else {
        line = bRead.readLine
      }
    }
     if (classAndMethod.get.contains("id:")) {
       val id = regexID.findFirstMatchIn(classAndMethod.get).get.matched
       val response = dispatch.reflection(classAndMethod.get, dispatch.formDataToMap(formData.get), id)
       sendResponse(response, bWrite)
     } else { 
       var randomInt = RNG.nextInt
       while(sessionMap.contains(randomInt)) {
         randomInt = RNG.nextInt
       }
      val response = dispatch.reflection(classAndMethod.get, dispatch.formDataToMap(formData.get), randomInt.toString)
      sendResponse(response, bWrite)
     }
   } else {
     val s = "HTTP/1.0 500 Internal Server Error"
     val response = "<html><body><p>Sorry, there was an error with your request</p>" +
     "<p><a href=\"/Index\">Return to Index</a></p></body></html>"
     sendResponse((s, response), bWrite)
   }
  }
  
  /**
   * method to send a response back to a client server
   * 
   * @param tuple -  a (string, string) where the first element is a status line and 
   *            the second element is the html response
   * @param bWrite - a buffered writer used to write the response            
   * 
   */
  def sendResponse(tuple: (String, String), bWrite: BufferedWriter): Unit = {
    bWrite.write(tuple._1 + "\r\n")
    bWrite.write("Content-Type: text/html\r\n")
    bWrite.write("\r\n")
    bWrite.write(tuple._2)
    bWrite.flush
  }
  

}

object Listener extends App {
  val listener = new Listener()
  listener.run()
}