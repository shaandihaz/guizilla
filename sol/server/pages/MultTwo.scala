package guizilla.sol.server.pages

import guizilla.src.Page

class MultTwo extends Page{
  
  private var num1 = 0
  
  override def defaultHandler(inputs: Map[String, String], sessionID: String): String = multTwoNumbers(inputs, sessionID)
  
  def multTwoNumbers(inputs: Map[String, String], sessionID: String): String = {
    "<html><body><p>Multiply Two Numbers</p>" + 
    "<from method=\"post\" action=\"id:" + sessionID + "/secondNumber\">" + 
    "<p> Please enter the first number you would like to multiply: </p>" + 
    "<input type=\"text\" name=\"num1\" />" + 
    "<input type=\"submit\" value=\"submit\" />" +
    "</form></body></html>"
  }
  
  def secondNumber(inputs: Map[String, String], sessionID: String): String = inputs.get("num1") match {
    case Some(num) =>
      try {
        num1 = num.toInt
        "<html><body><p>The first number entered was " + num1 + "</p>" +
        "<from method=\"post\" action=\"/id:" + sessionID + "/displayResult\">" +
        "<p>Please enter the second number you would like to multiply: </p>" +
        "<input type=\"text\" name=\"num2\" />" +
        "<input type=\"submit\" value=\"submit\" />" +
        "</form></body></html>"
      } catch {
        case _: NumberFormatException =>
          "<html><body><p>I'm sorry you did not input a valid number." +
          "<a href=\"/Index\">Return to the Index</a></p></body></html>"
      }
    case None => {
      "<html><body><p>I'm sorry, there was an error retrieving your input." +
      "<a href=\"/Index\">Return to the Index</a></p></body></html>"
    }
  }
  
  def displayResult(inputs: Map[String, String], sessionID: String): String = inputs.get("num2") match {
    case Some(num) =>
      try {
        val num2 = num.toInt
        "<html><body>" + 
        "<p>" + num1 + " X " + num2 + " = " + (num1*num2) + "</p>" +
        "</body></html>"
      } catch {
        case _: NumberFormatException => {
          "<html><body><p>I'm sorry you did not input a valid number." +
          "<a href=\"/Index\">Return to the Index</a></p></body></html>"
        }
      }
    case None => {
      "<html><body><p>I'm sorry, there was an error retrieving your input." +
      "<a href=\"/Index\">Return to the Index</a></p></body></html>"
    }  
  }

}