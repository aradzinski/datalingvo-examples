## DataLingvo. How to start.

Shortest an easiest way to start working with [DataLingvo](https://datalingvo.com) is configuring and start `HelloWorld` example.

Required tools and skills:
 - Java 8 and maven 3x (tested under maven 3.5.2) must be installed.
 - You should be able to create java project based on maven configuration file using any IDE (IDEA, Eclipse etc)

It takes 3-5 minutes and includes following steps:

### 1. Registration
 - Register user in [DataLingvo](https://datalingvo.com).
 
Note please that all next steps accessible only for administrative users. 

### 2. Getting company secret token
 - [Login](https://datalingvo.com/client/src/datalingvo.html#/signin) into the system. 
 - Click [Account](https://datalingvo.com/client/src/datalingvo.html#/account) page.
 - Scroll to the `Data Probe` chapter.
 - Click `Show token` button. 
 - Click `Copy` button and save copied to the clipboard key into your secret file. 

### 3. Getting `datalingvo-examples` project 
 - Clone project `datalingvo-examples` on your local machine from `https://github.com/aradzinski/datalingvo-examples.git`
  
Example for command line:
>cd<br/> 
>mkdir datalingvo-examples<br/> 
>cd datalingvo-examples<br/> 
>git clone https://github.com/aradzinski/datalingvo-examples.git .

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
