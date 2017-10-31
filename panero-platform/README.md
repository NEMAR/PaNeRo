### PaNeRo

Platform
--------

The **PaNeRo Platform**, it receives messages from gateways and processes them accordingly.

[Read the documentation](../docs/PaNeRo-Platform.md)

## Gateway Mapping Fix
Since several Messages tables have `fields` and `tags` configured wrong for InfluxDB a manual mapping to prevent message overwrite is used in the MeasurementConverter.

### Copy Measurement to Tmp
`SELECT * INTO tempTable FROM originalTable GROUP BY *`

### Tables
- default: (\*) -> (\*)
- ["il1", "il2", "il3", 
        "metering_value", "metering_value_backup", "metering_value_backup2",
        "p_negative", "p_positive",
        "phase_i1_u1", "phase_i2_u2", "phase_i3_u3", "phase_u1_u2", "phase_u1_u3",
        "pl1_negative", "pl1_positive",
        "pl2_negative", "pl2_positive",
        "pl3_negative", "pl3_positive",
        "ql1_negative", "ql1_positive",
        "ql2_negative", "ql2_positive",
        "ql3_negative", "ql3_positive",
        "ul1", "ul2", "ul3"]
    (time, short_id, tenant) -> (time, short_id, tenant, message_subtype)