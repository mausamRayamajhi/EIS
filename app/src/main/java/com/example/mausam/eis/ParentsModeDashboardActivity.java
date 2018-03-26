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

            StringBuffer stname = new StringBuffer();

            Pattern pattern = Pattern.compile("<current_semester>(.*?)</current_semester>");
            Matcher matcher = pattern.matcher(result);
            while (matcher.find()) {
              int  currentSemester = Integer.parseInt(matcher.group(1));
                stSemester.setText("Semester : "+currentSemester);
            }

            Pattern patternprogram_name = Pattern.compile("<program_name>(.*?)</program_name>");
            Matcher matcherprogram_name = patternprogram_name.matcher(result);
            while (matcherprogram_name.find()) {
                String  program_name = matcherprogram_name.group(1);
                stfaculty.setText(""+program_name);
            }



            Pattern phone = Pattern.compile("<phone>(.*?)</phone>");
            Matcher matcherphone = phone.matcher(result);
            while (matcherphone.find()) {
               String contact = matcherphone.group(1);
                stContact.setText(""+contact);
            }

            Pattern s_id = Pattern.compile("<s_id>(.*?)</s_id>");
            Matcher matchers_id = s_id.matcher(result);
            while (matchers_id.find()) {
              int  stId = Integer.parseInt(matchers_id.group(1));
                stId1.setText("ID : "+stId);
            }

            Pattern pass = Pattern.compile("<status>(.*?)</status>");
            Matcher mpass = pass.matcher(result);
            while (mpass.find()) {
               int pa= Integer.parseInt(mpass.group(1));
               if (pa==1){
                   passout.setText("Not Pass Out");
               }else{
                   passout.setText("Pass Out");
               }
            }


            Pattern name = Pattern.compile("<first_name>(.*?)</first_name>");
            Matcher matchername = name.matcher(result);
            while (matchername.find()) {
                stname .append( matchername.group(1)+" ");
            }

           Pattern namem = Pattern.compile("<middle_name>(.*?)</middle_name>");
            Matcher matchernamem = namem.matcher(result);
            while (matchernamem.find()) {
                stname .append( matchernamem.group(1)+" ");
            }
            Pattern nameml = Pattern.compile("<last_name>(.*?)</last_name>");
            Matcher matchernameml = nameml.matcher(result);
            while (matchernameml.find()) {
                stname .append( matchernameml.group(1));
            }
            stName.setText(""+stname);




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
        getStudentInfo.execute("http://" + MainActivity.ip + ":8080/exam-project-8/ApiStudentsProgram/GetStudentsProgramByStudentId/" + s_id);


        ParentsInfo parentsInfo = new ParentsInfo();
        parentsInfo.execute("http://" + MainActivity.ip + ":8080/exam-project-8/ApiStudentParent/GetStudentParentByStudentIdAndContact/" + s_id+"/"+pcontact);


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
