# Web Forum

This was project where i was for the first time introduced to principles and inner workings of web development.
It was written with MVC pattern in mind.

![Alt text](https://image.ibb.co/bNWnCF/f1.png "Picture of GUI")
![Alt text](https://image.ibb.co/kaZ7CF/f2.png "Picture of GUI")
![Alt text](https://image.ibb.co/gPHG6a/f3.png "Picture of GUI")
![Alt text](https://image.ibb.co/hwvQzv/f4.png "Picture of GUI")



## Overview

* Frontend was written using HTML5, CSS3/Bootstrap and JavaScript/Jquery(3.2.x)
* Backend was written using Java Servlet Techonologies
* MySQL is relational database used to store data (SQL scripts can be found in "sql scripts" folder in the root directory)
* Forum runs on Apache Tomcat 6.0, Dynamic Web Module 2.5

## Basic Information
* Web Forum has Forums > SubForums > Topics > Replys
* Forums can be Public (everyone can see them), Open (only Registered and up can see them), Closed (only Moderator and Admin can see them)
* Topics and Replys respect the visibility of their parent.

## User Roles
* Admin - CRUD operation for every possible object
* Moderator - Similar to Admin but cant work CRUD operations on Users or anything Admin made
* Registered - CRUD to his own Topics and Replys
* Guest - Can only see and search elements which are visible to him


## LICENSE

Copyright © 2017 Marko Stankovic

Distributed under the The MIT License (MIT).
