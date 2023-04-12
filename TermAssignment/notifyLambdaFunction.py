import json
import urllib.request
import urllib.parse
import os

def handler(event, context):
    request_body = event
    ip_address = os.environ.get('ipAddress')
    api_endpoint = 'http://' + ip_address + ':8083/notify'
    modified_request_body=json.dumps(request_body).encode('utf-8')
    try:
        req = urllib.request.Request(api_endpoint, data=modified_request_body, method='POST', headers={'Content-Type': 'application/json'})
        with urllib.request.urlopen(req) as response:
            data = response.read().decode()
            return {
            'statusCode': response.status,
            'body': json.loads(data)
                    }
    except urllib.error.HTTPError as e:
        return {
        'statusCode': e.code,
        'body': json.loads(e.read().decode())
                }