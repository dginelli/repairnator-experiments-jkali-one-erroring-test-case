# Application information

see <http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-application-info>

Application information exposes various information collected from all InfoContributor beans defined in your ApplicationContext. Spring Boot includes a number of auto-configured InfoContributors and you can also write your own.

You can customize the data exposed by the info endpoint by setting info.* Spring properties. All Environment properties under the info key will be automatically exposed. For example, you could add the following to your application.properties:

## application.properties

```ini
info.app.encoding=@project.build.sourceEncoding@
info.app.java.source=@java.version@
info.app.java.target=@java.version@
```

## application.yml

```yml
info:
  app:
    encoding: @project.build.sourceEncoding@
    java:
      source: @maven.compiler.source@
      target: @maven.compiler.target@
```

To expand info properties **at build time** (so you have to recompile if you change application.properties) we also have to configure Maven accordingly (see http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-automatic-expansion).

## pom.xml

```xml
...
<build>
  <resources>
    <resource>
      <directory>src/main/resources</directory>
      <filtering>true</filtering>
    </resource>
  </resources>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-resources-plugin</artifactId>
      <version>2.7</version>
      <configuration>
        <delimiters>
          <delimiter>@</delimiter>
        </delimiters>
        <useDefaultDelimiters>false</useDefaultDelimiters>
      </configuration>
    </plugin>
    ...
  </plugins>
  ...
</build>
```

The useDefaultDelimiters property is important if you are using standard Spring placeholders in your configuration (e.g. ${foo}). These may be expanded by the build if that property is not set to false.

## Maven Compiler Source and Target version

To output the dedicated source and target compiler version, we add the Maven compiler plugin and the according properties (see <https://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-source-and-target.html>):

### pom.xml

```xml
<properties>
  <maven.compiler.source>1.8</maven.compiler.source>
  <maven.compiler.target>1.8</maven.compiler.target>
</properties>
...
<build>
  ...
  <plugins>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.6.1</version>
        <configuration>
          <source>${maven.compiler.source}</source>
          <target>${maven.compiler.target}</target>
        </configuration>
      </plugin>
    ...
  </plugins>
  ...
</build>
```

### application.properties

```ini
info.app.encoding=@project.build.sourceEncoding@
info.app.java.source=@maven.compiler.source@
info.app.java.target=@maven.compiler.target@
```

### application.yml

```yml
info:
  app:
    encoding: @project.build.sourceEncoding@
    java:
      source: @maven.compiler.source@
      target: @maven.compiler.target@
```

## Default project encoding

Even if the info output already has shown "UTF-8" as project encoding, it is safer to explicitely add it to the properties with the default key "project.build.sourceEncoding":

### pom.xml

```xml
<properties>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

## Project information

It also seems to be a good idea to output information about the application itself:

### application.properties

```ini
info.app.project.name=@project.name@
info.app.project.groupId=@project.groupId@
info.app.project.artifactId=@project.artifactId@
info.app.project.version=@project.version@
```

### application.yml

```yml
info:
  app:
    ...
    project:
      name: '@project.name@'
      groupId: @project.groupId@
      artifactId: @project.artifactId@
      version: @project.version@
```

## Response Sample

Response to http://localhost:9001/info.json:

```json
{
  "_links": {
    "self": {
      "href": "http://localhost:9001/info.json"
    }
  },
  "app": {
    "project": {
      "name": "DigitalCollections: Blueprints 3: REST Webservice Server (Frontend IMPL Spring Boot)",
      "groupId": "de.digitalcollections.blueprints",
      "version": "1.0.0-SNAPSHOT",
      "artifactId": "rest-server-springboot"
    },
    "java": {
      "source": "1.8",
      "target": "1.8"
    },
    "encoding": "UTF-8"
  }
}
```
