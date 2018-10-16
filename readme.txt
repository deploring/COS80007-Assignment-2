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
10.2. Double click the JAR file again to run it (or open it in commmand prompt/terminal).
10.2x. There should not be any errors past this point.

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Features Completed
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

The standalone contains all of the following features. The Client/Server
also contains all of these features.

-------------------------------------------------------------------------
	Recieving Orders
-------------------------------------------------------------------------
Can recieve a customer's order by asking for:
- Customer Name
- Table Number
- Meal Type (Breakfast, Lunch, Dinner)
- Food
- One or more Drinks

The user can select Food without a drink and vice-versa.

A reminder message will be displayed if the customer name, table number,
meal type, food and beverage items remain blank and the 'Enter Data'
button is pressed.

Menu selection boxedbecome enabled with all meni items after the customer's
name, table number, and meal type are filled in.

Food and Beverage items are loaded in through the given csv file.

If a meal type and at least one menu item is chosen, then the 'Display 
Choices' button can be clicked to display nutritional information. 
Inlcuding the item name, energy, protein, carbohydrates, total fat, fibre,
and price.

The order can be displayed by selected 'Display Order'.

-------------------------------------------------------------------------
	Preparing Orders
-------------------------------------------------------------------------

Orders can be prepared by a chef. Once the order is placed it is in a 
'waiting' state for the chef to start preperation. The chef can select the
prder from the waiting state and the 'Prepare' button become enabled; and
upon preperation will lead the order to enter a 'served' state.

-------------------------------------------------------------------------
	Preparing Orders
-------------------------------------------------------------------------

Orders in the 'served' state can be billed. Validation is applied so that
only the served order cna be billed. 

A bill recipt with nutritional details is displayed.

-------------------------------------------------------------------------
	Other Features
-------------------------------------------------------------------------

On the Client/Server program, all orders can be seen and displayed on all
clents at the same time.

************************************
Enter other client/server features
************************************

-------------------------------------------------------------------------
	Improvements
-------------------------------------------------------------------------

***************************************
Enter improvement made
***************************************

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Estimated marks
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

-------------------------------------------------------------------------

1. Implement the Swing/JavaFX based GUI for the standalone prototype 
   version of the order reception, preparation, and billing with all 
   functionalities.  

Estimated marks:   / 60

Reasoning:

See: Report.pdf

-------------------------------------------------------------------------

2. Show any four uses of lambdas and streams in validations 

Estimated marks: 10  / 10

Reasoning:
There has been at least four uses on lambdas and streams in validation
in this project

See: Report.pdf

-------------------------------------------------------------------------
3. Show any four uses of lambdas in event handlings

Estimated marks: 10 / 10

Reasoning:
There has been at least four uses of lambdas in event handling in this
project.

See: Report.pdf

-------------------------------------------------------------------------
4. Show any four uses of collections and/or generic methods

Estimated marks:   / 10

Reasoning:
There has been at least four uses of collections and/or generic methods
in this project.

See: Report.pdf

-------------------------------------------------------------------------
5. Programmatically creating and processing database and tables 
   in mysql/derby

Estimated marks:   / 10

Reasoning:

-------------------------------------------------------------------------
6. Programmatically loading the food and beverage details from 
   Assignment2Data.csv into the Food and Beverage combo boxes. 

Estimated marks:  10 / 10

Reasoning:
Food and beverage details from Assignment2Data.csv have been programatically
loaded into Food and Beverage combo boxes.

See. Report.pdf

-------------------------------------------------------------------------
7. Relate the implementation of the standalone version with Model 
   Controller View (MVC)

Estimated marks:  30 / 30

Reasoning:
The concept of MVC has been followed in this aplication. Justification
rearding Model View Controller concept is further explained in Report.pdf.
Naming conventions have also been used to specify files that are a model,
view or controller.

See: Report.pdf

-------------------------------------------------------------------------
8. Implement the Swing/JavaFX based GUI for the client server prototype 
   version of the order reception, preparation, and billing with all 
   functionalities.

Estimated marks:  60 / 60

Reasoning:
All functionalities of the client server prototype version of the order
reception, preparation, and billing has been implemented. Implementation
is explained in Report.pdf.

See: Report.pdf

-------------------------------------------------------------------------
9. Use of multi-threading and database in the client server prototype
   version

Estimated marks:   / 20

Reasoning:

-------------------------------------------------------------------------
10. Individual programs explored and experimented before they are 
    integrated into the client server prototype version. 
    Pre-integration folder must contain the individual experiments.  

Estimated marks:   / 10

Reasoning:

-------------------------------------------------------------------------
11. Justify and Implement any two improvements in the assignment 
    description  

Estimated marks:   / 10

Reasoning:

-------------------------------------------------------------------------
12. Regular work-in-progress demonstration during week 8, 9, 10, 11. 

Estimated marks:  8 / 10

Reasoning:
All members of the group have been present for work-in-progress 
demonstrations for week 8, 9, 10, and 11. All members have been able
to comprehensively explain their contribution to the project.

-------------------------------------------------------------------------
13. Include reasonable documentation according to the Javadoc standards

Estimated marks:  8 / 10

Reasoning:
All classes and methods have been meaningfully documented. We have attempted
to follow the structure of Javadoc as much as possible, but feel it may
be slightly lacking in minor areas.

-------------------------------------------------------------------------
14. Implement an “About” dialog activated from the menu

Estimated marks: -  / -

-------------------------------------------------------------------------
15. A formally written word-version report with the clearly numbered 
    screenshots related to the scenarios must be within your compressed 
    folder.  

Estimated marks:  25 / 30

Reasoning:
The provided report covers the entire functionality of the program. It is
well-written and information.

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

