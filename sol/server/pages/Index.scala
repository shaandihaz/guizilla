package guizilla.sol.server.pages

import guizilla.src.Page

/**
 * Class that is the Index page containing all the pages taht can be accessed in the server
 */
class Index extends Page {
  
  
  override def defaultHandler(inputs: Map[String, String], sessionID: String): String = {
    "<html><body><p>These are the web pages:<a href=\"/MadLibs\">MadLibs</a><a href=\"/Search\">Search</a></p></body></html>"
  }

}