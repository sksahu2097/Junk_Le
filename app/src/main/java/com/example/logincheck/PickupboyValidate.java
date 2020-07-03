package com.example.logincheck;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class PickupboyValidate {

    final static String LOGIN_BASE_URL = IPAddressSetting.ipAddress()+"/getOrder";

    final static String PARAM_QUERY1 = "category";
    final static String PARAM_QUERY2 = "dop";
    public static URL buildUrl(String category, String dop) {
        Uri builtUri = Uri.parse(LOGIN_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY1, category)
                .appendQueryParameter(PARAM_QUERY2, dop)
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
