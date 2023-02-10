package guizilla.sol.server.pages

import java.io.IOException
import java.net.Socket
import java.net.ServerSocket
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import scala.util.matching.Regex
import guizilla.src.Page
import java.net.URLDecoder

/**
 * Class that takes input from a listener and dispatches the information to the correct class and method
 * and the sends back the response
 */
class Dispatcher(listener: Listener) {
  
  /**
   * method to turn formData given by client into a map of field to name
   * 
   * @param formData = a string repreenting the data inputted in forms
   * @return a map from the name of the field to what was the input
   */
  def formDataToMap(formData: String): Map[String, String] = {
    var map = Map[String, String]()
    val pairsArr = formData.split("&")
    for (pair <- pairsArr) {
      val couple = pair.split("=")
      if (couple.size == 2) {
        map = map ++ Map(URLDecoder.decode(couple(0), "UTF-8") -> URLDecoder.decode(couple(1), "UTF-8")) 
      }
    }
    map
  }
  
  /**
   * method to create and call a class and method inside that class
   * 
   * @param URL - a string representing a class and/or method
   * @inputs: a map representing form data sent by a client server
   * @sessionID - a string representing a unique ID for this specific Page
   */
  def reflection(URL: String, inputs: Map[String, String], sessionID: String): (String, String) = {
    val splitURL = URL.split("/")
    var file: Option[String] = None
    var method: Option[String] = None
    if (splitURL.size == 2) {
      file = Some(splitURL(1))
    } else {
      file = Some(splitURL(1))
      method = Some(splitURL(2))
    }
    var index: Option[Page] = None
    var id = sessionID
    try {
      if (listener.sessionMap.contains(sessionID.toInt)) {
        val cloned = listener.sessionMap(sessionID.toInt).clone
        var randomInt = listener.RNG.nextInt
        while(listener.sessionMap.contains(randomInt)) {
          randomInt = listener.RNG.nextInt
        }
        index = Some(cloned)
        listener.sessionMap.put(randomInt, index.get)
        id = randomInt.toString
      } else {
        val scalaClass = file.get
        val indClass = Class.forName(s"""guizilla.sol.server.pages.$scalaClass""")
    
        index = Some(indClass.newInstance.asInstanceOf[Page])
    
        listener.sessionMap.put(sessionID.toInt, index.get)
      }
      if (method == None) {
        val defaultMethod = index.get.getClass.getMethod("defaultHandler", classOf[Map[String, String]], classOf[String])
    
        ("HTTP/1.0 200 OK", defaultMethod.invoke(index.get, inputs, id).asInstanceOf[String])
      } else {
        val stringMethod = method.get
        val scalaMethod = index.get.getClass.getMethod(stringMethod, classOf[Map[String, String]], classOf[String])
    
        ("HTTP/1.0 200 OK", scalaMethod.invoke(index.get, inputs, id).asInstanceOf[String])
      }
    } catch {
      case e: java.util.NoSuchElementException => {
        ("HTTP/1.0 400 Bad Request", "<html><body><p>I'm sorry, there was an error retrieving your input." +
        "<a href=\"/Index\">Return to Index</a></p></body></html>")
      }
      case x: IOException => {
        ("HTTP/1.0 400 Bad Request", "<html><body><p>I'm sorry, there was an error retrieving your input." +
        "<a href=\"/Index\">Return to Index</a></p></body></html>")
      }
      case y: java.lang.reflect.InvocationTargetException => {
        ("HTTP/1.0 400 Bad Request", "<html><body><p>I'm sorry, there was an error retrieving your input." +
        "<a href=\"/Index\">Return to Index</a></p></body></html>")
      }
      case z: ClassNotFoundException => {
        ("HTTP/1.0 404 Not Found", "<html><body><p>I'm sorry, that page does not exist" +
        "<a href=\"/Index\">Return to Index</a></p></body></html>")
      }
    }
    
    
  }
}