package guizilla.sol.client

import javafx.scene.layout.VBox

/**
  * A paragraph element of an HTML page
  *
  * @param elements - The HTML elements of the paragraph
  */
case class Paragraph(var elements: List[HTMLElement]) extends HTMLElement {
  
  /**
   * method to render the elements of a paragraph
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
