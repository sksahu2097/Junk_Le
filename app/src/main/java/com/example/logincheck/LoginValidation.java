package com.example.logincheck;

import java.io.IOException;
import java.net.URL;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class LoginValidation {

    final static String LOGIN_BASE_URL = IPAddressSetting.ipAddress()+"/login";

    final static String PARAM_QUERY1 = "username";
    final static String PARAM_QUERY2 = "password";
    final static String PARAM_QUERY3 = "role";

    public static URL buildUrl(String username,String password,String role) {
        Uri builtUri = Uri.parse(LOGIN_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY1, username)
                .appendQueryParameter(PARAM_QUERY2, password)
                .appendQueryParameter(PARAM_QUERY3, role)
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
