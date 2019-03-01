package com.example.mausam.eis;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Setting extends AppCompatActivity {

    private TextView student_name;
    private TextView student_userName;
    private TextView student_password;

    private EditText usernameStudentTextField;
    private EditText usernameStudentPasswordTextField;

    private Button changeStudentPasswordBtn;

    private int s_id;
    private  Student stdn;
    String jsonResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        s_id = Integer.parseInt(getIntent().getExtras().getString("studentId"));

        student_name =(TextView)findViewById(R.id.student_name);
        student_userName =(TextView)findViewById(R.id.student_userName);
        student_password =(TextView)findViewById(R.id.student_password);

        usernameStudentTextField=(EditText)findViewById(R.id.usernameStudentTextField);
        usernameStudentPasswordTextField=(EditText)findViewById(R.id.usernameStudentPasswordTextField);


        changeStudentPasswordBtn=(Button)findViewById(R.id.changeStudentPasswordBtn);

       GetStudentInfo getStudentInfo = new GetStudentInfo();
        getStudentInfo.execute("http://" + MainActivity.getServerIp() + ":8080/exam-project-8/ApiStudentsProgram/GetStudentsProgramByStudentId/"+s_id);

    }

    class GetStudentInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = new ReUsableClass().getMethod("GET", urls);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            Log.i("stDetails",result);

            try {
                JSONArray j  = new JSONArray(result);
                for (int i = 0; i < j.length(); i++) {
                    JSONObject rec = j.getJSONObject(i);

                    JSONObject student = rec.getJSONObject("student");
                    student_name.setText(student.getString("first_name")+" "+student.getString("middle_name")+" "+student.getString("last_name" ));
                    student_userName.setText("Username : "+student.getString("username"));
                    student_password.setText("Password : "+student.getString("password"));
                    usernameStudentTextField.setText(student.getString("username"));
                    usernameStudentPasswordTextField.setText(student.getString("password"));

                    stdn= new Student();
                    stdn.setFirst_name(student.getString("first_name"));
                    stdn.setMiddle_name(student.getString("middle_name"));
                    stdn.setLast_name(student.getString("last_name" ));
                    stdn.setS_id(s_id);
                    stdn.setAddress(student.getString("address" ));
                    stdn.setCurrent_semester(student.getInt("current_semester"));
                    stdn.setDate_of_birth(student.getString("date_of_birth"));
                    stdn.setEmail(student.getString("email"));
                    stdn.setGender(student.getInt("gender"));
                   // stdn.setImage(student.getString(""));
                    stdn.setPassword(student.getString("password"));
                    stdn.setPhone(student.getString("phone"));
                    stdn.setStatus(student.getInt("status"));
               if (student.getString("verificationCode")!=null){
                   stdn.setVerificationCode(student.getString("verificationCode"));
               }

                    Log.i("studentModel",stdn.toString());

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class UpdateUsernamePassword extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = new ReUsableClass().putMethod("PUT",jsonResult, urls);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            Log.i("afterUpdate",result);
            Snackbar snackbar= Snackbar.make(student_name,"Username and password changed.",Snackbar.LENGTH_LONG);
            snackbar.show();

            GetStudentInfo getStudentInfo = new GetStudentInfo();
            getStudentInfo.execute("http://" + MainActivity.getServerIp() + ":8080/exam-project-8/ApiStudentsProgram/GetStudentsProgramByStudentId/"+s_id);


        }
    }

    public void changeStudentPassword(View view) {
        if (usernameStudentTextField.getText().toString().trim().length()>2 && usernameStudentPasswordTextField.getText().toString().trim().length()>7){
            stdn.setUsername(usernameStudentTextField.getText().toString().trim());
            stdn.setPassword(usernameStudentPasswordTextField.getText().toString().trim());

            Log.i("usernameStudentTex","usernameStudentTextField");

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                 jsonResult  = objectMapper.writeValueAsString(stdn);
                UpdateUsernamePassword updateUsernamePassword = new UpdateUsernamePassword();
                updateUsernamePassword.execute("http://" + MainActivity.getServerIp() + ":8080/exam-project-8/ApiStudent/UpdateStudent");
                Log.i("jsonResult",jsonResult);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }else{
            if (usernameStudentTextField.getText().toString().trim().length()<3){
                new MainActivity().buildDialog(this,"Username Error","Username must be greater than 3 character.").show();
            }
            if (usernameStudentPasswordTextField.getText().toString().trim().length()<8){
                new MainActivity().buildDialog(this,"Password Error","Password must be greater than 7 character.").show();
            }
        }

    }

}
