package com.bignerdranch.android.todo.login;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Map;


public class RequestHandler {

    protected static String logger = "RequestHandler";

    /**
     * Send request to server , get status code and response
     * return status code and response as key value pair
     **/
    public int createItem(User user) {
               int code = 0;
               /*int[] subpair = new int[4];
               subpair[0] = 0;
               subpair[1] = 0;
               subpair[2] = 0;
               subpair[3] = 0;
               KeyValue<Integer, int[]> pair = new KeyValue<>(0 , subpair);*/
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://10.0.2.2:8080/backend-1.0-SNAPSHOT/rest/users");
            String email = user.getEmail();
            String password = user.getPassword();
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);
            StringEntity input = new StringEntity(json.toString());
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() == 200) {
               //pair.setKey(200);
                code = 200;
            } else if (response.getStatusLine().getStatusCode() == 401) {
                //pair.setKey(401);
                code = 401;
            } else if (response.getStatusLine().getStatusCode() == 400) {
                //pair.setKey(400);
                code = 400;
                BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
                String output;
                StringBuffer check = new StringBuffer();

                /*String str1 = "Empty Email is not allowed !";
                String str2 = "Invalid Email address !";
                String str3 = "Empty Password is not allowed !";
                String str4 = "Password should be 6 numbers !";*/
                while ((output = br.readLine()) != null) {
                    //Log.i(logger, output);
                    check.append(output);
                }
                Log.i(logger, check.toString());
                //Combination of response
                /*if (check.toString().contains(str1) && !check.toString().contains(str3)) {
                    subpair[0] = 1;
                    subpair[1] = 0;
                    subpair[2] = 0;
                    subpair[3] = 0;
                    pair.setValue(subpair);
                    Log.i(logger, Arrays.toString(subpair).replaceAll("\\[|\\]|,|\\s", ""));
                }else if(check.toString().contains(str1) && !check.toString().contains(str3) && check.toString().contains(str4)){
                        subpair[0] = 1;
                        subpair[1] = 0;
                        subpair[2] = 0;
                        subpair[3] = 1;
                        pair.setValue(subpair);
                        Log.i(logger, Arrays.toString(subpair).replaceAll("\\[|\\]|,|\\s", ""));
                }else if(check.toString().contains(str1) && check.toString().contains(str3)) {
                    subpair[0] = 1;
                    subpair[1] = 0;
                    subpair[2] = 1;
                    subpair[3] = 0;
                    pair.setValue(subpair);
                    Log.i(logger, Arrays.toString(subpair).replaceAll("\\[|\\]|,|\\s", ""));
                } else if (check.toString().contains(str2) && !check.toString().contains(str1)) {
                    subpair[0] = 0;
                    subpair[1] = 1;
                    subpair[2] = 0;
                    subpair[3] = 0;
                    pair.setValue(subpair);
                    Log.i(logger, Arrays.toString(subpair).replaceAll("\\[|\\]|,|\\s", ""));
                    if(check.toString().contains(str3)) {
                        subpair[0] = 0;
                        subpair[1] = 1;
                        subpair[2] = 1;
                        subpair[3] = 0;
                        pair.setValue(subpair);
                        Log.i(logger, Arrays.toString(subpair).replaceAll("\\[|\\]|,|\\s", ""));
                    }else if (check.toString().contains(str4)) {
                        subpair[0] = 0;
                        subpair[1] = 1;
                        subpair[2] = 0;
                        subpair[3] = 1;
                        pair.setValue(subpair);
                        Log.i(logger, Arrays.toString(subpair).replaceAll("\\[|\\]|,|\\s", ""));}
                }else if (check.toString().contains(str3) && !check.toString().contains(str1)) {
                    subpair[0] = 0;
                    subpair[1] = 0;
                    subpair[2] = 1;
                    subpair[3] = 0;
                    pair.setValue(subpair);
                    Log.i(logger, Arrays.toString(subpair).replaceAll("\\[|\\]|,|\\s", ""));
                }else if(!check.toString().contains(str1) && !check.toString().contains(str3) && check.toString().contains(str4)){
                        subpair[0] = 0;
                        subpair[1] = 0;
                        subpair[2] = 0;
                        subpair[3] = 1;
                        pair.setValue(subpair);
                        Log.i(logger, Arrays.toString(subpair).replaceAll("\\[|\\]|,|\\s", ""));
                } else {
                    subpair[0] = 0;
                    subpair[1] = 0;
                    subpair[2] = 0;
                    subpair[3] = 0;
                    pair.setValue(subpair);
                    Log.i(logger, Arrays.toString(subpair).replaceAll("\\[|\\]|,|\\s", ""));
                }*/

            } else {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }


        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * Custom Pair Class
    **/
    public static class KeyValue<K, V> implements Map.Entry<K, V>
    {
        private K key;
        private V value;

        public KeyValue(K key, V value)
        {
            this.key = key;
            this.value = value;
        }

        public K getKey()
        {
            return this.key;
        }

        public V getValue()
        {
            return this.value;
        }

        public K setKey(K key)
        {
            return this.key = key;
        }

        public V setValue(V value)
        {
            return this.value = value;
        }
    }
}
