package com.example.mausam.eis;

import android.content.Intent;
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

public class StudentDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String student_id;

    private Toolbar toolbar;

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mToggle;

    private NavigationView s_navigationView;



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
                    TextView userName=(TextView)findViewById(R.id.userName);
                    TextView details =(TextView)findViewById(R.id.details);
                    userName.setText(student.getString("first_name")+" "+student.getString("middle_name")+" "+student.getString("last_name" ));
                    details.setText(student.getString("email"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        student_id=getIntent().getExtras().getString("s_id");


        toolbar = (Toolbar) findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Dashboard");



        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        s_navigationView=(NavigationView)findViewById(R.id.s_navigationView);
        s_navigationView.setNavigationItemSelectedListener(this);
        s_navigationView.bringToFront();


        GetStudentInfo getStudentInfo = new GetStudentInfo();
        getStudentInfo.execute("http://" + MainActivity.ip + ":8080/exam-project-8/ApiStudentsProgram/GetStudentsProgramByStudentId/"+student_id);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)){

            return  true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();

        if (id == R.id.s_examRoutine){
            Intent routine = new Intent(getApplicationContext(), Routine.class);
            routine.putExtra("studentMode_s_id", student_id);
            routine.putExtra("mode", "student");
            startActivity(routine);

            mDrawerLayout.closeDrawers();
        }
        if (id == R.id.s_result){
            Intent showResult = new Intent(getApplicationContext(), result_show_activity.class);
            showResult.putExtra("studentMode_s_id", student_id);
            showResult.putExtra("mode", "student");
            startActivity(showResult);
            mDrawerLayout.closeDrawers();
        }
        if (id == R.id.s_viewSubject){
            Intent showSubject = new Intent(getApplicationContext(), Show_Subject.class);
            showSubject.putExtra("subject_s_id", student_id);
            mDrawerLayout.closeDrawers();
            startActivity(showSubject);
        }
        if (id == R.id.s_profile){
            Intent profile = new Intent(getApplicationContext(), Profile.class);
            profile.putExtra("studentId", student_id);
            mDrawerLayout.closeDrawers();
            startActivity(profile);

        }
        if (id == R.id.s_forum){
            Intent forum = new Intent(getApplicationContext(), Forum.class);
            mDrawerLayout.closeDrawers();
            startActivity(forum);
        }
        if (id == R.id.s_settings){
            Intent setting = new Intent(getApplicationContext(), Setting.class);
            setting.putExtra("studentId", student_id);
            mDrawerLayout.closeDrawers();
            startActivity(setting);
        }
        if (id == R.id.s_logout){
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            mDrawerLayout.closeDrawers();
        }
        mDrawerLayout.closeDrawers();
        return false;
    }

}
