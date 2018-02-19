package com.example.mausam.eis;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText usernameTextField;

    Button forgetPasswordBtn;

    Button checkEmailBtn;

    String email;

    CardView cardViewButton;

    CardView loading;

    CardView cardViewMyStoreLayout;

    CardView verificationcardViewLayout;

    CardView cardViewVerifyCodeLayout;

    String setRequestMethod = "";

    EditText verificationCodeTextField;

    String verificationCode = "";

    CardView cardViewRestorePassword;

    CardView cardViewRestorePasswordLayout;

    EditText newPasswordTextField;

    EditText newPasswordConfirmTextField;

    String newPassword = "";

    String confirmedPassword = "";


    // Run Multiple Async Task

    public void runMultipleAsynctask(int taskToRun) {

        if (taskToRun == 1) {

            setRequestMethod = "GET";

            ForgetPasswordRequest request = new ForgetPasswordRequest();

            request.execute("http://" + MainActivity.ip + ":8080/exam-project-8/ApiForgetPassword/" + email);

        } else if (taskToRun == 2) {

            setRequestMethod = "GET";

            String[] codeAndId = verificationCode.split("/");

            VerifyCode verifyCode = new VerifyCode();

            verifyCode.execute("http://" + MainActivity.ip + ":8080/exam-project-8/ApiResetPassword/" + codeAndId[0] + "/Student/" + codeAndId[1]);

        } else if (taskToRun == 3) {

            setRequestMethod = "POST";

            ResetPassword resetPassword = new ResetPassword();


            resetPassword.execute("http://" + MainActivity.ip + ":8080/exam-project-8/ApiResetPasswordNext");


        }


    }

    // this class resets the password

    class ResetPassword extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {


            String result = "";
            String[] codeAndId = verificationCode.split("/");

            String postParameters = "{'vCode':'" + codeAndId[0] + "','tablename':'Student','ID':'" + codeAndId[1] + "','password':'" + confirmedPassword + "'}";

            result = postMethod(setRequestMethod, postParameters, urls);

            //while trying to access mainactivity postMethod error says"calling dead method ki thread re ...so post method is copied below"
            //result=new MainActivity().postMethod(setRequestMethod,postParameters,urls);

            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //200 success
            //-400 failure



            if (result.equalsIgnoreCase("200")) {


                Handler mHandler = new Handler(getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Password changed.", Toast.LENGTH_LONG).show();
                    }
                });

                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);

            } else {
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
            }
        }
    }

// this class check if verification code is valid or not

    class VerifyCode extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = getMethod(urls);

            return result;


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equalsIgnoreCase("true")) {

                verificationcardViewLayout.setVisibility(View.INVISIBLE);

                cardViewVerifyCodeLayout.setVisibility(View.INVISIBLE);


                cardViewRestorePassword.setVisibility(View.VISIBLE);

                cardViewRestorePasswordLayout.setVisibility(View.VISIBLE);


            } else {

                new MainActivity().buildDialog(ForgetPasswordActivity.this, "Invalid Code", "Verification code did not match . Check your email again or request again.").show();

            }
        }
    }
    // this class is used to send forgetPassword request

    class ForgetPasswordRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = getMethod(urls);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            if (!result.equalsIgnoreCase("failed")) {

                new MainActivity().buildDialog(ForgetPasswordActivity.this, "Request successful", "Verification code has been send to you email. Check your email. Press ok to verify code.").show();

                cardViewButton.setVisibility(View.INVISIBLE);

                loading.setVisibility(View.INVISIBLE);

                cardViewMyStoreLayout.setVisibility(View.INVISIBLE);

                verificationcardViewLayout.setVisibility(View.VISIBLE);

                cardViewVerifyCodeLayout.setVisibility(View.VISIBLE);

            } else {

                new MainActivity().buildDialog(ForgetPasswordActivity.this, "Request failed ", "Something is wrong with your email of internet connection.").show();

                cardViewButton.setVisibility(View.VISIBLE);

                loading.setVisibility(View.INVISIBLE);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget_password);

        usernameTextField = (EditText) findViewById(R.id.usernameTextField);

        checkEmailBtn = (Button) findViewById(R.id.checkEmailBtn);

        forgetPasswordBtn = (Button) findViewById(R.id.forgetPasswordBtn);

        cardViewButton = (CardView) findViewById(R.id.cardViewButton);

        loading = (CardView) findViewById(R.id.cardViewLoading);

        cardViewMyStoreLayout = (CardView) findViewById(R.id.cardViewMyStore);

        verificationcardViewLayout = (CardView) findViewById(R.id.verificartionCardView);

        cardViewVerifyCodeLayout = (CardView) findViewById(R.id.cardViewVerifyCode);

        verificationCodeTextField = (EditText) findViewById(R.id.verificationCodeTextField);

        cardViewRestorePassword = (CardView) findViewById(R.id.cardViewRestorePassword);

        cardViewRestorePasswordLayout = (CardView) findViewById(R.id.cardViewRestorePasswordLayout);

        newPasswordTextField = (EditText) findViewById(R.id.newPasswordTextField);

        newPasswordConfirmTextField = (EditText) findViewById(R.id.newPasswordConfirmTextField);


    }

    /// this is called when rest is clicked

    public void resetPasswordNow(View view) {

        newPassword = newPasswordTextField.getText().toString().trim();

        confirmedPassword = newPasswordConfirmTextField.getText().toString().trim();

        if (newPassword.equals(confirmedPassword)) {

            runMultipleAsynctask(3);

        } else {

            new MainActivity().buildDialog(ForgetPasswordActivity.this, "Not matched", "Your password did not matched. Enter again.").show();
        }
    }

    // method for sending request

    public void forgetPassword(View view) {

        email = usernameTextField.getText().toString().trim();

        //  ForgetPasswordRequest request = new ForgetPasswordRequest();

        //request.execute("http://" + MainActivity.ip + ":8080/exam-project-8/ApiForgetPassword/" + email);


        runMultipleAsynctask(1);

        cardViewButton.setVisibility(View.INVISIBLE);

        loading.setVisibility(View.VISIBLE);

    }

    // method for checking email

    public void checkEmail(View view) {

        Log.i("checkEmail", "checkEmail");

    }

    // method to verify code of email

    public void verifyCode(View view) {

        verificationCode = verificationCodeTextField.getText().toString().trim();

        runMultipleAsynctask(2);

    }

    public String getMethod(String... urls) {

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

    public String postMethod(String setRequestMethod, String postParameters, String... urls) {

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

            if (urlConnection.getResponseCode() == 200) {

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

        return result;
    }

}
