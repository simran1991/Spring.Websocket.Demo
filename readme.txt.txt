
*********************************************************************************
This application is a spring boot application so no extra server is needed.
It is built using spring boot,Stomp over web socket to suport real time chat.


************************************************************************************
How to run this application.


UI is bit simple HTML and jquery but I can make it in Angular 2 if requirement arises.

To Run this project 

1)You need to have maven installed to  build this project.

2)Appropriate Java installation needed.(Mostly latest JRE will suffice).

3) Then, we need to clone the repository from 
https://github.com/simran1991/Spring.Websocket.Demo.git
or download it as zip file from same repository

2)open command prompt in folder wher this repository is residing.(this folder is where POM.xml is residing(root folder)).

3)use "mvn install" to build project.

4)an executable jar will be created in "target" folder.

5)Goto this "target folder" and use java command to run this project from executable jar

6)use "java -jar target/Spring.Websocket.Demo-0.0.1-SNAPSHOT.jar"

7)goto web browser open 2 tabs and in both open "localhost:8080".

8)Enter any name in both pages and click connect.

9)now users on both pages can chat in real time.

10)Use photo button to send images to other user.


