# Project Description
Project 1 is a Tuition Reimbursement Management API System which provides a way to reimburse employees for their training courses.  The application allows users with the roles of admin to approve ordisapprove employees’ reimbursement form based on their roles of Direct Supervisor, Department Head, and Benefit Coordinator.  Employees are also allow to upload information regarding their reimbursement.

# Technologies Used
- Java
- AWS S3
-	AWS Keyspace
-	Postman

# Roles / Responsibilities:
-	Created the process of approval among the Department Head, Direct Supervisor, and Benefit Coordinator
-	Created Keyspaces in AWS to store information of users and Tuition Reimbursement forms
-	Implemented Admin’s ability to approve or disapprove forms from users.
-	Used junit testing to provide information if functions in the program are operating as attended.
-	Ensure the ordering of the approval process is set in order.
-	Calculate the formula of the reimbursement system.
-	Created types of their reimbursement coverage based on what course the user took.
-	Created a system to automatically approve reimbursement if deadline is met late.


# Features
-	Created the process of approval among the Department Head, Direct Supervisor, and Benefit Coordinator.
-	View all the users and their status of their forms
-	Login, Register, and Logout functionality 
# To-do list:
-	Fix the uploading functionality of the application to store grade/presentation information.
- Fix issue with certain files not being readable when downloading from AWS.

# Getting Started
1.	Install Git Bash (https://git-scm.com/downloads), and Eclipse (https://www.eclipse.org/downloads/) onto your computer.
2.	Launch your Git Bash application and input the following commands:
a.	Git clone https://github.com/RashadCBowman/revature-project1
b.	Cd rBowmanProject1
c.	Git pull origin main
3.	Launch the rbowmanProject1 application.

# Usage
With postman, you will be able to send commands to the Deck Building API as follows:
http://localhost:8080
- PUT /employee/alt: Register a user to the system.
- POST /employee/(user name): Login as the user.
- DELETE /employee: Logout the current user.
- POST /employee/(user id): Approves the following form based on the user’s id.  Admin can only use this function.
- DELETE /employee/(user id): Deny the following form based on the user’s id.  Admin can only use this function.
- POST /employee/(file name)/picture: Upload a file to the bucket
- GET /employee/(file name)/picture: Download a file from the bucket

# License
This project uses the following license: 

