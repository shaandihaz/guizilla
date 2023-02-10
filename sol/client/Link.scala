package guizilla.sol.client

import scala.util.matching.Regex
import javafx.scene.layout.VBox
import javafx.scene.control.Hyperlink
import javafx.event.EventHandler
import javafx.event.ActionEvent

/**
  * A link element of an HTML page
  *
  * @param href - The URL of the link
  * @param text - The text to be rendered
  */
case class Link(href: String, text: PageText) extends HTMLElement {
  
  /**
   * method to render a link
   * 
   * @param box - the VBox to be passed on
   * @param browser - the GUIBrowser that is being used 
   * 
   * @return the VBox to be passed on
   */
   def render(box: VBox, browser: GUIBrowser): VBox = {
     val link = new Hyperlink(text.text)
     val event = new EventHandler[ActionEvent](){
       override def handle(a: ActionEvent): Unit = {
         val regex = new Regex("""//\w+/""")
         val host = regex.findFirstMatchIn(href)
         if (host == None) {
            browser.getRequest(None, href)
         } else {
           val path = href.split("//")
           browser.getRequest(Some(host.get.matched.filter(x => x != '/')), "/" + path.dropWhile(x => x != '/'))
           }
         }
       }
     link.setOnAction(event)
     box.getChildren.add(link)
     box
     }
}