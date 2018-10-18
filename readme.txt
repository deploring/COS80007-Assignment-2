- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Index
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

This file contains:
    - Instructions to run the assignment
	- Team Member's Information
	- Features Completed
	- Expected Marks
	- Helped received from outside sources

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Team Member's Information
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

Name:		Bradley Chick
Student ID:	101 626 151 
Subject ID:	COS80007
		Undergraduate

Name:		Joshua Skinner
Student ID:	101 601 828
Subject ID:	COS80007
		Undergraduate

Name:		Keagan Foster
Student ID:	101 609 822
Subject ID:	COS80007
		Undergraduate

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Instructions to run the assignment
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

1. Download and Install "Maven". Follow their instructions for installation.
    - http://maven.apache.org/install.html
2. Navigate to the project's root directory in a command prompt/terminal.
3. Type "mvn clean install".
4. Wait a while, especially if it is your first time compiling something with Maven.
5. You should receive a "reactor summary" showing the four different modules successfully compiling.

* TO RUN SERVER PROGRAM*
6.1. To run the server program, navigate to the AJASS2-Server folder in a command prompt/terminal.
7.1. Run the server using 'java -jar AJASS2-Server.jar'. It will terminate itself.
8.1. Open the 'server.settings' file using a text editor, like Notepad++.
9.1. Enter the MySQL server settings, along with a schema name. You may also change the server's port.
10.1. Re-run the server using 'java -jar AJASS2-Server.jar'. It should run without terminating this time.
10.1x. Any obvious user-caused errors are shown in console.

* TO RUN CLIENT PROGRAM *
6.2. To run the client program, navigate to the AJASS2-Client folder in Explorer/Finder.
7.2. Double-click the JAR file to run it, or run it from a command prompt/terminal for verbose logs.
8.2. The program will have shown an error and closed. Open the 'client.settings' file using a text editor.
9.2. Enter the server hostname as well as the server port. The server must be running.
10.2. Double click the JAR file again to run it (or open it in command prompt/terminal).
10.2x. There should not be any errors past this point.

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Features Completed
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

The standalone contains all of the following features. The Client/Server
also contains all of these features.

-------------------------------------------------------------------------
	Receive Orders
-------------------------------------------------------------------------
Can receive a customer's order by asking for:
- Customer Name
- Table Number
- Meal Type (Breakfast, Lunch, Dinner)
- Food
- Beverage

The user can select food without a beverage and vice-versa.

A reminder message will be displayed if the customer name, table number,
meal type, food and beverage items remain blank and the 'Enter Data'
button is pressed.

Menu selection boxes become enabled with all menu items after the customer's
name, table number, and meal type are filled in.

Food and Beverage items are loaded in through the given csv file.

If a meal type and at least one menu item is chosen, then the 'Display 
Choices' button can be clicked to display nutritional information. 
Including the item name, energy, protein, carbohydrates, total fat, fibre,
and price.

The order can be displayed by selected 'Display Order'.

-------------------------------------------------------------------------
	Preparing Orders
-------------------------------------------------------------------------

Orders can be prepared by a chef. Once the order is placed it is in a 
'waiting' state for the chef to start preparation. The chef can select the
order from the waiting state and the 'Prepare' button become enabled; and
upon preparation will lead the order to enter a 'served' state.

-------------------------------------------------------------------------
	Billing Orders
-------------------------------------------------------------------------

Orders in the 'served' state can be billed. Validation is applied so that
only the served order cna be billed. 

A bill receipt with nutritional details is displayed.

-------------------------------------------------------------------------
	Programmatic/Other Features
-------------------------------------------------------------------------

All non-visible and programmatic features are explained in the report.

See: Report.pdf

-------------------------------------------------------------------------
	Improvements
-------------------------------------------------------------------------

Improvements are explained in the Report.

See: Report.pdf

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Estimated marks
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

-------------------------------------------------------------------------

1. Implement the Swing/JavaFX based GUI for the standalone prototype 
   version of the order reception, preparation, and billing with all 
   functionalities.  

Estimated marks:  55 / 60

Reasoning:
We neglected to implement multiple beverages. All other aspects and
requirements needed to fully implement the standalone prototype have
been added however. This still deserves high marks as it is a small
requirement that was not emphasised heavily in the requirements.

See: Report.pdf

-------------------------------------------------------------------------

2. Show any four uses of lambdas and streams in validations 

Estimated marks: 0  / 10

Reasoning:
Lambdas and streams were not used because they were deemed illogical
to use. Lambdas/streams could have been used to carry out the actions
after validation was performed, but there was no actual use for them
to validate anything. All validations took the form of checking the
value of something that had already been pre-created.

-------------------------------------------------------------------------
3. Show any four uses of lambdas in event handling

Estimated marks: 10 / 10

Reasoning:
There has been at least four uses of lambdas in event handling in this
project. Each button in ButtonView uses a lambda to handle an OnClick
listener.

