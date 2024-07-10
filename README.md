# donation-demo
Donation demo app created for Digital Aid Seattle Volunteer Opportunity.

# Instructions for running
Make sure to have Java JDK 21 installed and Maven 3.2 installed.

To create a jar file: Run mvn clean package.
More information on Maven commands can be found on Maven's website: https://maven.apache.org/
This will create a jar file called donation-demo-0.0.1-SNAPSHOT.jar

Other instructions on how to run a spring-boot application are here: https://docs.spring.io/spring-boot/docs/1.5.16.RELEASE/reference/html/using-boot-running-your-application.html
This project uses Maven.

This will run the application in localhost, port 8080.
Open a Browser and type localhost:8080
The donation list should appear.
Use the "Enter a new donation" link to see the donation form.

# User Interface
This is a very simple two-page user-interface. It uses Tymeleaf templates which was an easy way to create a quick front-end for a Spring-boot application without the need to tie in a more robust UI framework.
The instructions said to keep the UI very simple, so that's why it isn't very intutive or pretty.

THe UI has two pages:
- The home page: /(root)
- The donation form: /donation-form
  
When a new donation is submitted, the page will navigate back to the root page where it shows a donation-list of all donations.
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
- Donors (FirstName, LastName, Id)
- Donations (donorId, type, quantity, date, Id)







