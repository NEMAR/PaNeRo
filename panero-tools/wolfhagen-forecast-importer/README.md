
CSV Importer for Wolfhagen's "Residuallast" Forecasts
-----------------------------------------------------

This tool is used by Stadtwerke Wolfhagen to import their "Residuallast" forecasts.
The forecasts are staged on a FTP location as CSV files twice a day.

We connect to this FTP location and check for new CSV files (using Spring Integration).
New files are downloaded and stored in a local directory in order to track what file has already been processed.
After downloading, the file content is parsed (using Spring Batch) and send to a PaNeRo Gateway instance (HTTP endpoint) in the appropriate message format.

The data is treated like a time series and we create a single measurement in InfluxDB (`residuallast.forecast`).
We create additional tags in order to identify each forecast. 
We use the actual filename of the CSV file and the timestamp (in ISO format) where the data has been reported as `tags`.

The tool runs as a daemon and checks the FTP location in intervals.
A log file is written at `/var/log/wolfhagen-forecast-importer.log`.

### Installation (as a Daemon)

* Extract the binary archive to `/opt/wolfhagen-forecast-importer`

* Create a symlink:

    ```
    ln -s /opt/wolfhagen-forecast-importer/wolfhagen-forecast-importer.jar /etc/init.d/wolfhagen-forecast-importer
    ```

* Flag it to start automatically:

    ```
    update-rc.d wolfhagen-forecast-importer defaults
    ```

* Run as non-root:

    ```
    chown -R <user>:<user> /opt/wolfhagen-forecast-importer
    ```

* Manage the service:
    
    ```
    service wolfhagen-forecast-importer start|stop|status
    ```

### Configuration

You can change the configuration parameters in `config/application.yml` in order to adapt the FTP settings to your needs.
