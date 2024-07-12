# donation-demo
Donation demo app created for Digital Aid Seattle Volunteer Opportunity.

# Instructions for running
Install
- Java JDK 21
- Maven 3.2
- Postgresql

To create the database, use the scripts in the donation-demo-db.sql file to create the database, schema, and tables.
Update the file application.properties with a user and password that has access to your locally created donation-demo database.
The user being used in this repo, is the default postgres user.

To create a jar file: Run mvn clean package.
More information on Maven commands can be found on Maven's website: https://maven.apache.org/
This will create a jar file called donation-demo-0.0.1-SNAPSHOT.jar

Other instructions on how to run a spring-boot application are here: https://docs.spring.io/spring-boot/docs/1.5.16.RELEASE/reference/html/using-boot-running-your-application.html
This project uses Maven.

This will run the application in localhost, port 8080.
Open a Browser and type localhost:8080
The donation list should appear.

# User Interface
This is a very simple two-page user-interface. It uses Tymeleaf templates which was an easy way to create a quick front-end for a Spring-boot application without the need to tie in a more robust UI framework.
The instructions said to keep the UI very simple, so that's why it isn't very intutive or pretty.

THe UI has two pages:
- The home page: /(root)
- The donation form: /donation-form
  
When a new donation is submitted, the page will navigate to a submit page.
The donation-list page gives the option to edit or delete a donation. The edit button will open up the donation-form with the donation information for that donation. The delete button will call the webservice to delete the the donation record, then reload donation-list.

# API Layer
The UI communicates to the API Layer as if they are living on different servers.
This was specified in the instructions as well.
There are two endpoints:
- GET /donations : This will load a full list of donations. Returns a list of donation records.
- GET /donation?id={id} : This will get a single donation by id. Returns the entire donation record.
- POST /donation : This will create a new donation record. Variables: name, type, quantity, and date. Returns success or error.
- PUT /donation : This will update the donation record. Variables: name, type, quantity, and date. Returns success or error.
- DELETE /donation : This will delete the donation record. Variables: id. Returns success or error.

# Database
For file storage, I am using a simple PostgresSQL database. I chose PostgresSQL because I already had that installed on my machine. THe SQL and drivers would need to be modified if using a different database. Since this is a single table, a database might not be entirely necessary, but I wanted to show that I have a bit of expreience with databases.

The database consists of these tables:
- donor (first_name, last_name, id)
- donation (donor_id, type_id, quantity, date, id)
- donation_types (id, name)

# File Structure
- src/main: Contains the java code for the application and webservice layer
-   java/com/demo: Contains the main application shell, a controller for the UI form, and a controller for the backend REST services.
-   java/com/demo/database: Contains classes that handle connecting to, querying, and writing to the database.
-   resources - html and application.properties can be found here
-     templates - the basic html is here for the donationList and donationForm pages. These are using tymeleaf templates which requires this file structure.
- src/test: Contains java unit tests.
- target: Contains files that maven creates when it compiles, builds, cleans, and packages up the spring boot code into jar. This is also where the Snapshot jar files can be found.
  