ButtonView:78
ButtonView:145
ButtonView:162
ButtonView:182

See: Report.pdf

-------------------------------------------------------------------------
4. Show any four uses of collections and/or generic methods

Estimated marks:  2.5 / 10

Reasoning:
There was a single use of the Collections method, to generate a thread-safe list
to store the list of Client Connections in, as it is accessed by a different thread.

-------------------------------------------------------------------------
5. Programmatically creating and processing database and tables 
   in mysql/derby

Estimated marks: 10 / 10

Reasoning: The JDBC library has been used to programmatically create the
database schema and tables. It has also been used to process menu item and
order information as explained in Report.pdf.

See: Report.pdf

-------------------------------------------------------------------------
6. Programmatically loading the food and beverage details from 
   Assignment2Data.csv into the Food and Beverage combo boxes. 

Estimated marks:  10 / 10

Reasoning:
Food and beverage details from Assignment2Data.csv have been programmatically
loaded into Food and Beverage combo boxes. All code responsible for this is
managed from MenuItems. This class stores all the MenuItems, so to populate
the combo boxes they are taken from here.

AJASS2-Standalone -> MenuItems
AJASS2-Server -> MenuItems

-------------------------------------------------------------------------
7. Relate the implementation of the standalone version with Model 
   Controller View (MVC)

Estimated marks:  30 / 30

Reasoning:
The concept of MVC has been followed in this application. Justification
regarding Model View Controller concept is further explained in Report.pdf.
Naming conventions have also been used to specify files that are a model,
view or controller.

See: Report.pdf

-------------------------------------------------------------------------
8. Implement the Swing/JavaFX based GUI for the client server prototype 
   version of the order reception, preparation, and billing with all 
   functionalities.

Estimated marks:  58 / 60

Reasoning:
All functionalities of the client server prototype version of the order
reception, preparation, and billing has been implemented. Implementation
is explained in Report.pdf.

We think that the client/server prototype does not deserve to lose as many
marks despite its inability to order multiple beverages. This is because we
neglected to add this requirement to the standalone prototype long before
we made the client/server prototype. The client/server prototype has been
fully implemented to function exactly like the standalone, except with a
central server mediating the handling of data.

See: Report.pdf

-------------------------------------------------------------------------
9. Use of multi-threading and database in the client server prototype
   version

Estimated marks:  20 / 20

Reasoning: Multi-threading has been used to a useful extent to
distribute different workloads on both the client and server.

See: Report.pdf

-------------------------------------------------------------------------
10. Individual programs explored and experimented before they are 
    integrated into the client server prototype version. 
    Pre-integration folder must contain the individual experiments.  

Estimated marks:  1 / 10

Reasoning:
We did not experiment much with anything other than TCP because of the
obvious impracticalities of UDP and RMI.

UDP does not take into account that the data may not arrive, so clients
can become unsynchronized with the server. This makes it impractical.

RMI does not support two-way communications like TCP does. A quick
experiment was done with RMI, but was unsuccessful. Any changes need
to be done on the server and distributed to the clients and RMI's design
in general does not allow this.

We still feel we deserve a mark for being able to successfully and
efficiently implement the client/server prototype using TCP without
running into any problems.

-------------------------------------------------------------------------
11. Justify and Implement any two improvements in the assignment 
    description  

Estimated marks:  10 / 10

Reasoning: Two considerable improvements have been added to the software.

* Group orders (standalone only)
* Automatic reconnect when connection is lost  (client-server prototype only)

See: Report.pdf

-------------------------------------------------------------------------
12. Regular work-in-progress demonstration during week 8, 9, 10, 11. 

Estimated marks:  10 / 10

Reasoning:
All members of the group have been present for work-in-progress 
demonstrations for week 8, 9, 10, and 11. All members have been able
to comprehensively explain their contribution to the project.

-------------------------------------------------------------------------
13. Include reasonable documentation according to the Javadoc standards

Estimated marks:  10 / 10

Reasoning:
All classes and methods have been meaningfully documented.

-------------------------------------------------------------------------
14. Implement an “About” dialog activated from the menu

Estimated marks: -  / -

Reasoning:
No marks were awarded for this, so it was not done.

-------------------------------------------------------------------------
15. A formally written word-version report with the clearly numbered 
    screenshots related to the scenarios must be within your compressed 
    folder.  

Estimated marks:  28 / 30

Reasoning:
The provided report covers the entire functionality of the program. It is
well-written and informative. It could be a little more "to-the-point".

See: Report.pdf

-------------------------------------------------------------------------
16. A readme.txt file explaining the features completed for the expected 
    marks and locating the presence of codes from other sources.  

Estimated marks:  10 / 10

Reasoning:
The readme.txt file provided contains all required aspects. Including
features completed, expected marks, presence of code from external 
sources and team member's information.

See: This file, readme.txt

-------------------------------------------------------------------------

Total:  / 300

-------------------------------------------------------------------------

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Helped Received from outside sources
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

We have used no additional code from outside sources.

