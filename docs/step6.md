## 7. Use Solr Remote Backend

In this step we show how to use a Remote Solr Server as backend. Additionally we learn how
change basic configurations of the search lib.

### 7.1 Dependencies

In order to use a Solr Remote Backend we have to switch the dependency to this.

```xml
<dependency>
    <groupId>com.rbmhtechnology.vind</groupId>
    <artifactId>remote-solr-server</artifactId>
    <version>${vind.version}</version>
</dependency>
```

### 7.2 Configuration

The searchlib uses property files for configuration. It comes with some basic configurations (like pagesize) that can be changed. 
 For configuring the remote host you have to place a file called searchlib.properties in the classpath which includes
 the host information.

```
//configure http client
server.solr.host=http://localhost:8983/solr/searchindex

//configure zookeeper client
server.solr.cloud=true
server.solr.host=zkServerA:2181,zkServerB:2181,zkServerC:2181
server.solr.collection=collection1

//change pagesize
search.result.pagesize=7
```
 
In addition to property file the static configuration interface allows also changes on runtime.
```java
SearchConfiguration.set(SearchConfiguration.SERVER_SOLR_HOST, "http://example.org/solr/core");
```
   
If you cannot (or do not want to) change the dependency on different profiles you can have also 2 server dependency on
classpath and select one by adding a configuration property. Currently 2 solr server provider (embedded and remote) are
supported.
```java
SearchConfiguration.set(SearchConfiguration.SERVER_SOLR_PROVIDER, "com.rbmhtechnology.vind.solr.EmbeddedSolrServerProvider");
SearchConfiguration.set(SearchConfiguration.SERVER_SOLR_PROVIDER, "com.rbmhtechnology.vind.solr.RemoteSolrServerProvider");
```

*Attention*: If you want to connect via zookeeper connection string, in addition to the host also the collection has to set.
```java
SearchConfiguration.set(SearchConfiguration.SERVER_SOLR_PROVIDER, "com.rbmhtechnology.vind.solr.RemoteSolrServerProvider");
SearchConfiguration.set(SearchConfiguration.SERVER_SOLR_CLOUD, true);
SearchConfiguration.set(SearchConfiguration.SERVER_SOLR_HOST, "zkServerA:2181,zkServerB:2181,zkServerC:2181");
SearchConfiguration.set(SearchConfiguration.SERVER_SOLR_COLLECTION, "collection1");
```
   
*HINT*
If you want to test things with a standalone Solr Server we created a small script that helps you with that. The script is located
in the main directory of the project. Usage:

* `./solr_remote.sh start` downloads solr (if necessary), configures the server and starts it
* `./solr_remote.sh stop` stops the running server