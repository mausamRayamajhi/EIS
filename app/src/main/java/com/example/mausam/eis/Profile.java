package com.example.mausam.eis;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class Profile extends AppCompatActivity {

   private TextView student_name;
    private TextView student_program;
    private TextView student_semester;
    private TextView student_address;
    private TextView student_phone;
    private TextView student_dob;
    private TextView student_gender;
    private TextView student_email;
    private TextView student_status;

    Student  stdn;
    private int s_id;
//PROJECT GIT PUSH
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        s_id = Integer.parseInt(getIntent().getExtras().getString("studentId"));

        student_name = (TextView)findViewById(R.id.student_name);
        student_program = (TextView)findViewById(R.id.student_program);
        student_semester = (TextView)findViewById(R.id.student_semester);
        student_address = (TextView)findViewById(R.id.student_address);
        student_phone = (TextView)findViewById(R.id.student_phone);
        student_dob = (TextView)findViewById(R.id.student_dob);
        student_gender = (TextView)findViewById(R.id.student_gender);
        student_email = (TextView)findViewById(R.id.student_email);
        student_status = (TextView)findViewById(R.id.student_status);

        GetStudentInfo getStudentInfo = new GetStudentInfo();
        getStudentInfo.execute("http://" + MainActivity.getServerIp() + ":8080/exam-project-8/ApiStudentsProgram/GetStudentsProgramByStudentId/"+s_id);

    }
    public void editStudentProfile(View view){

        Intent editProfile = new Intent(getApplicationContext(), ProfileEdit.class);
        editProfile.putExtra("studentObject", (Serializable) stdn);
        startActivity(editProfile);

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
                    JSONObject program = rec.getJSONObject("program");
                  String  program_name= program.getString("program_name");
                    student_program.setText(program_name);
                    JSONObject student = rec.getJSONObject("student");
                    student_name.setText(student.getString("first_name")+" "+student.getString("middle_name")+" "+student.getString("last_name" ));
                    student_semester.setText("Semester : "+student.getString("current_semester"));
                    student_address.setText("Address : "+student.getString("address"));
                    student_phone.setText("Phone : "+student.getString("phone"));
                    student_dob.setText("DOB : "+student.getString("date_of_birth"));
                    student_email.setText(student.getString("email"));

                    int genderStatus=student.getInt("gender");
                    int status=student.getInt("status");

                    if (genderStatus==1 ){
                        student_gender.setText("Gender : Male");

                    }else{
                        student_gender.setText("Gender : Female");
                        student_status.setText("Passout");
                    }

                    if (status==1){
                        student_status.setText("Active");
                    }else{
                        student_status.setText("Passout");
                        student_status.setBackgroundColor(getResources().getColor(R.color.red));
                    }


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
                    stdn.setUsername(student.getString("username"));
                    if (student.getString("verificationCode")!=null){
                        stdn.setVerificationCode(student.getString("verificationCode"));
                    }

                    Log.i("studentInfofromProgile",stdn.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
}
