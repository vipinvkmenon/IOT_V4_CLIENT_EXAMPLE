var request = require('request');
var data = { "capabilityAlternateId": "100", "sensorAlternateId": "1B", "measures": [{"temperature": "600"}]};
const fs = require('fs');


var url =  "https://<HOST>/iot/gateway/rest/measures/<ALTERNATE ID>";


request.post({
        url: url,
        headers: {'content-type' : 'application/json'},
         body: JSON.stringify(data),
         agentOptions: {
        pfx: fs.readFileSync('<Location to .P12 file>'),
        passphrase: '<PASSPHRASE>'
    } 
         }, function(error, response, body){
            console.log(body);
    })