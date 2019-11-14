from Crypto.PublicKey import RSA
from Crypto.Util import asn1
from base64 import b64decode
from base64 import b64encode
import sys
from Crypto.Cipher import PKCS1_OAEP
from Crypto import Random

with open('private','r') as fk:
	privatekey  = fk.read()
	fk.close()

with open('pub','r') as fp:
	pub = fp.read()
	fp.close()

msg ="Ym85MmMvajIxQS9FdUhrTW4wdWRTWkZqSFFSK1FHZXI0bWVxUmdMSGxpTEFBeE9DdllSR2YyYnR5ZHlHbHh4bStvc1JJNjlEbXY2L1dJRDZvaFlFOGYxREJmY1lscElwdEJVdEpqU05iT1M0azJLbGtvOCtrREZ5RkZUNko1azg0MitwWDRQUEVkTjZDUlk1dDdtUjc5THdpV0xmOGtJQ2JsVnZmc0lPL1ZNPQ=="
print("Private key (first 200 chars): ",privatekey[:200])
print("\nCipher: ",msg)

key = RSA.import_key(privatekey)

decryptor = PKCS1_OAEP.new(key)
decrypted = decryptor.decrypt(msg)

print("\nDecrypted: ",decrypted)
