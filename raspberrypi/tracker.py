import gps
import httplib, urllib
import time
import threading
import socket
 
class GpsPoller(threading.Thread):

  def __init__(self):
    threading.Thread.__init__(self)
    self.gpsd = gps.gps(mode=gps.WATCH_ENABLE)
    self.current_value = self.gpsd.next()

  def get_current_value(self):
    return self.current_value

  def run(self):
    while True:
      self.current_value = self.gpsd.next()

if __name__ == "__main__":
  try:
    gpsp = GpsPoller()
    gpsp.daemon = True
    gpsp.start()
    while True:
      cur = gpsp.get_current_value() 
      if cur["class"] == "TPV":
        if hasattr(cur, "lat"):
          data = {"bike_rental_id": "a00j00000041nvH", "lat": cur.lat, "lon": cur.lon, "time": cur.time}
          headers = {"Content-type": "application/x-www-form-urlencoded"}
          try:
            conn = httplib.HTTPConnection("foo.herokuapp.com")
            conn.request("POST", "/", urllib.urlencode(data), headers)
            conn.close()
            print data
          except (httplib.HTTPException, socket.error) as ex:
            print ex
      time.sleep(60) # once per minute

  except KeyboardInterrupt:
    quit()
