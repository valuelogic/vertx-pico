[![CircleCI](https://circleci.com/gh/valuelogic/vertx-pico/tree/master.svg?style=shield&circle-token=6de10fc4566df9ebd87b0eeea471e7f13386cb55)](https://circleci.com/gh/valuelogic/vertx-pico/tree/master)

# Vertx PicoContainer verticle factory

Dependency injection for verticles

# Usage

Add dependency in Gradle:

```groovy
dependencies {
  compile "one.valuelogic.vertx-pico:0.1"
}
```

or in Maven:

```xml
<dependencies>
   <dependency>
      <groupId>one.valuelogic</groupId>
      <artifactId>vertx-pico</artifactId>
      <version>0.1</version>
   </dependency>
</dependencies>   
```

To be used, it should be registered in Vertx and verticle should be deployed with `pico:` prefix.


```java
    vertx.registerVerticleFactory(new PicoVerticleFactory(picoContainer));
    vertx.deployVerticle("pico:"+ExampleVerticle.class.getName());
```
