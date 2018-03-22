RMLC-api
=========

## How to use RMLC-api as a Maven dependency
* clone or download and install the project in your local maven repo as follows 
```
    mvn clean install
```
* put the following fragments into your `pom.xml`

```xml        
    <dependency>
        <groupId>es.upm.fi.dia.oeg.rmlc</groupId>
        <artifactId>rmlc-api-jena-binding</artifactId>
        <version>0.6.1-SNAPSHOT</version>
    </dependency>

    <dependency>
        <groupId>es.upm.fi.dia.oeg.rmlc</groupId>
        <artifactId>rmlc-api-core</artifactId>
        <version>0.6.1-SNAPSHOT</version>
    </dependency>
```


## Release history

<a name="v0.6.1"></a>
### 22 March, 2018 ::  Version 0.6.1 
* fork made from [R2RML-api](https://github.com/R2RML-api/R2RML-api), changes to RMLC were applied
