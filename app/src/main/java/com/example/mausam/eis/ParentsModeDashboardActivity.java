package com.example.mausam.eis;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import android.util.Log;
import android.view.MenuItem;


import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParentsModeDashboardActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    private String s_id;
    private String pcontact;

    private Toolbar toolbar;

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mToggle;


    //display student info
    TextView stName;
    TextView stfaculty;
    TextView stContact;
    TextView stSemester;
    TextView stId1;
    TextView passout;



    class GetStudentInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = new ReUsableClass().getMethod("GET", urls);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            Log.i("msgFormserverresult",result);


            try {
                JSONArray j  = new JSONArray(result);
                for (int i = 0; i < j.length(); i++) {
                    JSONObject rec = j.getJSONObject(i);
                    JSONObject program = rec.getJSONObject("program");
                    String  program_name= program.getString("program_name");
                    stfaculty.setText(program_name);
                    JSONObject student = rec.getJSONObject("student");
                    stName.setText(student.getString("first_name")+" "+student.getString("middle_name")+" "+student.getString("last_name" ));
                    stSemester.setText("Semester : "+student.getString("current_semester"));

                    stContact.setText("Phone : "+student.getString("phone"));
                    stId1.setText("DOB : "+student.getString("s_id"));



                    int status=student.getInt("status");


                    if (status==1){
                        passout.setText("Active");
                    }else{
                        passout.setText("Passout");
                        passout.setBackgroundColor(getResources().getColor(R.color.red));
                    }



                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    class ParentsInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = new ReUsableClass().getMethod("GET", urls);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);



            try {
                JSONArray array= new JSONArray(result);

                for (int i=0;i<array.length();i++){
                    JSONObject obj = array.getJSONObject(i);
                    String parentsName = obj.getString("fullname");
                    String primary_contact = obj.getString("primary_contact");
                    String relation = obj.getString("relation");

                    TextView userName=(TextView)findViewById(R.id.userName);
                    TextView details =(TextView)findViewById(R.id.details);

                    userName.setText(parentsName);
                    details.setText(primary_contact);


                    Log.i("msgFormserver",parentsName + "  "+ primary_contact+"  "+relation);
                }


            } catch (JSONException e) {

                e.printStackTrace();
            }


        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_mode_dashboard);

      SharedPreferences saveUserCrediantial = getApplicationContext().getSharedPreferences("com.example.mausam.eis", Context.MODE_PRIVATE);

        s_id = Integer.toString(getIntent().getExtras().getInt("studentId"));
        pcontact = saveUserCrediantial.getString("parentsNumber", "");

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Parents Mode");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //this creates a back button

        NavigationView navigationView= (NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        stName =(TextView)findViewById(R.id.stName);
        stfaculty =(TextView)findViewById(R.id.stfaculty);
        stContact =(TextView)findViewById(R.id.stContact);
        stSemester =(TextView)findViewById(R.id.stSemester);
        stId1 =(TextView)findViewById(R.id.stId);
        passout =(TextView)findViewById(R.id.passout);

        GetStudentInfo getStudentInfo = new GetStudentInfo();
        getStudentInfo.execute("http://" + MainActivity.getServerIp() + ":8080/exam-project-8/ApiStudentsProgram/GetStudentsProgramByStudentId/" + s_id);


        ParentsInfo parentsInfo = new ParentsInfo();
        parentsInfo.execute("http://" + MainActivity.getServerIp() + ":8080/exam-project-8/ApiStudentParent/GetStudentParentByStudentIdAndContact/" + s_id+"/"+pcontact);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();


        if (id == R.id.pexamRoutine){
            Intent routine = new Intent(getApplicationContext(), Routine.class);
            routine.putExtra("mode", "parent");
            startActivity(routine);

            mDrawerLayout.closeDrawers();
        }
        if (id == R.id.presult){
            Toast.makeText(this,"presult",Toast.LENGTH_SHORT).show();

            Intent showResult = new Intent(getApplicationContext(), result_show_activity.class);
            showResult.putExtra("mode", "parent");
            startActivity(showResult);

            mDrawerLayout.closeDrawers();
        }
        if (id == R.id.psettings){
            Toast.makeText(this,"psettings",Toast.LENGTH_SHORT).show();
            mDrawerLayout.closeDrawers();
        }
        if (id == R.id.plogout){
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            mDrawerLayout.closeDrawers();
        }
        mDrawerLayout.closeDrawers();
        return false;
    }
}
