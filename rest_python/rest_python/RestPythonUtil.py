import http.client
import json
import hashlib


class RestPythonService:
    """接口访问工具类"""
    __apiKey = ''
    __apiSecret = ''
    __host = ''
    __port = 443

    def __init__(self, host, port, api_key, api_secret):
        self.__apiKey = api_key
        self.__port = port
        self.__apiSecret = api_secret
        self.__host = host

    def ticker(self) -> dict:
        h = http.client.HTTPConnection(host=self.__host, port=self.__port)
        h.request("GET", '/api/v1/spot/btc/ticker')
        r = h.getresponse()
        data = None
        if r.status == 200:
            data = json.load(r)
        h.close()
        return data

    def query_account(self) -> dict:
        params = {
            "apiKey": self.__apiKey
        }
        params["sign"] = self.__sign(params)
        return self.__post(url="/api/v1/spot/btc/queryUserAccount", body=self.__convert_to_str(params))

    def __convert_to_str(self, params: dict) -> str:
        l = []
        for key, val in params.items():
            l.append(key)
            l.append("=")
            l.append(val)
            l.append("&")
        return ''.join(l)[:-1]

    def __post(self, url: str, body: str) -> dict:
        headers = {"Content-type": "application/x-www-form-urlencoded"}
        conn = http.client.HTTPConnection(host=self.__host, port=self.__port)
        conn.request(method="POST", url=url, body=body, headers=headers)
        r = conn.getresponse()
        data = {}
        if r.status == 200:
            data = json.load(r)
        conn.close()
        return data

    def __sign(self, params: dict) -> str:
        items = params.items()
        l = []
        for key, val in sorted(items):
            l.append(key)
            l.append("=")
            l.append(val)
            l.append("&")
        l.append("apiSecret=")
        l.append(self.__apiSecret)
        m = hashlib.md5()
        m.update(bytes(''.join(l), "UTF-8"))
        return str(m.hexdigest())

    def add_order(self,direction:str,price:str,amount:str) -> dict:
        params={
            "apiKey":self.__apiKey,
            "direction":direction,
            "price":price,
            "amount":amount
        }
        params["sign"]=self.__sign(params)
        return self.__post(url="/api/v1/spot/btc/addUserOrder",body=self.__convert_to_str(params))

    def query_order(self, page:str, psize:str) -> dict:
        params ={
            "apiKey":self.__apiKey,
            "page": page,
            "psize": psize
        }
        params["sign"] = self.__sign(params)
        return self.__post(url="/api/v1/spot/btc/queryUserOrder", body=self.__convert_to_str(params))

    def remove_order(self, orderId:str):
        params = {
            "apiKey": self.__apiKey,
            "orderId": orderId
        }
        params["sign"] = self.__sign(params)
        return self.__post(url="/api/v1/spot/btc/removeUserOrder", body=self.__convert_to_str(params))
        pass

