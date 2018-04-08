package com.example.mausam.eis;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

;


public class MainActivity extends AppCompatActivity {

    static SharedPreferences saveUserCrediantial;

    ImageView circleImageView;

    Button loginBtn;

    EditText usernameTextField;

    EditText passwordTextField;

    String userName = "";

    String password = "";

    String parentsModeStatus ="";

    CheckBox rememberMeCheckBox;

    boolean remenberMeIsChecked;

    CardView BothParentStudentCardView;

    CardView loginCardView;

    CardView cardView;

    TextView forgetPassword;

    ImageView parentsModeTextView;

    GetUserData user;


    static String ip;

    String setRequestMethod = "";

    String postParameters = "";

    int s_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        saveUserCrediantial = getApplicationContext().getSharedPreferences("com.example.mausam.eis", Context.MODE_PRIVATE);

        circleImageView = (ImageView) findViewById(R.id.circleImageView);

        loginCardView =(CardView)findViewById(R.id.loginCardView);

        cardView=(CardView)findViewById(R.id.cardView);

        BothParentStudentCardView=(CardView)findViewById(R.id.BothParentStudentCardView);

        forgetPassword=(TextView)findViewById(R.id.forgetPassword);

        parentsModeTextView=(ImageView)findViewById(R.id.parentsModeTextView);

        loginBtn = (Button) findViewById(R.id.loginBtn);

        usernameTextField = (EditText) findViewById(R.id.usernameTextField);

        passwordTextField = (EditText) findViewById(R.id.passwordTextField);

        rememberMeCheckBox = (CheckBox) findViewById(R.id.rememberMeCheckBox);

        user = new GetUserData();


        // for local machin Use 10.0.2.2 for default AVD and 10.0.3.2 for Genymotion

        //ip = "192.168.0.20";
        ip ="192.168.15.119";

        //ip ="192.168.1.4";//shrawan ip

        // form genymotion

        //ip = "10.0.3.2";

        //for android emulator

        //ip = "10.0.2.2";


        if (!isConnected(MainActivity.this)) {

            buildDialog(MainActivity.this, "No Internet Connection", "You need to have Mobile Data or wifi to access this. Press ok to Exit").show();

        } else {

            userName = saveUserCrediantial.getString("username", "");

            password = saveUserCrediantial.getString("password", "");


            parentsModeStatus  = saveUserCrediantial.getString("parentsModeStatus", "");

            // this if when parentsmode btn is long pressed to request again
            parentsModeTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    parentsModeStatus="false";

                    parentsMode(view);
                    return true;
                }
            });


