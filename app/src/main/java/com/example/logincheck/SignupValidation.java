package com.example.logincheck;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class SignupValidation {

    final static String LOGIN_BASE_URL = IPAddressSetting.ipAddress()+"/signup";

    final static String PARAM_QUERY1 = "user";
    final static String PARAM_QUERY2 = "password";
    final static String PARAM_QUERY3 = "role";
    final static String PARAM_QUERY4 = "fname";
    final static String PARAM_QUERY5 = "lname";
    final static String PARAM_QUERY6 = "email";
    final static String PARAM_QUERY7 = "no";

    public static URL buildUrl(String user, String password, String role, String fname, String lname, String email,String mno) {
        Uri builtUri = Uri.parse(LOGIN_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY1, user)
                .appendQueryParameter(PARAM_QUERY2, password)
                .appendQueryParameter(PARAM_QUERY3, role)
                .appendQueryParameter(PARAM_QUERY4, fname)
                .appendQueryParameter(PARAM_QUERY5, lname)
                .appendQueryParameter(PARAM_QUERY6, email)
                .appendQueryParameter(PARAM_QUERY7, mno)
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
