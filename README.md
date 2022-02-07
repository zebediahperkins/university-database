# university-database
A node.js/mongodb program with a basic java command line front end modeled after a basic university database
## How the client uses the API
To interact with the server, the client uses a REST API. [`UniversityAPI`](https://github.com/zebediahperkins/university-database/blob/main/Client/src/UniversityAPI.java) houses functions that make these interactions much more simple and structured.

Simple example of UniversityAPI:
```
UniversityAPI.login("exampleUsername", "examplePassword");

System.out.println(UniversityAPI.viewCourses());
UniversityAPI.registerForCourse("ENG 101");

UniversityAPI.logout();
```
The server receives these requests from a client, and performs some logic to determine whether the request is valid (authentic, logically sound, etc). Once the server has finished any logic/database operations necessary, a response is sent back to the client.
## How the server accesses the database
The server is configured to connect to a mongodb database running on the local machine. The npm package [mongoose](https://mongoosejs.com/docs/) is used to structure `documents` and `collections` before accessing the database.
## Security
Sensitive interactions between server/database are encrypted with the sha-256 and bcrypt algorithms. A user's session is verified by an api-key, whose unencrypted value is only known to the client program. In a real-world implementation, the api-key would likely be replaced by another form of authentification.

Interactions between client/server aren't explicitly encrypted. This is because, in an actual implementation, the server would have an SSL certificate.