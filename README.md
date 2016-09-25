Multi-Threaded File based HTTP Server
====================================

Dependencies:
-------------

- JDK 8
- Maven 3.3 or above

Building:
---------

`mvn clean package`

Usage:
------

The jar file produced after building, is an executable one and can be simply run on the command line by `java -jar {{path_to_jar}}`.

The server provides the following options:

1. Port `-p`: An appropriate port number to listen to incoming connections.
2. Local Bind Address `-b`: A local address to bind to.
3. Root Directory `-r`: The root of the directory to serve. _(Note: `~/` does not work)_ 
4. Thread Count `-t`: The number of threads to use to handle concurrent requests.

#### Sample Usage:

1. `cd {{project_root}}`
2. `java -jar target\httpserver-1.0-jar-with-dependencies.jar -t 4 -p 7999 -b localhost -r C:/Users/themanikjindal/Desktop/`

References:
-----------

1. http://www.onjava.com/pub/a/onjava/2003/04/23/java_webserver.html