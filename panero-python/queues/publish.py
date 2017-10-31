#!/usr/bin/env python

import time
import sys
import os
import stomp

user = os.getenv('ACTIVEMQ_USER') or None
password = os.getenv('ACTIVEMQ_PASSWORD') or None
host = os.getenv('ACTIVEMQ_HOST') or 'localhost'
port = os.getenv('ACTIVEMQ_PORT') or 61613
destination = sys.argv[1:2] or ['/queue/Queue.PANERO.1.INPUT_PLAIN']
destination = destination[0]

messages = 10
data = '{"tenant":"wolfhagen","measurements":[{"name":"watts","tags":[{"name":"house_id","value":"0"},{"name":"household_id","value":"0"},{"name":"plug_id","value":"4"},{"name":"debs","value":"2014"}],"time":1461868810999,"precision":"MILLISECONDS","value":0.0,"value_metadata":[{"name":"property","value":1}]}]}'

conn = stomp.Connection12([(host, port)], use_ssl=False)
conn.start()
conn.connect(user, password, wait=True)

for i in range(0, messages):
  conn.send(destination, data, 'text/plain', { 'persistent': 'true' })
conn.disconnect()
