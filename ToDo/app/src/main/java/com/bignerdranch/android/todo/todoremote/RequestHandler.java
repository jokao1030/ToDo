package com.bignerdranch.android.todo.todoremote;

import android.util.Log;
import com.bignerdranch.android.todo.RoomDB.ToDo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Most codes here reference and took from HttpClientDataItemCRUDAccessor from
 * Professor Dr. Martin Schaffoener
 */


public class RequestHandler implements ToDoItemCRUDAccessor{

    protected static String logger = "ToDoRequestHandler";
    private String baseUrl = "http://10.0.2.2:8080/backend-1.0-SNAPSHOT/rest/todos";
    private ObjectMapper mObjectMapper = new ObjectMapper();

    //Send Get
    @Override
    public List<ToDoR> readAllTodos() {

        try {

            HttpGet method = new HttpGet(baseUrl);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(method);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                List<ToDoR> readValue = mObjectMapper.readValue(is, new TypeReference<List<ToDoR>>(){});
                return readValue;
            } else {
                Log.e(logger, "readAllTodos():  Got status code: " + response.getStatusLine().getStatusCode());
            }

        } catch (Exception e) {
            Log.e(logger, "readAllTodos(): got exception: " + e, e);
        }

        return new ArrayList<ToDoR>();

    }

    //Send Post
    @Override
    public ToDoR createToDo(ToDoR task) {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(baseUrl);
            String name = task.getItem();
            String description = task.getDescription();
            String date = task.getDate();
            String time = task.getTime();
            boolean favorite = task.isFavorite();
            boolean completed = task.isCompleted();
            JSONObject json = new JSONObject();
            json.put("item", name);
            json.put("description", description);
            json.put("date", date);
            json.put("time", time);
            json.put("completed", completed);
            json.put("favorite", favorite);
            StringEntity input = new StringEntity(json.toString());
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);
            if(response.getStatusLine().getStatusCode() == 200){
                Log.i(logger, "create todo success!");
            }else if(response.getStatusLine().getStatusCode() > 200){
                Log.i(logger, "status code: " + response.getStatusLine().getStatusCode());
            }
        }catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;

    }
    //Send Delete
    @Override
    public boolean deleteToDo(long todoId) {

        try {

            HttpDelete method = new HttpDelete(baseUrl + "/" + todoId);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(method);

            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                return mObjectMapper.readValue(is, Boolean.class);
            } else {
                Log.e(logger, "deleteToDo():  Got status code: " + response.getStatusLine().getStatusCode());
            }

        } catch (Exception e) {
            Log.e(logger, "deleteToDo(): got exception: " + e, e);
        }

        return false;
    }

    public ToDoR updateToDo(ToDoR todo){

        try {
            HttpPut method = new HttpPut(baseUrl);
            method.setEntity(createHttpEntityFromDataItem(todo));
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(method);

            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                ToDoR ret = mObjectMapper.readValue(is, ToDoR.class);
                return ret;

            } else {
                Log.e(logger, "updateToDo(): Got status code: " + response.getStatusLine().getStatusCode());
            }

        } catch (Exception e) {
            Log.e(logger, "updateToDo(): got exception: " + e, e);
        }

        return null;
    }

    protected HttpEntity createHttpEntityFromDataItem(ToDoR todo)
            throws UnsupportedEncodingException, IOException {

        StringEntity se = new StringEntity(mObjectMapper.writeValueAsString(todo));

        se.setContentType("application/json");
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                "application/json"));

        return se;
    }
}
