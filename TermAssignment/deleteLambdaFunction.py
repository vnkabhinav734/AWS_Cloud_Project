import json
import urllib.request
import urllib.parse
import os

def lambda_handler(event, context):
    request_body = event
    ip_address = os.environ.get('ipAddress')
    employeeId=request_body['employeeId']
    api_endpoint = 'http://' + ip_address +':8083/delete/employee/' + employeeId
    try:
        req = urllib.request.Request(api_endpoint, method='DELETE', headers={'Content-Type': 'application/json'})
        with urllib.request.urlopen(req) as response:
            data = response.read().decode()
            return {
            'statusCode': response.status,
            'body': "employee deleted"
                    }
    except urllib.error.HTTPError as e:
            return {
            'statusCode': e.code,
            'body': "error in deletion"
                    }