package com.example.logincheck;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ContactUsValidation {

    final static String LOGIN_BASE_URL = IPAddressSetting.ipAddress()+"/feedback";

    final static String PARAM_QUERY1 = "name";
    final static String PARAM_QUERY2 = "email";
    final static String PARAM_QUERY3 = "no";
    final static String PARAM_QUERY4 = "message";


    public static URL buildUrl(String username,String email,String no,String message) {
        Uri builtUri = Uri.parse(LOGIN_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY1, username)
                .appendQueryParameter(PARAM_QUERY2, email)
                .appendQueryParameter(PARAM_QUERY3, no)
                .appendQueryParameter(PARAM_QUERY4, message)
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
