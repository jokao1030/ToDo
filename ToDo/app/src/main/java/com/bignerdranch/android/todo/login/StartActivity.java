package com.bignerdranch.android.todo.login;

/**
 * Splash Screen reference from the youtube tutorial:
 * https://www.youtube.com/watch?v=JLIFqqnSNmg&t=68s&ab_channel=CodingWithTea
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bignerdranch.android.todo.R;
import com.bignerdranch.android.todo.ToDoListActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class StartActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;
    //Variables
    Animation topAnim, bottomAnim;
    ImageView mImage;
    TextView mLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        mImage = findViewById(R.id.imageView);
        mLogo = findViewById(R.id.textView);

        mImage.setAnimation(topAnim);
        mLogo.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                new CheckServerAsnyc().execute();
            }
        }, SPLASH_SCREEN);
    }

    /**
     * Async Task to check whether connection to server is working
     **/
    private class CheckServerAsnyc extends AsyncTask<String, Void,Boolean> {


        @Override
        protected Boolean doInBackground(String... args) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://10.0.2.2:8080/backend-1.0-SNAPSHOT/");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute (Boolean check){
            if (check == true) {
                startActivity(new Intent(StartActivity.this, Login.class));
            } else {
                //If we cannot connect to server, then go to todolist directly
                startActivity(new Intent(StartActivity.this, ToDoListActivity.class));
            }
        }



    }
}