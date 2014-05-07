package com.etam;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;

/**
 * User: vryzhuk
 * Date: 5/7/14
 * Time: 12:49 PM
 */
public class ResponseToItemsConverter {
    public static List<Item> convert(String response) {
        List<Item> result = new ArrayList<Item>();
        JSONObject obj = (JSONObject) JSONValue.parse(response);
        JSONArray array = (JSONArray)obj.get("aaData");

        for(int i = 0; i < array.size(); i++) {
            JSONArray element = (JSONArray)array.get(i);
            String url = getLink(element.get(0));
            String lot = getLot(element.get(0));
            String name = getName(element.get(1));
            String price = getPrice(element.get(2));
            String endOfRegistration = element.get(3).toString();
            String beginDate = element.get(4).toString();
            result.add(new Item(url, lot, name, price, endOfRegistration, beginDate));
        }
        return result;
    }

    private static String getPrice(Object o) {
        String s = o.toString();
        s = s.replaceAll("&nbsp;", "");
        s = s.replaceAll(",", ".");
        return s;
    }

    private static String getName(Object o) {
        String s = o.toString();
        return s.substring(s.indexOf(">") + 1, s.lastIndexOf("<"));
    }

    private static String getLink(Object o) {
        String s = o.toString();
        return "http://torgi.minjust.gov.ua/" + s.substring(s.indexOf("/")+1, s.indexOf(">"));
    }

    private static String getLot(Object o) {
        String s = o.toString();
        return s.substring(s.indexOf(">") + 1, s.lastIndexOf("<"));
    }
}
