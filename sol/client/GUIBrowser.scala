package guizilla.sol.client

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.scene.control.TextField
import javafx.scene.text.Text
import javafx.stage.Stage
import guizilla.src.parser.HTMLParser
import guizilla.src.HTMLTokenizer
import guizilla.src.parser.ParseException
import java.net.Socket
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import scala.util.matching.Regex

/**
 * Class responsible for handling browser navigation.
 * TODO: extend Browser class that was written for Sparkzilla, i.e.
 * where your code dealt with networking and communicating with server.
 */
class GUIBrowser {

  /**
   * hostname - the current host
   */
   private var hostname: Option[String] = None
  
    /**
   * cache - the tuple of past pages and hostnames
   */
  private var cache = List[(Option[String],List[HTMLElement])]()

  @FXML protected var gPane: GridPane = null
  @FXML protected var urlBar: TextField = null
  @FXML var box: VBox = new VBox()

  private var stage: Stage = null
  private var urlText: String = null

  /**
    * Handles the pressing of the submit button on the main GUI page.
    */

  @FXML def handleQuitButtonAction(event: ActionEvent) {
    stage.close()
  }

  /**
    * Handles the pressing of the back button on the main GUI page.
    */
  @FXML def handleBackButtonAction(event: ActionEvent) {
    if (cache.size ==1 | cache.size == 0) {
          renderHomePage()
        } else {
          cache = cache.drop(1)
          hostname = cache.head._1
          renderPage(cache.head._2)
        }
  }

  /**
   * Handles submitting URL button action.
   */
  @FXML def handleSubmitButtonAction(event: ActionEvent) {
    urlText = urlBar.getText
    val regex = new Regex("""//\w+/""")
    val host = regex.findFirstMatchIn(urlText)
    if (host == None) {
      if (hostname == None) {
        if (cache.isEmpty) {
          renderHomePage
        } else {
          renderError("Invalid URL")
        }
      } else if (urlText == "") {
        if (cache.isEmpty) {
          renderHomePage
        } else {
          renderError("Invalid URL")
        }
      } else if (urlText.charAt(0) != '/') {
        if (cache.isEmpty) {
          renderHomePage
        } else {
          renderError("Invalid URL")
        }
      } else {
        renderError("Invalid URL")
        }
      } else {
        val url = urlText.split("//")
        hostname = Some((host.get.matched.filter(x => x != '/')))
        getRequest(None, url(1).dropWhile(x => x != '/'))
      }
  }

  /**
    * Sets the stage field of the controller to the given stage.
    *
    * @param stage The stage
    */
  def setStage(stage: Stage) {
    this.stage = stage
  }

  /**
   * method to help render a page
   * 
   * @param hList - the list[HTMLElement] to be rendered
   * @param box - the VBox to add the elements to 
   * 
   * @return the VBox that was changed
   */
  def renderPageHelper(hList: List[HTMLElement], box: VBox): VBox = hList match {
   case Nil => box
   case (x: Link)::tail =>  
     renderPageHelper(tail, x.render(box, this))
   case (x: SubmitInput)::tail => 
     renderPageHelper(tail, x.render(box, this))
   case (x: TextInput)::tail =>
     renderPageHelper(tail, x.render(box) )
   case (x: Paragraph)::tail =>  
     renderPageHelper(tail, x.render(box, this))
   case (x: PageText)::tail =>
     renderPageHelper(tail, x.render(box))
   case (x: Form)::tail =>
     renderPageHelper(tail, x.render(box, this))
 }
  
  /**
   * method to render a page and clear the box
   * 
   * @param hList - the List[HTMLElement] to render
   */
  private def renderPage(hList: List[HTMLElement]): Unit = {
    box.getChildren.clear
    box = renderPageHelper(hList: List[HTMLElement], box)
  }
  
  /**
   * method to render error pages
   * 
   * @param message - a string that is the error message to render
   */
  private def renderError(message: String) = {
    val errorPage = List[HTMLElement](Paragraph(List[HTMLElement](PageText(message))))
    renderPage(errorPage)
  }
  
  /**
   * method to render the home page
   *
   */
  def renderHomePage(): Unit = {
    cache = List[(Option[String],List[HTMLElement])]()
    val homePage = List[HTMLElement](Paragraph(List[HTMLElement](PageText("Welcome to Guizilla!"))))
    renderPage(homePage)
  }
  
