
### PaNeRo

Java Client Library
-------------------

Java client library in order to send data to a **PaNeRo Gateway** instance.


### Usage

* Add the Java client library to your project:

    ```xml
    <dependency>
        <groupId>org.panero</groupId>
        <artifactId>panero-gateway-client</artifactId>
        <version>${version}</version>
    </dependency>
    ```

* Create a `GatewayConfiguration` and `GatewayClient`:

    ```java
    final HttpClientConfiguration configuration = new HttpClientConfiguration();
    configuration.setHostname("localhost");
    configuration.setPort("8080");
    final GatewayClient client = new DefaultHttpClient(configuration);
    ```

* Use the client to send `Measurements` to **PaNeRo Gateway**:

    ```java
    final Measurements m = Measurements.create("tenant_id")
        .measurement(Measurement.create("cpu").tag("host", "server01").value(0.64).and().buildIt())
        .and().buildIt();

    client.sendAsync(m);
    ```
