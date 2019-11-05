RMLC-api
=========

## How to use RMLC-api as a Maven dependency
* clone or download and install the project in your local maven repo as follows 
```
    mvn clean install
```
* copy the jars in the target folder in the lib folder of your maven project
* put the following fragments into your `pom.xml`

```xml        
    <dependency>
        <groupId>es.upm.fi.dia.oeg.rmlc</groupId>
        <artifactId>rmlc-api-jena-binding</artifactId>
        <version>1.5.1</version>
        <scope>system</scope>
        <systemPath>${pom.basedir}/lib/rmlc-api-jena-binding-1.5.1.jar</systemPath>
    </dependency>

    <dependency>
        <groupId>es.upm.fi.dia.oeg.rmlc</groupId>
        <artifactId>rmlc-api-core</artifactId>
        <version>1.5.1</version>
        <scope>system</scope>
        <systemPath>${pom.basedir}/lib/rmlc-api-core-1.5.1.jar</systemPath>
    </dependency>
```
