
### PaNeRo

Collection of Commonly Used Components
--------------------------------------

### Data Model

#### Time Series Data:

Default time series data model that can be used to send data to queue `Queue.PANERO.1.INPUT_PLAIN` on **PaNeRo Gateway**.

```
{
    tenant: string,
    measurements: [{
        name: string,
        time: long,
        precision: enum,
        tags: [{
            name: string,
            value: string
        }],
        value: number,
        value_metadata: [{
            name: string,
            value: string
        }]
    }]
}
```

Possible values for field `precision`:

```
NANOSECONDS
MICROSECONDS
MILLISECONDS
SECONDS
MINUTES
HOURS
DAYS
```

---

**Example**

```json
{
  "tenant": "wolfhagen",
  "measurements": [
    {
      "name": "temp",
      "time": 1377986402,
      "precision": "SECONDS",
      "tags": [
        {
          "name": "city",
          "value": "wolfhagen"
        },
        {
          "name": "meter_id",
          "value": 1004711
        }
      ],
      "value": 22.25,
      "value_metadata": [
        {
          "name": "unit",
          "value": "celsius"
        }
      ]
    }
  ]
}
```
