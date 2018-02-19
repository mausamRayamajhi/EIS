package com.example.mausam.eis;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by default on 2/15/2018.
 */

public class ReUsableClass {

    public String postMethod(String setRequestMethod,String postParameters,String...urls){

        Log.i("post", urls[0]);

        String result = "";

        URL urlToRequest;

        HttpURLConnection urlConnection = null;



        try {

            urlToRequest = new URL(urls[0]);

            urlConnection = (HttpURLConnection) urlToRequest.openConnection();

            urlConnection.setRequestMethod(setRequestMethod);

            urlConnection.setDoOutput(true);

            DataOutputStream dStream = new DataOutputStream(urlConnection.getOutputStream());

            dStream.writeBytes(postParameters);

            dStream.flush();

            dStream.close();

            if ( urlConnection.getResponseCode() == 200) {

                InputStream inputStream = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(inputStream);

                int data = reader.read();

                while (data != -1) {

                    char currentCharacter = (char) data;

                    result += currentCharacter;

                    data = reader.read();
                }

                return result;

            }

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return  result;
    }


    public String getMethod(String setRequestMethod,String... urls) {

        String result = "";

        URL url;

        HttpURLConnection connection = null;

        try {

            url = new URL(urls[0]);

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(setRequestMethod);

            int responseCode = connection.getResponseCode();

            InputStream inputStream = connection.getInputStream();

            InputStreamReader reader = new InputStreamReader(inputStream);

            int data = reader.read();

            while (data != -1) {

                char currentCharacter = (char) data;

                result += currentCharacter;

                data = reader.read();

            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
