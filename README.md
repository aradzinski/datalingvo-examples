<img src="images/datalingvo2.png" width="150"/>

## Getting Started
Welcome to DataLingvo - the easiest way to built advanced natural language
interface to any device, data source or a service. 

This repo contains Java and Scala
examples of various data models from very simple (like `HelloWorld` example) to more 
complete ones (like `Weather` example). In this short tutorial you'll learn how to get up and running with a
simple example.

### Prerequisites
Here's the list of tools and basic skills that you will need to get started with DataLingvo:
 - DataLingvo uses Java and you need to have [Java SE Runtime Environment](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (JRE) ver. 8 or later installed.
 - You'll need [Git]("https://git-scm.com/downloads) and [Maven](https://maven.apache.org/install.html) to be installed as well.
 - Know how to create Maven-based project in your favorite Java IDE.

### Create Account
If you haven't done it already go ahead and [sign up](https://www.datalingvo.com/client/src/datalingvo.html#/signup)
with DataLingvo to create your **free account.** All newly created accounts automatically
get administrative privileges. When signing in for the first time you will get a message that you don't have 
any data sources - don't worry, we'll add one quickly.

### Get Company Probe Token
To configure your example you'll need a <b>secret probe token</b> that has been created and
associated with your company. Here's how to get it:
 - Go to [www.datalingvo.com](https://www.datalingvo.com) and [sign in](https://datalingvo.com/client/src/datalingvo.html#/signin).
 - Open up [Account](https://datalingvo.com/client/src/datalingvo.html#/account) page that can found by clicking on the avatar at the top right corner of your browser window.
 - Scroll down to the **Probe Token** section.
 - Click `Show Probe Token` button.</li>
 - Click `Copy` button to save your probe token into the clipboard. You should store it securely in a safe place.
 
### Clone This Project
Clone this project to a local folder:
```shell
$ mkdir datalingvo
$ cd datalingvo
$ git clone https://github.com/aradzinski/datalingvo-examples.git
```
### Run 'HelloWorld' Data Probe
We are going to use **HelloWorld** example from example project, located in `com.datalingvo.examples.helloworld` package, that simply answers 'Hello World' for
any user questions. Lets create Maven-based project, configure and run it:
 - Create Maven project based on `pom.xml` using your favorite Java IDE.
 - Create **Run Configuration** for `HelloWorldProbeRunner` class.
 - Specify two system properties for that configuration:
   - `DATALINGVO_PROBE_TOKEN`=`your_probe_token` (see previous step)
   - `DATALINGVO_PROBE_ID`=`hello.world`
 - Start `HelloWorldProbeRunner` example and ensure that you get the output that looks like this:
    ```shell
    +--------------------------+
    | Probe started [2.05 sec] |
    +--------------------------+
    
    10:07:20 INFO  Down-link 'downlink.datalingvo.com:8100' established.
    10:07:23 INFO  Up-link 'uplink.datalingvo.com:8101' established.
    ``` 

### Adding Data Source
At this point you have a running local data probe that is connected to DataLingvo servers.
All you have to do now is to add a data source in the [Admin Studio](https://datalingvo.com/client/src/datalingvo.html#/studio) and
start asking questions:
 - Go back to [www.datalingvo.com](https://www.datalingvo.com) and open up [Admin Studio](https://datalingvo.com/client/src/datalingvo.html#/studio) (top navbar menu).
 - Navigate down to **Data Sources** and click `Add Data Source` button.
 - Set any **Name** and **Description** and select 'Hello World Example' model.
 - Click `Add` button.
 - **NOTE:** first data source will be automatically enabled and selected.
 
### Ask Questions
Everything's ready - just go head and start exploring:
 - Go to [home](https://datalingvo.com/client/src/datalingvo.html#/ask) page and ask the question: 
<img src="images/howto1.png" width="262px">

 - You should get the following answer: 
<img src="images/howto2.png" width="620px">

### Explore!
Now that you have the basic example running you can explore all other examples in the
project. Configure and run them exactly the same way and head over to
[Developers Guide](https://datalingvo.com/client/src/datalingvo.html#/devguide) for in-depth explanations.
 

