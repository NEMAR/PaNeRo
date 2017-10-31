
OpenWeatherMap Integration
--------------------------

Example HTTP Request:

```http
GET /data/2.5/weather?appid=d190b4ffc58bb79dfce732f39bf9b232&units=metric&id=2842632&lang=de HTTP/1.1
Host: api.openweathermap.org
Accept: application/json
```

The respective `JSON` response does look like the following and is mapped to [`WeatherResponse.java`](detail/WeatherResponse.java):

```json
{
  "coord": {
    "lon": 6.75,
    "lat": 49.32
  },
  "weather": [
    {
      "id": 300,
      "main": "Drizzle",
      "description": "leichtes Nieseln",
      "icon": "09d"
    }
  ],
  "base": "cmc stations",
  "main": {
    "temp": 18.96,
    "pressure": 1010,
    "humidity": 82,
    "temp_min": 16,
    "temp_max": 22.5
  },
  "wind": {
    "speed": 2.6,
    "deg": 170
  },
  "clouds": {
    "all": 75
  },
  "dt": 1464870620,
  "sys": {
    "type": 1,
    "id": 4890,
    "message": 0.0043,
    "country": "DE",
    "sunrise": 1464838247,
    "sunset": 1464895921
  },
  "id": 2842632,
  "name": "Saarlouis",
  "cod": 200
}
```
