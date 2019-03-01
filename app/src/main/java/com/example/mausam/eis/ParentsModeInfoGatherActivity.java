package com.example.mausam.eis;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class ParentsModeInfoGatherActivity extends AppCompatActivity {

    private EditText parentNameTextField;

    private EditText relationTextField;

    private EditText studentIdTextField;

    private EditText mobileNumberTextField;

    private Button requestBtn;

    private SaveParentModeInfo info;

    int respCode;

    int studentId;

    String contact;

    String fullName;

    String relation;



    public class SaveParentModeInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {


            String setRequestMethod = "POST";

            String postParameters = "{\"student_id\":{\"s_id\":" + studentId + "},\"fullname\":\"" + fullName + "\",\"relation\":\"" + relation + "\",\"primary_contact\":\"" + contact + "\"}";

            //String postParameters="{}";
            String result = postMethod(setRequestMethod, postParameters, urls);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            if (respCode == 200) {

                 //Snackbar.make(findViewById(R.id.mobileNumberTextField),"Your request for parents mode has been send. You will get call once it is confirmed",Snackbar.LENGTH_LONG ).setAction("Action",null).show();

                new MainActivity().saveUserCrediantial.edit().putString("parentsModeStatus", "active").apply();

                new MainActivity().saveUserCrediantial.edit().putString("studentId", Integer.toString(studentId)).apply();

                new MainActivity().saveUserCrediantial.edit().putString("parentsNumber", contact).apply();

                //AlertDialog show = new MainActivity().buildDialog(ParentsModeInfoGatherActivity.this, "Request Sucessfull", "Your request for parents mode has been send. You will get call once it is confirmed.").show();

                Toast.makeText(ParentsModeInfoGatherActivity.this, "Your request for parents mode has been send. You will get call once it is confirmed.", Toast.LENGTH_LONG).show();

                Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);

                startActivity(mainActivity);



            } else {

                new MainActivity().buildDialog(ParentsModeInfoGatherActivity.this, "Request Failed", "Your parents mode request is not sucessfull. Please check you information again.").show();


            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_mode_info_gather);

        parentNameTextField = (EditText) findViewById(R.id.parentNameTextField);

        relationTextField = (EditText) findViewById(R.id.relationTextField);

        studentIdTextField = (EditText) findViewById(R.id.studentIdTextField);

        mobileNumberTextField = (EditText) findViewById(R.id.mobileNumberTextField);

        requestBtn = (Button) findViewById(R.id.requestBtn);


    }


    public void requestParentsMode(View view) {


        studentId = Integer.parseInt(studentIdTextField.getText().toString().trim());

        contact = mobileNumberTextField.getText().toString().trim();

        fullName = parentNameTextField.getText().toString().trim();

        relation = relationTextField.getText().toString().trim();


        info = new SaveParentModeInfo();

        info.execute("http://" + MainActivity.getServerIp() + ":8080/exam-project-8/ApiStudentParent/SaveStudentParent");


    }


    public String postMethod(String setRequestMethod, String postParameters, String... urls) {

        Log.i("post", urls[0] + "\n" + postParameters);

        String result = "";

        URL urlToRequest;

        HttpURLConnection urlConnection = null;


        try {

            urlToRequest = new URL(urls[0]);

            urlConnection = (HttpURLConnection) urlToRequest.openConnection();

            urlConnection.setRequestMethod(setRequestMethod);


            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");

            urlConnection.setDoOutput(true);

            DataOutputStream dStream = new DataOutputStream(urlConnection.getOutputStream());

            dStream.writeBytes(postParameters);

            dStream.flush();

            dStream.close();

            respCode = urlConnection.getResponseCode();

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
