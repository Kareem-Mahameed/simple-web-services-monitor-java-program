# Project for simple web Services Monitor Java program using multithreading and socket

- The project has Java Classes only, no UI, main method in MonitorApp.java.
- after starting MonitorApp.java/main or MonitorAppTests.java, please try to switch the Wifi or network signal on/off to see the notifications in logs or console  

- more Unit test cases need to be added, most of the test done through validating the logs specially MonitorAppTests.java
- after starting the app or any test case 

- Please replace log path with a suitable path in log4j property file: log4j.appender.roto.File=%YOUR_PATH%/servicesMonitorLogs.log to see MonitorApp logging or same logs will be presented in the Console

- Solution core idea built on the base of having services Monitor thread(MonitorApp.java) which works all the time and triggers pooled worker threads (ServicePollWorker) to check each service status and notify the registered service's listeners, services and its listeners listed in the singleton servicesList Map (ServiceList.jav). 
- for testing purpose I used Executors.newCachedThreadPool(), It's unbounded, suitable for testing short-lived asynchronous tasks like our example, for production scale we have to use Executors.newFixedThreadPool or ThreadPoolExecutor so we limit the number of threads and have full control

- Establish a TCP connection to the host implemented by trying to open a socket. 

- As the requirement to have Java class, not application/ web service, service registration and listeners registration are direct API call, not web services.
- First I started implementing using some enterprise Framework like Spring scheduler, they have great API for scheduling timed functions, like @Scheduled along with @Async for parallel handling, but later on, I realized that the requirements might be to use core Java API, so I quickly switched to Java classes with help of some java threading ready examples .  

- As requested services will be monitored based on the configured time interval and outage dates.

- If a service goes down, services Monitor will not notify any listener until service's grace period are passed and the service still down.

- Assignment mentioned to have one grace period for all services,  I configured services to have their own grace period, but the caller can set all services with the same value!.

- Listeners will not be notified until their individual configured time interval passed since their previous notification.

- As requested, each listener for each service has his own desired time interval (polling frequency).

- Notifier function just logs the notification, but it can send Email or make an asynchronous API callback. 

- Since opening a socket require network latency,  further enhancement can be done like not checking any service if there is no listener for it or all listeners still within their time interval (polling frequency). 

- Services and listeners Models can be saved in a DB
- each service has its configurable polling frequency, this property is required to implement the last line in the assignment: "If the grace time is less than the polling frequency, the monitor should schedule extra checks of the service." 
- So now we have: Service pollingInterval, Service graceTime, listenerPollingInterval
- to send service_Up notification below condition must apply :
1- Service pollingInterval or Service graceTime already exceeded (the smaller one)
2- Listener lastNotifiedState is not "UP"
3- listenerPollingInterval exceeded

- to send service_DOWN notification below condition must apply :
1- Service pollingInterval or Service graceTime already exceeded (the smaller one)
2- Listener lastNotifiedState is not "DOWN"
3- listenerPollingInterval exceeded
4- service has been down for period exceeded Service graceTime
