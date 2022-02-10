# MyHealth
An unofficial Android application developed to help the Malaysian government deal with COVID-19.

Screenshots & details are available at:
https://guizichen.wixsite.com/portfolio/myhealth

## Code Samples
1. [**AppointmentFragment.java**](https://github.com/Gavin-Guiii/MyHealth/blob/main/app/src/main/java/com/RobX/MyHealth/AppointmentFragment.java) <br>It reads all the existing appointments and displays them.



2. [**AppointmentDatabaseHelper.java**](https://github.com/Gavin-Guiii/MyHealth/blob/main/app/src/main/java/com/RobX/MyHealth/AppointmentDatabaseHelper.java) <br> It handles CRUD (Create, Read, Update, Delete) of the appointment database, and provides other useful functions. For example, it can return data of user's next appointment chronologically, or return the latest PCR Test result.



3. [**SelectMCActivity.java**](https://github.com/Gavin-Guiii/MyHealth/blob/main/app/src/main/java/com/RobX/MyHealth/SelectMCActivity.java) <br> It first reads all the medical centers stored in the medical center database, and then sorts them based on the distance between user's current location and the medical center.


