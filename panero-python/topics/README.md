## PaNeRo

### Message-based communication with Python

> Topics and Durable Subscriptions

The `publish-optimizations.py` script sends two messages - one with category `solar` and another one with category `wind` - to the topic `Topic.PANERO.1.OPTIMIZATION_RESULTS.wolfhagen`.

The scripts `listener-solar.py` and `listener-wind.py` subscribe durably to topic `Topic.PANERO.1.OPTIMIZATION_RESULTS.wolfhagen` selecting only messages that match their category (Selective Consumers).

#### Usage

```shell
python listener-solar.py
python listener-wind.py
python publish-optimizations.py
```
