import json
import urllib.request
import urllib.parse
import os

def lambda_handler(event, context):
    request_body = event
    ip_address = os.environ.get('ipAddress')
    employeeId=request_body['employeeId']
    api_endpoint = 'http://' + ip_address + ':8083/update/employee/'+employeeId
    del request_body['employeeId']
    modified_request_body=json.dumps(request_body).encode('utf-8')

    try:
        req = urllib.request.Request(api_endpoint, data=modified_request_body, method='PUT', headers={'Content-Type': 'application/json'})
        with urllib.request.urlopen(req) as response:
            data = response.read().decode(),
            return {
            'statusCode': response.status,
            'body': "employee updated"
                    }
    except urllib.error.HTTPError as e:
            return {
                'statusCode': 500,
                'body': "error in updation"
                }