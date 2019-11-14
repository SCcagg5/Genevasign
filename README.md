# Geneva sign


### Routes's Basics:

Routes | Methods | Params | Return |
-|-|-|-|
`/test/` | POST, GET |  / | / |
`/login/` | POST | pass | token |
`/sign/` | POST | token, id, firstname, lastname, digest, name | response_from_api |

### Parameters

```javascript
{
  id: "1",                      //(string)
  firstname: "YOUR_NAME",       //(string)
  lastname: "YOUR_NAME",        //(string)
  digest: "YOUR_PDF",           //(sha256 of your pdf)
  name: "name of the doc"       //(string | max 20)
}
```

### Return template

```javascript
{
    "queryInfos": {
        "route": "/test/",
        "params": []
    },
    "status": 200,
    "error": null,
    "data": null,
    "succes": true
}
```
