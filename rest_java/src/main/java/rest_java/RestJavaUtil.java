package rest_java;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

class RestJavaUtil {


    private static String API_SECRET = RestJava.API_SECRET;

    static JSONObject removeOrder(String url, String apiKey, String orderId) throws IOException {
        TreeMap<String, String> tm = new TreeMap<String, String>();
        tm.put("apiKey", apiKey);
        tm.put("orderId", orderId);
        tm.put("sign", sign(tm));
        return RestJavaUtil.post(url, convertTostr(tm));
    }

    static JSONObject queryOrder(String url, String apiKey, String page, String psize) throws IOException {
        TreeMap<String, String> tm = new TreeMap<String,String>();
        tm.put("apiKey", apiKey);
        tm.put("page", page);
        tm.put("psize", psize);
        tm.put("sign", sign(tm));
        return RestJavaUtil.post(url, convertTostr(tm));
    }

    static JSONObject addOrder(String url, String apiKey, String direction, String price, String amount) throws IOException {
        TreeMap<String, String> tm = new TreeMap<String, String>();
        tm.put("apiKey", apiKey);
        tm.put("direction", direction);
        tm.put("price", price);
        tm.put("amount", amount);
        tm.put("sign", sign(tm));
        return RestJavaUtil.post(url, convertTostr(tm));
    }

    static JSONObject queryAccount(String url, String apiKey) throws IOException {
        TreeMap<String, String> tm = new TreeMap<String,String>();
        tm.put("apiKey", apiKey);
        tm.put("sign", sign(tm));
        String convertTostr = convertTostr(tm);
        return RestJavaUtil.post(url, convertTostr);
    }

    static JSONObject ticker(String url) throws IOException {
        return RestJavaUtil.get(url);
    }

    private static JSONObject get(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = createHttpClient().execute(httpGet);
        try{
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                return JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            }
        }finally {
            if (response != null){
                response.close();
            }
        }
        return null;
    }

    private static JSONObject post(String url,String body) throws IOException {
        HttpPost post = new HttpPost(url);
        HttpEntity httpEntity = new StringEntity(body);
        post.setEntity(httpEntity);
        CloseableHttpResponse response = createHttpClient().execute(post);
        try{
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                return JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            }
        }finally {
            if (response != null){
                response.close();
            }
        }
        return null;
    }

    private static CloseableHttpClient createHttpClient(){
        return HttpClients.custom().build();
    }
    private static String convertTostr(TreeMap<String, String> tm) {

        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = tm.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            sb.append(next.getKey()).append("=").append(next.getValue()).append("&");
        }

        return sb.substring(0, sb.length() - 1);
    }

    private  static String sign(TreeMap<String, String> params){
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, String> param = iterator.next();
            sb.append(param.getKey()).append("=").append(param.getValue().toString()).append("&");
        }
        sb.append("apiSecret=").append(API_SECRET);
        return Md5Util.md5Coin(sb.toString());
    }


}
