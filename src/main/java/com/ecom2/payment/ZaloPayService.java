package com.ecom2.payment;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
//import vn.zalopay.crypto.HMACUtil;
import com.ecom2.payment.vn.zalopay.crypto.HMACUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ZaloPayService {

    @Value("${zalopay.appid}")
    private String appId;

    @Value("${zalopay.key1}")
    private String key1;

    @Value("${zalopay.endpoint}")
    private String endpoint;

    public String createOrder(long amount) throws Exception {
        final Map<String, String> embeddata = new HashMap<>() {{
            put("merchantinfo", "embeddata123");
        }};

        final Map<String, Object> item = new HashMap<>() {{
            put("itemid", "knb");
            put("itemname", "kim nguyen bao");
            put("itemprice", 198400);
            put("itemquantity", 1);
        }};

        String appTransId = getCurrentTimeString("yyMMdd") + "_" + UUID.randomUUID();
        long appTime = System.currentTimeMillis();

        Map<String, Object> order = new HashMap<>();
        order.put("appid", appId);
        order.put("apptransid", appTransId);
        order.put("apptime", appTime);
        order.put("appuser", "demo");
        order.put("amount", amount);
        order.put("description", "ZaloPay Integration Demo");
        order.put("bankcode", "zalopayapp");
        order.put("item", new JSONObject(Collections.singletonList(item)).toString());
        order.put("embeddata", new JSONObject(embeddata).toString());

        String data = appId + "|" + appTransId + "|" + "demo" + "|" + amount + "|" + appTime + "|" + order.get("embeddata") + "|" + order.get("item");
        String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, key1, data);
        order.put("mac", mac);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(endpoint);

        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, Object> entry : order.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        post.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            resultJsonStr.append(line);
        }
        return resultJsonStr.toString();
    }

    private String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }
}
