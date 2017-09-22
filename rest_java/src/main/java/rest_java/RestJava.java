package rest_java;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public class RestJava {

    static final String API_SECRET = "69aebb12-6b47-4858-b5ea-7056561466e4";
    private static final String API_KEY = "31c068b8-a61b-40d1-82e8-d6b6720274af";
    private static final String BASE_URL = "http://localhost:18080";

    public static void main(String[] args) throws IOException {

        System.out.println("获取行情信息");
        String PATH_SB_TICKER = "/api/v1/spot/btc/ticker";
        JSONObject ticker = RestJavaUtil.ticker(BASE_URL + PATH_SB_TICKER);
        System.out.println(ticker.toJSONString());

        System.out.println("查询账户信息");
        String PATH_SB_QUERY_USER_ACCOUNT = "/api/v1/spot/btc/queryUserAccount";
        JSONObject account = RestJavaUtil.queryAccount(BASE_URL + PATH_SB_QUERY_USER_ACCOUNT,API_KEY );
        System.out.println(account.toJSONString());

        System.out.println("下单");
        String PATH_SB_ADD_USER_ORDER = "/api/v1/spot/btc/addUserOrder";
        JSONObject order = RestJavaUtil.addOrder(BASE_URL + PATH_SB_ADD_USER_ORDER, API_KEY, "0", "4500", "0.1");
        System.out.println(order.toJSONString());


        System.out.println("查询挂单");
        String PATH_SB_QUERY_USER_ORDER = "/api/v1/spot/btc/queryUserOrder";
        JSONObject allOrder = RestJavaUtil.queryOrder(BASE_URL + PATH_SB_QUERY_USER_ORDER, API_KEY, "1", "10");
        System.out.println(allOrder.toJSONString());

        JSONArray orders = allOrder.getObject("obj", JSONArray.class);
        for (int i = 0; i < orders.size() ; i++) {
            System.out.println("撤单");
            String PATH_SB_REMOVE_USER_ORDER = "/api/v1/spot/btc/removeUserOrder";
            JSONObject removeOrder = RestJavaUtil.removeOrder(BASE_URL+ PATH_SB_REMOVE_USER_ORDER, API_KEY,orders.getJSONObject(i).getString("id"));
            System.out.println(removeOrder.toJSONString());
        }
    }

}
