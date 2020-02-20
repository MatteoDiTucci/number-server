# Description
Web Application to log collections of 9 digits numbers.  
Constraints:
- Only 9 digits numbers are valid
- Numbers collections need to terminate with a new line character (`\n`)
- Max 5 clients simultaneously logging numbers
- Only log unique numbers, filtering out numbers that have already been logged
- Every 10 seconds, write in console:
    - how many unique numbers was received in the last 10 seconds
    - how many duplicate numbers was received in the last 10 seconds
    - how many unique numbers have been received from application startup

# Setup
* Java 11
* If you running the project in IntelliJ and Gradle gives problems:   
    * File -> Settings -> Build, Execution, Deployment -> Build Tools -> Gradle
    * Gradle JVM: change to version 1.11
    * Re-run gradle task 

# How to run locally
In the project root folder, run `./gradlew run`  
The application accepts POST requests to `http://localhost:4000/numbers` with body containing any 9 digits numbers terminated by a new line character (`\n`), for instance: 
```
123456789
098765432    

```

Numbers will be logged into the file `numbers.log`

To shutdown the app send a POST request with the following body:
```
terminate  

```

# How to test
In the project root folder, run `./gradlew test`

# Assumptions
* Clients receives a http response with status 200 if their request has been added to the application queue, but not logged yet  
* Runtime exception are not cleanly handled to return a 5XX to clients
* Numbers are treated as `String` and not wrapped into a domain entity to keep the design simple and to improve performance avoiding conversions from and to `String`
* No data replication (e.g. multiple log files) is needed
* Logback can log any string of arbitrary length
* 60 seconds as a reasonable amount of time for the queue consumers to finish their tasks when shutdown is triggered
* Logback shutdowns gracefully by [default](http://logback.qos.ch/manual/configuration.html#shutdownHook)
* If just one number is malformed within the whole collection, the client will receive a BAD REQUEST (400)


# How the application was implemented
I started implementing all the functional requirements assuming single-threaded execution and no optimisation for throughput.

Then, I introduced concurrency safety mechanisms:
* `AtomicInteger` to count duplicates
* `ConcurrentHashSet` (`ConcurrentHashMap`) to persist unique numbers
* 5 threads for the http server (https://docs.micronaut.io/latest/guide/index.html#threadPools)
* Logback handles concurrency internally by default

Finally, I started measuring and optimising for throughput.
I used Gatling to generate a load scenario and IntelliJ JProfiler plugin the spot the bottlenecks.  
The Gatling load scenario used is 20 active users per second firing requests containing 10K random numbers as payload.  
I thought about using different configurations and load distributions (Poisson) but at the end it would have been an arbitrary assumption.
I also do not master Gatling, so I kept it simple.  
The bottlenecks encountered were:
* Logback writing to file (lifted using async appender and logging all the numbers contained in a request as a single string)
* `ConcurrentHashSet` (`ConcurrentHashMap`) insertion (lifted introducing multi-threaded producer-consumer approach)

At the time of writing, the bottleneck for the client response time is the regex that validates the numbers format.
The bottleneck for the threads consuming the queue is the `ConcurrentHashSet` (`ConcurrentHashMap`) insertion

# Limits
* The application can hold up to Java `Integer.MAX_VALUE` unique numbers because `ConcurrentHashSet` (`ConcurrentHashMap`) is based on an array whose max length is `Integer.MAX_VALUE`
* The application can hold up to Java `Integer.MAX_VALUE` duplicates because `AtomicInteger` upper bound is `Integer.MAX_VALUE`
* The application can hold up to Java `Integer.MAX_VALUE` request in the queue as I used the default capacity of `LinkedBlockingQueue` which is `Integer.MAX_VALUE`

# Missing test coverage
* The report that prints in console is not covered by an acceptance test.  
The idea would have been to swap `stdout` with a `new PrintStream(new ByteArrayOutputStream())` during the setup of the test and then restore `stdout` at the end of the test.  
I did not consider this critical so I left it out for the sake of time
* Number format validation would be better tested using property based testing.  
I left this out for the sake of time
* The `QueueConsumers` logic is not tested.  
This could have been done extracting that logic in another class and unit testing it.  
This would also have led to a better separation of concerns, leaving the `QueueConsumers` just in charge of thread spawning and shutting down.  I left it out for the sake of time
 