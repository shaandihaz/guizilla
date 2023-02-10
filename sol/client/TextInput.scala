package guizilla.sol.client

import javafx.scene.layout.VBox
import javafx.scene.control.TextField
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
/**
  * A text input element of an HTML page
  *
  * @param name - Name of the text input for communicating with the server
  * @param value - Value of the text input as given by the user
  */
case class TextInput(val name: String, var value: Option[String]) extends HTMLElement {  

  /**
   * method to render text input
   * 
   * @param box - the VBox to be passed on
   * 
   * @return the VBox to be passed on
   */
   def render(box: VBox): VBox = {
     val txtBox = new TextField()
     txtBox.textProperty.addListener(new ChangeListener[String]() {
       override def changed(ov: ObservableValue[_ <: String], oldMessage: String, newMessage: String) {
         txtBox.setText(newMessage)
         value = Some(newMessage)
       }
     })
     box.getChildren.add(txtBox)
    box
  }

}
