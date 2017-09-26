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

    private String apiSecret;
    private String apiKey;
    private String baseUrl;

    public RestJavaUtil(String baseUrl,String apiKey,String apiSecret){
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public JSONObject removeOrder(String orderId) throws IOException {
        TreeMap<String, String> tm = new TreeMap<String, String>();
        tm.put("apiKey", apiKey);
        tm.put("orderId", orderId);
        tm.put("sign", sign(tm));
        return post(baseUrl+"/api/v1/spot/btc/removeUserOrder", convertTostr(tm));
    }

    public JSONObject queryOrder(String page, String psize) throws IOException {
        TreeMap<String, String> tm = new TreeMap<String,String>();
        tm.put("apiKey", apiKey);
        tm.put("page", page);
        tm.put("psize", psize);
        tm.put("sign", sign(tm));
        return post(baseUrl+"/api/v1/spot/btc/queryUserOrder", convertTostr(tm));
    }

    public JSONObject addOrder(String direction, String price, String amount) throws IOException {
        TreeMap<String, String> tm = new TreeMap<String, String>();
        tm.put("apiKey", apiKey);
        tm.put("direction", direction);
        tm.put("price", price);
        tm.put("amount", amount);
        tm.put("sign", sign(tm));
        return post(baseUrl+"/api/v1/spot/btc/addUserOrder", convertTostr(tm));
    }

    public JSONObject queryAccount() throws IOException {
        TreeMap<String, String> tm = new TreeMap<String,String>();
        tm.put("apiKey", apiKey);
        tm.put("sign", sign(tm));
        String convertTostr = convertTostr(tm);
        return post(baseUrl+"/api/v1/spot/btc/queryUserAccount", convertTostr);
    }

    public JSONObject ticker() throws IOException {
        return get(baseUrl+"/api/v1/spot/btc/ticker");
    }

    private JSONObject get(String url) throws IOException {
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

    private JSONObject post(String url,String body) throws IOException {
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

    private CloseableHttpClient createHttpClient(){
        return HttpClients.custom().build();
    }
    private String convertTostr(TreeMap<String, String> tm) {

        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = tm.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            sb.append(next.getKey()).append("=").append(next.getValue()).append("&");
        }

        return sb.substring(0, sb.length() - 1);
    }

    private  String sign(TreeMap<String, String> params){
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, String> param = iterator.next();
            sb.append(param.getKey()).append("=").append(param.getValue().toString()).append("&");
        }
        sb.append("apiSecret=").append(apiSecret);
        return Md5Util.md5Coin(sb.toString());
    }


}