// this if is for if the app has saved both student and parents mode status
         if ((!userName.equals("") && !password.equals("") && !parentsModeStatus.equals(""))){

             loginCardView.setVisibility(View.INVISIBLE);

             cardView.setVisibility(View.INVISIBLE);

             forgetPassword.setVisibility(View.INVISIBLE);

             rememberMeCheckBox.setVisibility(View.INVISIBLE);

             parentsModeTextView.setVisibility(View.INVISIBLE);

             BothParentStudentCardView.setVisibility(View.VISIBLE);

         }else{

             if (!userName.equals("") && !password.equals("")) {

                 usernameTextField.setText(userName);

                 passwordTextField.setText(password);

                 user.execute("http://" + ip + ":8080/exam-project-8/ApiLoginOut");

                 Toast.makeText(MainActivity.this, "Username and password already exits As " + saveUserCrediantial.getString("username", ""), Toast.LENGTH_LONG).show();

             } else if (parentsModeStatus.equalsIgnoreCase("active")) {

                 //Toast.makeText(this, "Parents Mode is Active", Toast.LENGTH_LONG).show();

                 s_id = Integer.parseInt(saveUserCrediantial.getString("studentId", ""));

                 String contact = saveUserCrediantial.getString("parentsNumber", "");

                 if (!(s_id < 0 && contact == null)) {

                     IsParentsModeValid modeValid = new IsParentsModeValid();

                     modeValid.execute("http://" + MainActivity.ip + ":8080/exam-project-8/ApiStudentParent/GetStudentParentBy/" + s_id + "/" + contact);
                 }


             }

         }

        }

    }

    public void newLogin(View view){

        loginCardView.setVisibility(View.VISIBLE);

        cardView.setVisibility(View.VISIBLE);

        forgetPassword.setVisibility(View.VISIBLE);

        rememberMeCheckBox.setVisibility(View.VISIBLE);

        parentsModeTextView.setVisibility(View.VISIBLE);

        BothParentStudentCardView.setVisibility(View.INVISIBLE);
    }

    public  void activateStudentMode(View view){

        user.execute("http://" + ip + ":8080/exam-project-8/ApiLoginOut");

    }

    public  void activatePatentsMode(View view){

        s_id = Integer.parseInt(saveUserCrediantial.getString("studentId", ""));

        String contact = saveUserCrediantial.getString("parentsNumber", "");

            IsParentsModeValid modeValid = new IsParentsModeValid();

            modeValid.execute("http://" + MainActivity.ip + ":8080/exam-project-8/ApiStudentParent/GetStudentParentBy/" + s_id + "/" + contact);


    }

    public void parentsMode(View view) {

        if (parentsModeStatus.equalsIgnoreCase("active")) {

            //Toast.makeText(this, "Parents Mode is Active", Toast.LENGTH_LONG).show();

            s_id = Integer.parseInt(saveUserCrediantial.getString("studentId", ""));

            String contact = saveUserCrediantial.getString("parentsNumber", "");

            if (!(s_id < 0 && contact == null)) {

                IsParentsModeValid modeValid = new IsParentsModeValid();

                modeValid.execute("http://" + MainActivity.ip + ":8080/exam-project-8/ApiStudentParent/GetStudentParentBy/" + s_id + "/" + contact);
            }
        }else {

            Intent parentsMode = new Intent(getApplicationContext(), ParentsModeActivity.class);

            startActivity(parentsMode);
        }
    }

    public void userLogin(View view) {

        if (isConnected(MainActivity.this) && !usernameTextField.getText().toString().equalsIgnoreCase("") && !passwordTextField.getText().toString().equalsIgnoreCase("")) {

            userName = usernameTextField.getText().toString().trim();

            password = passwordTextField.getText().toString().trim();

            try {

                // this has to be done to create new background connection and no need of try catch too...

                user = new GetUserData();

                user.execute("http://" + ip + ":8080/exam-project-8/ApiLoginOut");

            } catch (Exception e) {

                e.printStackTrace();
            }

        } else {

            if (!isConnected(MainActivity.this)) {

                buildDialog(MainActivity.this, "No Internet Connection", "You need to have Mobile Data or wifi to access this. Press ok to Exit").show();

            } else {

                Toast.makeText(MainActivity.this, "Error in crediantial !", Toast.LENGTH_SHORT).show();
            }

        }
        remenberMeIsChecked = rememberMeCheckBox.isChecked();

    }

    // loginbtn clicked

    public void forgetPassword(View view) {

        Intent forgetPassword = new Intent(getApplicationContext(), ForgetPasswordActivity.class);

        startActivity(forgetPassword);
    }

    // forget password


    // create cirle

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {

            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (mobile != null && mobile.isConnectedOrConnecting() || wifi != null && wifi.isConnectedOrConnecting()) {

                return true;

            } else {

                return false;

            }

        } else {

            return false;
        }
    }

    public AlertDialog.Builder buildDialog(Context c, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        builder.setTitle(title);

        builder.setMessage(message);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }

        });

        return builder;
    }

    class IsParentsModeValid extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = new ReUsableClass().getMethod("GET", urls);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            // 1-valid request 0- invalid

            Log.i("paremtsmOdedvalid",result);

            int validParentsMode = Integer.parseInt(result);

            if (validParentsMode ==1){

                Toast.makeText(MainActivity.this, "Your request has been accepted.", Toast.LENGTH_LONG).show();


                Intent parentsDashboard = new Intent(getApplicationContext(), ParentsModeDashboardActivity.class);

                parentsDashboard.putExtra("studentId", s_id );

                startActivity(parentsDashboard);
            }


        }
    }

    public class GetUserData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            setRequestMethod = "POST";

            postParameters = "{'category':'student','adminUserName':'" + userName + "','InputPassword1':'" + password + "'}";

            String result = postMethod(setRequestMethod, postParameters, urls);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.i("studentMode",result);
            if (result != null && Integer.parseInt(result) > 0) {

                Toast.makeText(MainActivity.this, "Login sucess! & Id = " + result, Toast.LENGTH_SHORT).show();

                if (remenberMeIsChecked) {

                    saveUserCrediantial.edit().putString("username", userName).apply();

                    saveUserCrediantial.edit().putString("password", password).apply();
                }

                //put here method after login success

                Intent studentDashboard = new Intent(getApplicationContext(),StudentDashboardActivity.class);
                studentDashboard.putExtra("s_id",result );
                startActivity(studentDashboard);

            } else {

                Toast.makeText(MainActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();

            }

            Log.i("login", "finished");

        }
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
