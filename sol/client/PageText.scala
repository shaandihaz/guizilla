package guizilla.sol.client

import javafx.scene.layout.VBox
import javafx.scene.text.Text


/**
  * A text element of an HTML page
  *
  * @param text - The text
  */
case class PageText(val text: String) extends HTMLElement {
  
  /**
   * method to render PageText
   * 
   * @param box - the VBox it is adding the elements to
   */
   def render(box: VBox): VBox = {
    val textElement = new Text()
    textElement.setText(text)
    box.getChildren.add(textElement)
    box
  }
}