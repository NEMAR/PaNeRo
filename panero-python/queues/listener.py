#!/usr/bin/env python

import time
import sys
import os
import stomp
import atexit

user = os.getenv('ACTIVEMQ_USER') or None
password = os.getenv('ACTIVEMQ_PASSWORD') or None
host = os.getenv('ACTIVEMQ_HOST') or 'localhost'
port = os.getenv('ACTIVEMQ_PORT') or 61613
destination = sys.argv[1:2] or ['/queue/Queue.PANERO.1.OPTIMIZATION.wolfhagen']
destination = destination[0]

class MyListener(object):

  def __init__(self, conn):
    self.conn = conn
    self.count = 0
    self.start = time.time()

  def on_error(self, headers, message):
    print('Received an error: %s' % message)

  def on_message(self, headers, message):
    self.count += 1
    print('%i: %s' % (self.count, message))

conn = stomp.Connection12([(host, port)], use_ssl=False)
conn.set_listener('MyListener', MyListener(conn))
conn.start()
conn.connect(user, password, wait=True)
conn.subscribe(destination, 'default', ack='auto')

def exit_handler():
  conn.disconnect()
atexit.register(exit_handler)

print('Waiting for messages...')
while 1:
  time.sleep(10)
