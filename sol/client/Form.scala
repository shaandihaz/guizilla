package guizilla.sol.client

import java.net.URLEncoder
import javafx.scene.layout.VBox
import scala.util.matching.Regex

/**
  * A form element of an HTMLPage
  *
  * @param url - The URL to send the form data
  * @param elements - The HTML elements contained in the form
  */
case class Form(val url: String, var elements: List[HTMLElement]) extends HTMLElement {
  /**
   * method that sets elements field
   * 
   * @param elms - the list of html elements to be set
   */
  def setElements(elms: List[HTMLElement]) {
    elements = elms
  }
  
  /**
   * method to render the elements in a form
   * 
   * @param box - the VBox to be passed on
   * @param browser - the GUIBrowser that is being used 
   * 
   * @return the VBox to be passed on
   */
   def render(box: VBox, browser: GUIBrowser): VBox = { 
    browser.renderPageHelper(elements, box) 
    box
  }
  
}
