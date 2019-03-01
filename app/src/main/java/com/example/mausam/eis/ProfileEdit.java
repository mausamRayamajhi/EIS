package com.example.mausam.eis;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProfileEdit extends AppCompatActivity {

   private EditText fnameStudent;
    private EditText mnameStudent;
    private EditText lnameStudent;
    private Spinner genderStudent;
    private EditText    phoneStudent;
    private EditText dobStudent;
    private EditText addressStudent;
    private EditText emailStudent;

    Student model;
    String jsonResult;

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

            Snackbar snackbar= Snackbar.make(emailStudent,"Updated.",Snackbar.LENGTH_LONG);
            snackbar.show();

            Intent profile = new Intent(getApplicationContext(), Profile.class);
            profile.putExtra("studentId", String.valueOf(model.getS_id()));
            startActivity(profile);


        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

          model = (Student) getIntent().getSerializableExtra("studentObject");

        fnameStudent =(EditText)findViewById(R.id.fnameStudent);
        mnameStudent =(EditText)findViewById(R.id.mnameStudent);
        lnameStudent =(EditText)findViewById(R.id.lnameStudent);
        genderStudent =(Spinner) findViewById(R.id.genderStudent);
        phoneStudent =(EditText)findViewById(R.id.phoneStudent);
        dobStudent =(EditText)findViewById(R.id.dobStudent);
        addressStudent =(EditText)findViewById(R.id.addressStudent);
        emailStudent =(EditText)findViewById(R.id.emailStudent);

        fnameStudent.setText(""+model.getFirst_name());
        mnameStudent.setText(""+model.getMiddle_name());
        lnameStudent.setText(""+model.getLast_name());
        phoneStudent.setText(""+model.getPhone());
        dobStudent.setText(""+model.getDate_of_birth());
        addressStudent.setText(""+model.getAddress());
        emailStudent.setText(""+model.getEmail());

        ArrayAdapter<String> myAdp= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.gender));
        myAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderStudent.setAdapter(myAdp);

        if (model.getGender()==1){
            genderStudent.setSelection(0);
        }else{
            genderStudent.setSelection(1);
        }


        genderStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    model.setGender(1);
                }else{
                    model.setGender(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    public  void  editStudentProfile(View view){

        model.setFirst_name(fnameStudent.getText().toString().trim());
        model.setMiddle_name(mnameStudent.getText().toString().trim());
        model.setLast_name(lnameStudent.getText().toString().trim());
        model.setDate_of_birth(dobStudent.getText().toString().trim());
        model.setPhone(phoneStudent.getText().toString().trim());
        model.setAddress(addressStudent.getText().toString().trim());
        model.setEmail(emailStudent.getText().toString().trim());

Log.i("modelstudentsmm",model.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonResult  = objectMapper.writeValueAsString(model);
        UpdateUsernamePassword updateUsernamePassword = new UpdateUsernamePassword();
            updateUsernamePassword.execute("http://" + MainActivity.getServerIp() + ":8080/exam-project-8/ApiStudent/UpdateStudent");

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
