import requests
import json
import hashlib

class sign:
    def __init__(self, id, firstname, lastname):
        self.id = str(id)
        self.firstname = firstname
        self.lastname = lastname

    def push(self, name, digest):
        url = "https://qualified-signature-gateway.hortis.ch:8443/qualified-signature/companyRequest"
        headers = {'content-type': "application/json"}
        payload = {
        "signatureRequest": {
                "requestId": "requestForSignUsingPOST",
                "digest": digest,
                "language": "fr",
                "message": "Voulez vous signer " + name + " ?",
                "target":
                {
                    "companyFullName": "SmartCo Sarl",
                    "plateformName": "smartco",
                    "serviceName": "smartco",
                    "clientId": "SmartCo",
                    "signerId": self.id,
                    "firstName": self.firstname,
                    "lastName": self.lastname
                }

            }
        }

        response = requests.request("POST", url, data=json.dumps(payload), headers=headers)

        url = "https://qualified-signature-gateway.hortis.ch:8443/qualified-signature/userSign"
        headers = {'content-type': "application/json"}
        payload = {
            "signatureRequest": {
                "requestId": str(response.text),
                "digest": str(digest),
                "language": "fr",
                "message": "Voulez vous signer " + name + " ?",
                "target":
                {
                    "companyFullName": "SmartCo Sarl",
                    "plateformName": "smartco",
                    "serviceName": "smartco",
                    "clientId": "SmartCo",
                    "signerId": str(self.id),
                    "firstName": str(self.firstname),
                    "lastName": str(self.lastname)
                }

            }
        }
        response = requests.request("POST", url, data=json.dumps(payload), headers=headers)
        return [True, json.loads(response.text)]