  /** Parses the input from the server into a list of HTMLElements.
    * @param inputFromServer- BufferedReader containing HTML from server
    * @returns hierarchical list of the HTMLElements. See the documentation
    *   and view the sol code for the specific composition of each HTMLElement
    *   within the list.
    */
  private def getHTMLElementList(inputFromServer: BufferedReader): List[HTMLElement] = {
    val parser = new HTMLParser(new HTMLTokenizer(inputFromServer))
    return parser.parse().toHTML
  }
  
  /**
    * method to process a GET request to a server and print out the 
    * response webpage
    * 
    * @param host - the host server to put it on, if its none, use the current hostname
    * @param url - the url for the server request
    */
    def getRequest(host: Option[String], url: String): Unit = {
     var actualHost: Option[String] = None
     if (host == None) {
      actualHost = hostname
     } else {
      actualHost = host 
     }
     try {
       val sock = new Socket(actualHost.get, 8080) 
       val iStream = sock.getInputStream
       val oStream = sock.getOutputStream
       val bRead = new BufferedReader(new InputStreamReader(iStream))
       val bWrite = new BufferedWriter(new OutputStreamWriter(oStream))
       bWrite.write("GET " + url + " HTTP/1.0\r\n")
       bWrite.write("Connection: close\r\n")
       bWrite.write("User-Agent: Guizilla/1.0\r\n")
       bWrite.write("\r\n")
       bWrite.flush
       // maybe write some thing like connecting...
       var parseStream = bRead.readLine()
       while (parseStream != "") {
         if (parseStream.contains("OK")) {

         }
         parseStream = bRead.readLine()
         }
       val page = getHTMLElementList(bRead)
       sock.shutdownInput
       sock.shutdownOutput
       sock.close
       cache = (actualHost, page)::cache
       urlBar.setText("http://" + actualHost.get + url)
       renderPage(cache.head._2)
     } catch {
       case e: java.net.UnknownHostException => {
         if (cache.isEmpty) {
           renderHomePage
         } else {
           renderError("Unkown Host")
         }
       }
       case e: java.net.ConnectException => {
         if (cache.isEmpty) {
           renderHomePage
         } else {
           renderError("Cannot connect to host")
         }
       }
       case e: ParseException => {
         if (cache.isEmpty) {
           renderHomePage
         } else {
           renderError("Bad HTML from Server")
         }
       }
       case e: java.io.IOException => {
         if (cache.isEmpty) {
           renderHomePage
         } else {
           renderError("Socket Error")
         }
       }
     }
   }
   
   /**
    * method to process a post request to a server and print out the response
    * 
    * @param host - the host server as an Option[String] to put it on, if its none, use the current hostname
    * @param url - a string representing the url for the server request
    * @param formData - a string of the form data needed to process the request
    * @param contentLength - an Int of the length of the formData string 
    */
    def postRequest(host: Option[String], url: String, formData: String, contentLength: Int): Unit = {
     var actualHost: Option[String] = None
     if (host == None) {
      actualHost = hostname
     } else {
      actualHost = host 
     }
     try {
       val sock = new Socket(actualHost.get, 8080) 
       val iStream = sock.getInputStream
       val oStream = sock.getOutputStream
       val bRead = new BufferedReader(new InputStreamReader(iStream))
       val bWrite = new BufferedWriter(new OutputStreamWriter(oStream))
       bWrite.write("POST " + url + " HTTP/1.0\r\n")
       bWrite.write("Connection: close\r\n")
       bWrite.write("User-Agent: Guizilla/1.0\r\n")
       bWrite.write("Content-Type: application/x-www-form-urlencoded\r\n")
       bWrite.write(s"Content-Length: $contentLength\r\n")
       bWrite.write("\r\n")
       bWrite.write(formData + "\r\n")
       bWrite.flush
       // maybe write some thing like connecting...
       var parseStream = bRead.readLine()
       while (parseStream != "") {
         parseStream = bRead.readLine()
         }
       val page = getHTMLElementList(bRead) 
       sock.shutdownInput
       sock.shutdownOutput
       sock.close
       cache = (actualHost, page)::cache
       renderPage(cache.head._2)
       } catch {
         case e: java.net.UnknownHostException => {
           if (cache.isEmpty) {
             renderHomePage
           } else {
             renderError("Unkown Host")
           }
           }
         case e: java.net.ConnectException => {
           if (cache.isEmpty) {
             renderHomePage
           } else {
             renderError("Cannot connect to host")
           }
           }
         case e: ParseException => {
         if (cache.isEmpty) {
           renderHomePage
         } else {
           renderError("Bad HTML from Server")
         }
         }
       case e: java.io.IOException => {
         if (cache.isEmpty) {
           renderHomePage
         } else {
           renderError("Socket Error")
         }
       }
         }
   }
}
