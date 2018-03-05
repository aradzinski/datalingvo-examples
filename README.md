<img src="images/datalingvo2.png" width="150"/>

## Getting Started
Welcome to DataLingvo - the easiest way to built advanced natural language
interface to any device, data source or a service. This repo contains Java and Scala
examples. In this quick how-to tutorial you'll learn how to get up and running with a
simple example.

### Prerequisites
Here's the list of tools and basic skills that you will need to get started with DataLingvo:
 - DataLingvo uses Java and you need to have [Java SE Runtime Environment](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (JRE) ver. 8 or later installed.
 - You'll need [Git]("https://git-scm.com/downloads) and [Maven](https://maven.apache.org/install.html) to be installed as well.
 - Know how to create Maven-based project in your favorite Java IDE.

### Create Account
If you haven't done it already go ahead and [sign up](https://www.datalingvo.com/client/src/datalingvo.html#/signup)
with DataLingvo to create your <b>free account.</b> All newly created accounts automatically
get administrative privileges. When signing in for the first time you'll be asked to change
your temporary password.

### Clone This Project
Clone this project to a local folder:
```shell
$ mkdir datalingvo
$ cd datalingvo
$ git clone https://github.com/aradzinski/datalingvo-examples.git
```

### Get Company Probe Token
To configure your example you'll need a <b>secret probe token</b> that has been created and
associated with your company. Here's how to get it:
 - Go to https://www.datalingvo.com and [sign in](https://datalingvo.com/client/src/datalingvo.html#/signin).
 - Open up [Account](https://datalingvo.com/client/src/datalingvo.html#/account) page that can found by clicking on the avatar at the top right corner of your browser window.
 - Scroll down to the <b>Data Probe</b> / <b>Probe Token</b> section.
 - Click `Show Probe Token` button.</li>
 - Click `Copy` button to save your probe token into the clipboard. You should store it securely in a safe place.
 
### Run 'HelloWorld' Data Probe
We are going to use *HelloWorld* example, located in `com.datalingvo.examples.helloworld` package, that simply answers 'Hello World' for
any user questions. Lets create Maven-based project, configure and run it:
 - Create Maven project based on `pom.xml` using your favorite Java IDE.
 - Create *Run Configuration* for `HelloWorldProbeRunner` class.
 - Specify two system properties for that configuration:
   - `DATALINGVO_PROBE_TOKEN`=`your_probe_token`
   - `DATALINGVO_PROBE_ID`=`hello.world`
 - Start `HelloWorldProbeRunner` example and ensure that you get the output that looks like this:
    ```shell
    +--------------------------+
    | Probe started [2.05 sec] |
    +--------------------------+
    
    10:07:20 INFO  Down-link 'downlink.datalingvo.com:80' established.
    10:07:23 INFO  Up-link 'uplink.datalingvo.com:80' established.
    ``` 
 
 
 
 
 
 
 
 
 
 
 
 
 


### 5. Configuring datasource 
 - Go back to `DataLingvo` console [Admin studio](https://datalingvo.com/client/src/datalingvo.html#/studio)
 - Click button `Add datasource`
 - Set any preferable `Name` and `Description` in these fields. 
 - Select `Model`.
 - `Configuration` field leave empty. 
 - Click button `Add`.
Added datasource should be appear in the table `Data Sources`. Click `Select` button to set it active. 

### 6. Ask first question 
 - Go to the page [Ask](https://datalingvo.com/client/src/datalingvo.html#/ask) and type any test question. 
 For example: *`test question`*.
 - After few second you should receive response `Hello World!`.

So, your first example works fine.

Continue with other examples of cloned project `datalingvo-examples` and [Developers guide](TODO:LINK!).		

