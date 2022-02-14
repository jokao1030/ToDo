package com.bignerdranch.android.todo.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bignerdranch.android.todo.R;
import com.bignerdranch.android.todo.ToDoListActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends AppCompatActivity {

    private Button mLoginButton;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private ProgressBar mProgressBar;
    protected static String logger = "Login";
    private RequestHandler mHandler = new RequestHandler();
    private User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        mEmail = (TextInputLayout)findViewById(R.id.email);
        mPassword= (TextInputLayout)findViewById(R.id.password);
        mLoginButton = (Button)findViewById(R.id.login_button);
        mProgressBar = findViewById(R.id.progressbar);
        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    if(!validateEmail() | !validatePassword()){
                        return;
                    }else{
                        mUser = new User();
                        mUser.setEmail(mEmail.getEditText().getText().toString());
                        mUser.setPassword(mPassword.getEditText().getText().toString());

                        new LoginAsync().execute();
                    }
            }
        });


    }
    

    /**
     * Client Side Validation of Input
     **/
    private Boolean validateEmail(){
        String val = mEmail.getEditText().getText().toString();
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+╲╲.+[a-z]+";

        if(val.isEmpty()){
            mEmail.setError("Empty Email is not allowed !");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(val).matches()){
            mEmail.setError("Invalid Email address !");
            return false;
        }else{
            mEmail.setError(null);
            mEmail.setErrorEnabled(true);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = mPassword.getEditText().getText().toString();
        String passwordPattern = "[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]";
        if(val.isEmpty()){
            mPassword.setError("Empty Password is not allowed !");
            return false;
        }else if(!val.matches(passwordPattern)){
            mPassword.setError("Password should be 6 numbers !");
            return false;
        }else{
            mPassword.setError(null);
            mPassword.setErrorEnabled(true);
            return true;
        }
    }




    /**
     * Async Task to login
     * The server will test if
     * the email is : kao@gmail.com
     * and the password is : 123456
     * if yes: 200
     * if not: 401
     **/
    private class LoginAsync extends AsyncTask<User,Void, Integer> {
        //int[] subpair = new int[4];
        //RequestHandler.KeyValue<Integer, int[]> code = new RequestHandler.KeyValue<>(0 , subpair);
        int code = 0;
        @Override
        protected Integer doInBackground(User... user) {


            try{
                code = mHandler.createItem(mUser);
            } catch (Exception e) {
                Log.e(logger, "sendUser(): got exception: " + e);
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer code) {
            ValueAnimator animator = ValueAnimator.ofInt(0, mProgressBar.getMax());
            animator.setDuration(3000);
            if (code == 200) {
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation){
                        mProgressBar.setProgress((Integer)animation.getAnimatedValue());
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        startActivity(new Intent(Login.this, ToDoListActivity.class));
                    }
                });
                animator.start();
            }else if(code == 401){
                mPassword.setError(null);
                mEmail.setError(null);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation){
                        mProgressBar.setProgress((Integer)animation.getAnimatedValue());
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Toast.makeText(getApplicationContext(), "You are not registered!", Toast.LENGTH_LONG).show();
                    }
                });
                animator.start();

            }


              //When the input validation from server also required...
              /*else if(code.getKey() == 400){
                    String str1 = "Empty Email is not allowed !";
                    String str2 = "Invalid Email address !";
                    String str3 = "Empty Password is not allowed !";
                    String str4 = "Password should be 6 numbers !";
                    String check = Arrays.toString(code.getValue()).replaceAll("\\[|\\]|,|\\s", "");
                    Log.i(logger,check);
                    if(check.equals("1000")) {
                        mEmail.setError(str1);
                        mPassword.setError(null);
                    }else if(check.equals("0100")) {
                        mEmail.setError(str2);
                        mPassword.setError(null);
                    }else if(check.equals("1010")) {
                        mEmail.setError(str1);
                        mPassword.setError(str3);
                    }else if(check.equals("1001")) {
                        mEmail.setError(str1);
                        mPassword.setError(str4);
                    }else if(check.equals("0010")) {
                        mEmail.setError(null);
                        mPassword.setError(str3);
                    }else if(check.equals("0110")) {
                        mEmail.setError(str2);
                        mPassword.setError(str3);
                    }else if(check.equals("0101")) {
                        mEmail.setError(str2);
                        mPassword.setError(str4);
                    }else if(check.equals("0001")) {
                        mEmail.setError(null);
                        mPassword.setError(str4);
                    }else if(check.equals("0000")){
                        mEmail.setError(null);
                        mPassword.setError(null);
                    }

            }*/
        }
    }

}
