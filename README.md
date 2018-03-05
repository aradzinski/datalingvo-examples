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

### 4. Configuring and run `HelloWorld` example runner
 - Create `datalingvo-examples` project based on pom.xml file of cloned project using any IDE like IDEA or Eclipse and build it.
 - Configure VM options of 'HelloWorld' example (class: `com.datalingvo.examples.helloworld.HelloWorldProbeRunner`)
 
Required  VM options:
  - DATALINGVO_PROBE_TOKEN=\<TOKEN>
  - DATALINGVO_PROBE_EMAIL=\<EMAIL> 
  - DATALINGVO_PROBE_ID=\<ID>
   
where 
 - \<TOKEN> - company secret token, which was copied in the p. `2. Getting company secret token`
 - \<EMAIL> is your email which used for registration in `DatLingvo` system.
 - \<ID> - unique identifier of probe. Set is as any value now.
 
Example of configured VM options: -DDATALINGVO_PROBE_TOKEN=key-content -DDATALINGVO_PROBE_EMAIL=your-email.gmail.com  -DDATALINGVO_PROBE_ID=helloworld.id

Run and be sure that it started without errors and warnings. 

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

