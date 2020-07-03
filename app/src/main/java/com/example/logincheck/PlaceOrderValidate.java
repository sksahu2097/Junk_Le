package com.example.logincheck;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class PlaceOrderValidate {

    final static String LOGIN_BASE_URL = IPAddressSetting.ipAddress()+"/placeOrder";

    final static String PARAM_QUERY1 = "userid";
    final static String PARAM_QUERY2 = "category";
    final static String PARAM_QUERY3 = "quantity";
    final static String PARAM_QUERY4 = "price";
    final static String PARAM_QUERY5 = "pickup_address";
    final static String PARAM_QUERY6 = "dop";

    public static URL buildUrl(String username, String category, String quantity , String price,String pickup_address,String dop) {
        Uri builtUri = Uri.parse(LOGIN_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY1, username)
                .appendQueryParameter(PARAM_QUERY2, category)
                .appendQueryParameter(PARAM_QUERY3, quantity)
                .appendQueryParameter(PARAM_QUERY4, price)
                .appendQueryParameter(PARAM_QUERY5, pickup_address)
                .appendQueryParameter(PARAM_QUERY6, dop)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}
