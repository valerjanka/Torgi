package com.etam;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: vryzhuk
 * Date: 5/7/14
 * Time: 12:06 PM
 */
public class Test {
    private HttpClient client = new DefaultHttpClient();
    private HttpPost post = new HttpPost("http://torgi.minjust.gov.ua/inc/php/content.php");
    {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        nvps.add(new BasicNameValuePair("sEcho", "1"));
        nvps.add(new BasicNameValuePair("iColumns", "7"));
        nvps.add(new BasicNameValuePair("iDisplayStart", "0"));
        nvps.add(new BasicNameValuePair("iDisplayLength", "200"));
        nvps.add(new BasicNameValuePair("sColumns", ""));
        nvps.add(new BasicNameValuePair("mDataProp_0", "0"));
        nvps.add(new BasicNameValuePair("mDataProp_1", "1"));
        nvps.add(new BasicNameValuePair("mDataProp_2", "2"));
        nvps.add(new BasicNameValuePair("mDataProp_3", "3"));
        nvps.add(new BasicNameValuePair("mDataProp_4", "4"));
        nvps.add(new BasicNameValuePair("mDataProp_5", "5"));
        nvps.add(new BasicNameValuePair("mDataProp_6", "6"));
        nvps.add(new BasicNameValuePair("sSearch", ""));
        nvps.add(new BasicNameValuePair("bRegex", "false"));
        nvps.add(new BasicNameValuePair("sSearch_0", ""));
        nvps.add(new BasicNameValuePair("bRegex_0", "false"));
        nvps.add(new BasicNameValuePair("bSearchable_0", "true"));
        nvps.add(new BasicNameValuePair("sSearch_1", ""));
        nvps.add(new BasicNameValuePair("bRegex_1", "false"));
        nvps.add(new BasicNameValuePair("bSearchable_1", "true"));
        nvps.add(new BasicNameValuePair("sSearch_2", ""));
        nvps.add(new BasicNameValuePair("bRegex_2", "false"));
        nvps.add(new BasicNameValuePair("bSearchable_2", "true"));

        nvps.add(new BasicNameValuePair("sSearch_3", ""));
        nvps.add(new BasicNameValuePair("bRegex_3", "false"));
        nvps.add(new BasicNameValuePair("bSearchable_3", "true"));

        nvps.add(new BasicNameValuePair("sSearch_4", ""));
        nvps.add(new BasicNameValuePair("bRegex_4", "false"));
        nvps.add(new BasicNameValuePair("bSearchable_4", "true"));

        nvps.add(new BasicNameValuePair("sSearch_5", ""));
        nvps.add(new BasicNameValuePair("bRegex_5", "false"));
        nvps.add(new BasicNameValuePair("bSearchable_5", "false"));

        nvps.add(new BasicNameValuePair("sSearch_6", ""));
        nvps.add(new BasicNameValuePair("bRegex_6", "false"));
        nvps.add(new BasicNameValuePair("bSearchable_6", "false"));

        nvps.add(new BasicNameValuePair("npub", ""));
        nvps.add(new BasicNameValuePair("sSearch_1", ""));
        nvps.add(new BasicNameValuePair("reg", "0"));

        nvps.add(new BasicNameValuePair("q_ver", "arbitr"));
        nvps.add(new BasicNameValuePair("type", "0"));
        nvps.add(new BasicNameValuePair("pdate", "undefined~"));
        nvps.add(new BasicNameValuePair("aucdate", "~"));
        nvps.add(new BasicNameValuePair("stan", "0"));
        nvps.add(new BasicNameValuePair("add_info", ""));

        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        post.addHeader("X-Requested-With", "XMLHttpRequest");
        //post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36");
        //post.addHeader("Origin", "http://torgi.minjust.gov.ua");
        post.addHeader("Referer", "http://torgi.minjust.gov.ua/");
        //post.addHeader("Cookie", "PHPSESSID=qjf9rtiug3mkva1adjiahhemc0; _ga=GA1.3.855529154.1399448065");
    }

    public List<Item> getItems() throws IOException {

        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = rd.readLine();
        EntityUtils.consumeQuietly(response.getEntity());
        System.out.println(line);

        List<Item> result = ResponseToItemsConverter.convert(line);
        Collections.sort(result, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return new Double(o1.price).compareTo(new Double(o2.price));
            }
        });
        int i = 0;
        for(Item item : result) {
            HttpGet get = new HttpGet(item.link);
            response = client.execute(get);
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String place = getPlace(rd);
            EntityUtils.consumeQuietly(response.getEntity());
            item.place = place;
            System.out.println("" + ++i + ") " + item);
        }
        return result;
    }

    private static String getPlace(BufferedReader rd) throws IOException {
        String line = rd.readLine();
        while(line != null) {
            if(line.contains("<dd style=\"margin-left:300px; width:335px; font-weight:700;\">")) {
                return line.substring(line.indexOf(">")+1, line.lastIndexOf("<"));
            }
            line = rd.readLine();
        }
        return null;
    }

}
