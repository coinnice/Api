package rest_java;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public class RestJava {

    /**
     * API_SECRET 和 API_KEY需替换为您在COINNICE平台申请的
     */
    private static final String API_KEY = "xxxx";
    static final String API_SECRET = "xxxx";
    private static final String BASE_URL = "https://www.coinnice.com";

    public static void main(String[] args) throws IOException {

        RestJavaUtil rest = new RestJavaUtil(BASE_URL, API_KEY, API_SECRET);

        System.out.println("获取行情信息");
        JSONObject ticker = rest.ticker();
        System.out.println(ticker.toJSONString());

        System.out.println("查询账户信息");
        JSONObject account = rest.queryAccount();
        System.out.println(account.toJSONString());

        System.out.println("下单");
        JSONObject order = rest.addOrder("0", "4500", "0.1","1");
        System.out.println(order.toJSONString());


        System.out.println("查询挂单");
        JSONObject allOrder = rest.queryOrder("1", "10");
        System.out.println(allOrder.toJSONString());

        JSONArray orders = allOrder.getObject("obj", JSONArray.class);
        for (int i = 0; i < orders.size() ; i++) {
            System.out.println("撤单");
            JSONObject removeOrder = rest.removeOrder(orders.getJSONObject(i).getString("id"));
            System.out.println(removeOrder.toJSONString());
        }
    }

}
