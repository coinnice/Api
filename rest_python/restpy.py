import rest_python
# 请将API_SECRET API_KEY 更换为您在coinnice申请的
API_SECRET = "69aebb12-6b47-4858-b5ea-7056561466e4"
API_KEY = "31c068b8-a61b-40d1-82e8-d6b6720274af"
HOST = "www.coinnice.com"
PORT = 80

rest = rest_python.RestPythonUtil.RestPythonService(host=HOST, port=PORT, api_key=API_KEY, api_secret=API_SECRET)
print("获取行情信息", rest.ticker())
print("查询账户信息", rest.query_account())
print("下单", rest.add_order("0","4500","0.1"))
all_order = rest.query_order("1", "10")
print("查询挂单", all_order)
if all_order["result"]:
    for order in all_order["obj"]:
        print("撤单",rest.remove_order(order["id"]))
