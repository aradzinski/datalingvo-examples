<img src="images/datalingvo2.png" width="150"/>

## Getting Started
Welcome to DataLingvo - the easiest way to built advanced natural language
interface to any device, data source or a service. 

This repo contains Java and Scala examples of various data models from very simple (like `HelloWorld` example) to more 
complete ones (like `Weather` example). In this short tutorial you'll learn how to get up and running with a simple example.

### Prerequisites
Here's what you will need to get started with DataLingvo:
 - [Java SE Runtime Environment](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (JRE) ver. 8 or later installed.
 - Latest [Git]("https://git-scm.com/downloads) and [Maven](https://maven.apache.org/install.html).
 
### Create Account
[Sign up](https://www.datalingvo.com/client/src/datalingvo.html#/signup)
with DataLingvo to create your **free account.** All newly created accounts automatically get administrative privileges. 

### Get Company Probe Token
Secret <b>probe token</b> has been associated with your company - here's how to get it:
 - Go to [www.datalingvo.com](https://www.datalingvo.com) and [sign in](https://datalingvo.com/client/src/datalingvo.html#/signin).
 - Click on avatar at the top right corner and select **Account** from dropdown menu.
 - On **Account** page scroll down to the **Probe Token** section.
 - Click `Show Probe Token` button.</li>
 - Click `Copy` button to save your probe token into the clipboard. Store it securely in a safe place.
 
### Clone This Project
Clone this project to a local folder:
```shell
$ mkdir datalingvo
$ cd datalingvo
$ git clone https://github.com/aradzinski/datalingvo-examples.git
```
### Run 'HelloWorld' Data Probe
We are going to use **HelloWorld** example located in `com.datalingvo.examples.helloworld` package.

You can run **HelloWorld** data probe either:
 - from command line, or
 - by creating Maven-based project in Java IDE (like <a target=_ href="https://www.jetbrains.com/idea/">IntelliJ IDEA</a> or <a target=_ href="https://eclipse.org/">Eclipse</a>).

#### Run From Command Line
Go to the project root and build data probe:
```shell
$ mvn clean package
```
**NOTE**: if data probe build failed clear Maven local cache and re-run the command:
```shell
$ mvn dependency:purge-local-repository -DactTransitively=false -DreResolve=false --fail-at-end
$ mvn clean package
```
Start data probe using the following Maven command:
```shell
$ mvn exec:java@hello -DDATALINGVO_PROBE_TOKEN=XXX -DDATALINGVO_PROBE_ID=hello.probe.id
```
Where:
 * `hello` - name of the probe to start (you can also use `time`, `weather`, `chat`, `echo`, `robot`, or `whereami`).
 * `XXX` - probe token you obtained in the previous step.
 * `hello.probe.id` - arbitrary user-defined probe ID.
 
#### Run From Maven Project
To run **HelloWorld** data probe from the IDE project follow these steps:                     
 - Create Maven project based on `pom.xml` using your favorite Java IDE.
 - Create **Run Configuration** for `HelloWorldProbeRunner` class.
 - Specify two system properties for that configuration:
   - `DATALINGVO_PROBE_TOKEN`=`your_probe_token` (see previous step)
   - `DATALINGVO_PROBE_ID`=`hello.world`
 - Run `HelloWorldProbeRunner` example.

Whether you used command line or IDE project you should get the output:
```shell
...
+--------------------------+
| Probe started [2.05 sec] |
+--------------------------+

04.06.2018 10:06:04 Down-link 'downlink.datalingvo.com:80' established.
04.06.2018 10:06:04 Up-link 'uplink.datalingvo.com:80' established.
``` 

That's all there is to start a local data probe with a specific data model.

### Adding Data Source
Add data source in the [Admin](https://datalingvo.com/client/src/datalingvo.html#/studio) page and start asking questions:
 - Go back to [www.datalingvo.com](https://www.datalingvo.com) and open up [Admin](https://datalingvo.com/client/src/datalingvo.html#/studio) page (top navbar menu).
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
Now that you have the basic example running you can explore all other examples in the project. Configure and run them exactly the same way and head over to [Developers Guide](https://datalingvo.com/client/src/datalingvo.html#/devguide) for in-depth explanations.
 

