import json
import urllib.request
import urllib.parse
import os

def lambda_handler(event, context):
    request_body = event
    ip_address = os.environ.get('ipAddress')
    response = None
    url = 'http://' + ip_address + ':8083/getAllEmployee'
    with urllib.request.urlopen(url) as response:
        data = response.read().decode()
        print('API response:', data)
        return json.loads(data)