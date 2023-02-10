package guizilla.sol.client

import java.net.URLEncoder
import scala.util.matching.Regex
import javafx.scene.layout.VBox
import javafx.scene.control.Button
import javafx.event.EventHandler
import javafx.event.ActionEvent

/**
  * A submit button for a form
  *
  * @param form - The form that contains this submit button
  */
case class SubmitInput(form: Form) extends HTMLElement {

  
  /**
   * method to render a submit button
   * 
   * @param box - the VBox to be passed on
   * @param browser - the GUIBrowser that is being used 
   * 
   * @return the VBox to be passed on
   */
   def render(box: VBox, browser: GUIBrowser): VBox = {
     val submit = new Button("Submit")
     val eventHandler = new EventHandler[ActionEvent]() {
       override def handle(event: ActionEvent) = {
         val regex = new Regex("""//\w+/""")
         val host = regex.findFirstMatchIn(form.url)
         val data = createFormData(form.elements).dropRight(1)
         println(data)
         if (host == None) {
           browser.postRequest(None, form.url, data, data.length)
         } else {
           val path = form.url.split("//")
           browser.postRequest(Some(host.get.matched.filter(x => x != '/')), "/" + path.dropWhile(x => x != '/'), data, data.length())
         }
       }
     }
     submit.setOnAction(eventHandler)
     box.getChildren.add(submit)
    /*if (button == None) {
      button = Some(counter)
      System.out.println("[Submit][" + counter + "]")
    } else {
      System.out.println("[Submit][" + button.get + "]")
    }*/
    box
  }
  
  /**
   * method to create the form data from its elements
   * 
   * @param hList - the list of elements its grabbing the form data from
   * 
   * @return a string representing the form data 
   */
  private def createFormData(hList: List[HTMLElement]): String = hList match {
    case Nil => ""
    case (x: TextInput)::tail => x.name + "=" + URLEncoder.encode(x.value.getOrElse(""), "UTF-8") + "&" + createFormData(tail)
    case _::tail => createFormData(tail)
  }
}