package guizilla.sol.server.pages

import guizilla.src.Page

/**
 * Class that contains all of the MadLibs pages
 */
class MadLibs extends Page {

  override def defaultHandler(inputs: Map[String, String], sessionID: String): String = {
    "<html><body><p>Welcome to MadLibs! Please Select a MadLib</p>" +
    "<p><a href=\"/ClassReview\"> Class Review</a></p>" + 
    "</body></html>"
      
  }
}