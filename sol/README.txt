Instructions for use:
Run the RunApplication file, as well as the Listener file. Then, to access the
pages in the server, type http://localhost/Index in the address bar of the GUI,
but make sure the Server is running on the same machine, or else use the host
name of the machine the server is running on. That will take you to a page that
has links, which you can click to go to other pages to Search a wiki data base
or do some MadLibs. You can also go to any server running on port 8080 by typing
in the right host server. You can hit the Quit Button to quit at any time or the
back button to go to the previous page at any time

Overview of Design:

For the GUI:
I grabbed all of the source files from SparkZilla to parse HTML, and also
grabbed the HTML element files as well. In each of the HTMLElement files is a
render method that renders the page. The RunApplication Class is responsible for
running the method, and calls on the controller, the GUIBrowser Class. This
class has a renderPage method, which, given a VBox and a List[HTMLElement],
renders all the HTMLelements in the list. When using the address bar or a url
from a link, the getRequest method is called, which requests the page given the
url associated with it and then calls the renderPage method to render the
result. When a submit button is pressed, the formData, its length, and the url
to send the information is sent to the postRequest method, which sends the
information to the server and then calls renderPage to render the result. When
the back button is pressed, the handleBackButtonAction method is called and it
deletes the last item in cache and renders the next page, and changes the host
to fit. If the quit button is pressed, the stage is closed.

Failed/Extra Features:
All features were implemented and no extra features were implemented.

Bugs:
There are no known bugs in the program.

Testing:
Here is the link to the video taken for testing purposes:
https://drive.google.com/file/d/1TZlA01arjZXe3ggXRHMDw2T6ppHzkf5f/view?usp=sharing


Testing the GUI:

Address Bar:

1. Type invalid host names
2. Type invalid pages
3. Go to thufir server/Index

Rendering Elements:

1. Click on the Pizza Parlor Link
2. Type small and hit submit
3. Put Pepperoni in the add toppint field and hit submit
4. Click order the pizza

Back Button:

Hit Back Button until at the home page

Testing the Server:

Run the server and connect to it in the GUI and go to the Index Page

Search Integration and Reflection and Cloning and errors:

1. Click Search
2. Type Nothing and hit Submit
3. Click return to index and and then click search again
4. Type "Bangladesh" in the query and hit Submit
5. Click on Panga and then hit Back twice
6. type "The United States of America" in the query and hit Submit
7. Click the last link

Other Pages:

1. Go to the Index
2. Click on MadLibs and Class Review
3. Type Math, then Harvard, then, Calculus and then hit submit
4. Type Orange, then Green, then the CIT, then Yellow, then Blue, then Bad and
hit Submit






Collaboration:
No collaborators, other than former partner Kenneth Neighbors
