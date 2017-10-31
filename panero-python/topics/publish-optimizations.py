#!/usr/bin/env python

import time
import sys
import os
import stomp

user = os.getenv('ACTIVEMQ_USER') or None
password = os.getenv('ACTIVEMQ_PASSWORD') or None
host = os.getenv('ACTIVEMQ_HOST') or 'localhost'
port = os.getenv('ACTIVEMQ_PORT') or 61613
destination = sys.argv[1:2] or ['/topic/Topic.PANERO.1.OPTIMIZATION_RESULTS.wolfhagen']
destination = destination[0]

conn = stomp.Connection12([(host, port)], use_ssl=False)
conn.start()
conn.connect(user, password, wait=True)

# Message 1: Solar
conn.send(destination, '{"category": "solar", "optimization": true}', 'text/plain', { 'tenant': 'wolfhagen', 'category': 'solar' ,'persistent': 'true' })
# Message 2: Windpark
conn.send(destination, '{"category": "wind", "optimization": true}', 'text/plain', { 'tenant': 'wolfhagen', 'category': 'wind', 'persistent': 'true' })

conn.disconnect()
