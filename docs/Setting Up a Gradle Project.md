# Setting Up a Gradle Project 

## Start a new Gradle Project

1. Navigate to File $$\rightarrow$$ New Project and choose Gradle
2. Check ` Java ` for additional Libraries

![image-20191216114041941](Setting Up a Gradle Project.assets/image-20191216114041941.png)

3. Enter your name for `ArtefactId` , the field for `GroupId` does not matter

   <center><img src="Setting Up a Gradle Project.assets/image-20191216114322824.png" alt="image-20191216114322824" style="zoom:80%;" /></center>

4. For `ProjectName` ese the same name that you previously used for `ArtefactId` 

Once your project is finished you should be able to create classes and build them via the `Gradle` panel to the right.

Look for `ProjectName` $$\rightarrow$$ `Tasks` $$\rightarrow$$ `build` $$\rightarrow$$ ` build` 

![image-20191216115103403](Setting Up a Gradle Project.assets/image-20191216115103403.png)

## Setup the Main class

First, create your `Main` class somewhere in `src` $$\rightarrow$$ `Main` $$\rightarrow$$ `java` eg. 

```
src/main/java/awesomeProject
```



You need to make the following edits to `build.gradle` located in your root class path:

1. Add the application plugin, eg:

	```groovy
    plugins {
         id 'application'
    }
   ```
   
2. Define your main class name with relative path class, eg:

   ```groovy
   application {
       mainClassName = "org.Main"
   }
   ```
   
3. You should now be able to run the main via Gradle tasks.
	
	In the `Gradle` panel to the right, ook for `ProjectName` $$\rightarrow$$ `Tasks` $$\rightarrow$$ `application` $$\rightarrow$$ ` run` 
	



## Install Lombok

> Note, this is only necessary if Lombok hasn't been installed, yet.

The [Jetbrains IntelliJ IDEA](https://www.jetbrains.com/idea/) editor is compatible with lombok. 	

Add the [Lombok IntelliJ plugin](https://plugins.jetbrains.com/plugin/6317) to add lombok support for IntelliJ: 		

-  			Go to `File > Settings > Plugins` 		
-  			Click on `Browse repositories...` 		
-  			Search for `Lombok Plugin` 		
-  			Click on `Install plugin` 		
-  			Restart IntelliJ IDEA 		

 		You can also check out [Setting up Lombok with Eclipse and IntelliJ](https://www.baeldung.com/lombok-ide), a blog article on baeldung. 	

## Setup Lombok

Assuming the Lombok plugin is installed, all you need now are a few edits to the `build.gradle` 

1. Add the following line to your `plugins`:

    ```groovy
    plugins {
        id "io.freefair.lombok" version "4.1.6"
    }
    ```

2. Add the following line (if not already present) to `repositories` :

   ```groovy
   repositories {
       mavenCentral()
   }
   ```

## Setup Log4j

1. Create a `log4j.properties` in the root of the `src` folder 

   Here, we setup logging levels and properties, eg:

   ```properties
   # This sets the global logging level and specifies the appenders
   log4j.rootLogger=INFO, toConsole, toFile
    
   # settings for the console appender
   log4j.appender.toConsole=org.apache.log4j.ConsoleAppender
   log4j.appender.toConsole.layout=org.apache.log4j.PatternLayout
   log4j.appender.toConsole.layout.ConversionPattern=%-4r [%t] %-5p %c %x - %m%n
   
   # settings for the file appender
   log4j.appender.toFile=org.apache.log4j.RollingFileAppender
   log4j.appender.toFile.File=c:\temp\logs\example.log
   log4j.appender.toFile.MaxFileSize=100KB
   # Keep one backup file
   log4j.appender.toFile.MaxBackupIndex=1
   log4j.appender.toFile.layout=org.apache.log4j.PatternLayout
   log4j.appender.toFile.layout.ConversionPattern=%p %t %c - %m%n
   ```
   
2. Add the following lines to your `build.gradle`:

   (Add this anywhere)

   ```groovy
   apply plugin: 'idea'
   ```

   (Add this to your `dependencies`)

   ```groovy
   compile group: 'org.slf4j', name: 'slf4j-log4j12', version: '1.7.29'
   compile group: 'org.slf4j', name: 'slf4j-api', version: '1.7.29'
   ```

   

If Lombok is configured, then any class can use logging by prefixing the class with `@Slf4j`. 

You can then create log entries using `log.` notatioan, eg `log.debug("--> launch Main()");` 

### Important to note ...

- log4j assumes that levels are ordered and suppresses all logs of lower levels. Thus, we have 

  `DEBUG < INFO < WARN < ERROR < FATAL`.

- Set your level to `DEBUG` if you want to see all the logging messages.