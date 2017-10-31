
### PaNeRo

DEBS Simulator
--------------

Tool to replay the test data of the **DEBS 2014 Grand Challenge**.
The data is available at http://nemar.allweathercomputing.com/debs_2014.csv.gz.

The simulator reads the CSV data interation-based and blockwise.
Each second, the next block of data with the same timestamp is sent to **PaNeRo Gateway**.

Consider the following sample data from the CSV file (second column represents the timestamp):

```
2967740693,1379879533,82.042,0,1,0,12
2967740694,1379879533,105.303,1,1,0,12
2967740695,1379879533,208.785,0,2,0,12
2967740696,1379879534,217.717,1,2,0,12
2967740697,1379879534,2.207,0,3,0,12
2967740698,1379879535,2.081,1,3,0,12
2967740699,1379879535,0,1,3,1,12
2967740700,1379879535,0.313,0,3,1,12
2967740701,1379879536,0,1,3,2,12
2967740702,1379879536,0,1,3,2,12
```

* Iteration one will send three samples
* Iteration two will send two samples
* Iteration three will send three samples 
* ... and so on ...

> **NOTE:** Instead of using the original timestamp, the simulator always uses the current time when it sends the data to **PaNeRo Gateway**.

### Usage

* Extract the package to a directory of your choice.

* Open a terminal and navigate to the `panero-debs-simulator` directory.

* Start the simulation:

    ```shell
    java -jar panero-debs-simulator.jar --debs.file=<Path to DEBS data> --panero.gateway.hostname=<PaNeRo Gateway Host>
    ```

#### Configuration

You can change the parameters in `config/application.yml` or you can use the according command-line parameters.

| Parameter | Default | Description |
|:--------- |:------- |:----------- |
| `debs.file` | `~` | Path to DEBS data (CSV file) |
| `debs.compressed` | `true` | CSV file Gzip compressed |
| `debs.interval` | `60` | The interval in seconds when to send the next message |
| `panero.tenant` | `default` | PaNeRo Tenant name |
| `panero.gateway.hostname` | `localhost` | PaNeRo Gateway Host |
| `panero.gateway.port` | `8080` | PaNeRo Gateway HTTP Port |
