package guizilla.sol.server.pages

import guizilla.src.Page
import scala.collection.mutable.HashMap

/**
 * Class that is representing a MadLibs game
 */
class ClassReview extends Page {
  
  /**
   * variables to store for the department, school, and course as Strings
   */
   private var department = ""
   private var school = ""
   private var course = ""
  
  override def defaultHandler(inputs: Map[String, String], sessionID: String): String = properNouns(inputs, sessionID)
  
  /**
   * method to output html for page
   * 
   * @param inputs: a map[String, String] holding any form data needed
   * @param sessionID - a String with the Unique ID for the page
   * 
   * @return a string of html
   */
  def properNouns(inputs: Map[String, String], sessionID: String): String = {
    "<html><body><p>Class Review</p>" +
    "<form method=\"post\" action=\"/id:" + sessionID + "/adjNoun\">" +
    "<p> Please enter a Department: </p>" +
    "<input type=\"text\" name=\"department\" />" +
    "<p> Please enter a school: </p>" +
    "<input type=\"text\" name=\"school\" />" +
    "<p> Please enter a course: </p>" +
    "<input type=\"text\" name=\"course\" />" +
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
  def adjNoun(inputs: Map[String, String], sessionID: String): String = {
      department = inputs("department")
      school = inputs("school")
      course = inputs("course")
      "<html><body><p>Class Review</p>" +
    "<form method=\"post\" action=\"/id:" + sessionID + "/displayClass\">" +
    "<p> Please enter adjective 1: </p>" +
    "<input type=\"text\" name=\"adj1\" />" +
    "<p> Please enter adjective 2: </p>" +
    "<input type=\"text\" name=\"adj2\" />" +
    "<p> Please enter a noun: </p>" +
    "<input type=\"text\" name=\"noun\" />" +
    "<p> Please enter adjective 3: </p>" +
    "<input type=\"text\" name=\"adj3\" />" +
    "<p> Please enter adjective 4: </p>" +
    "<input type=\"text\" name=\"adj4\" />" +
    "<p> Please enter adjective 5: </p>" +
    "<input type=\"text\" name=\"adj5\" />" +
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
  def displayClass(inputs: Map[String, String], sessionID: String): String = {
      val adj1 = inputs.get("adj1").get
      val adj2 = inputs.get("adj2").get
      val noun = inputs.get("noun").get
      val adj3 = inputs.get("adj3").get
      val adj4 = inputs.get("adj4").get
      val adj5 = inputs.get("adj5").get
      
      s"""<html><body><p>Class Review</p>" 
      <p> The $department at $school is known for its generally 
      $adj1 nature. $course, however, is seen as a very $adj2 $noun.   
      The TAs are $adj3, the professor is $adj4, and the difficulty is $adj5.  
      We wish every $noun was like $course. 
      </p><p><a href=\"/MadLibs\">Return to MadLibs</a></p></body></html>"""
  }
}