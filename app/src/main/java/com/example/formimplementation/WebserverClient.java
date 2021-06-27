package com.example.formimplementation;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class WebserverClient {



        private static final String BASE_URL = "http://192.168.101.12:8000/";

        private static AsyncHttpClient client = new AsyncHttpClient();
       //client.setEnableRedirects(true, true, true);

        public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.setEnableRedirects(true, true, true);
            client.get(getAbsoluteUrl(url), params, responseHandler);

        }

        public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.setEnableRedirects(true, true, true);
            client.post(getAbsoluteUrl(url), params, responseHandler);


        }

        private static String getAbsoluteUrl(String relativeUrl) {
            return BASE_URL + relativeUrl;
        }
    }

