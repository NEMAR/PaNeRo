## PaNeRo

### Message-based communication with Python

> Working with Queues

The `publish.py` scripts publishes ten persistent messages to the central inbound queue `Queue.PANERO.1.DATA`.

The `listener.py` subscribes to the corresponding tenant queue `Queue.PANERO.1.INPUT.default` (assuming that the PaNeRo Gateway has been started with tenant identifier `default`).

#### Usage

```shell
python listener.py
python publish.py
```
