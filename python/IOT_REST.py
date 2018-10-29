from requests_pkcs12 import post,get
import json
from base64 import b64encode

url = 'https://<HOST>/iot/gateway/rest/measures/<ALTERNATE ID>'

certificate_location = '<Location to .P12 file>'
password = '<PASSPHRASE>'
headers = {'Content-type': 'application/json'}

json_data = { "capabilityAlternateId": "100", "sensorAlternateId": "1B", "measures": [{"temperature": "600"}]}; #sample payload
data=json.dumps(json_data)



# Sending data/timeseries data to the platform.
r = post(url, pkcs12_filename=certificate_location, pkcs12_password=password,data=data,headers=headers)
print(r.text)


# Recieving Data/Timeseries
user_name = "USERNAME"
password = "PASSWORD"
user_pass = user_name+":"+password
basicAuthToken = b64encode(user_pass.encode()).decode("ascii")
headers = { 'Authorization' : 'Basic %s' %  basicAuthToken }

url2 = 'https://<HOST>/iot/core/api/v1/devices/<ID>/measures?skip=0&top=100'
r = get(url2, headers=headers)

print(r.text) #The recieved string is a son string that can be converted back to son obect and used......