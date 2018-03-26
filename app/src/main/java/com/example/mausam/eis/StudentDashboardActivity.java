package com.example.mausam.eis;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class StudentDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String student_id;

    private Toolbar toolbar;

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mToggle;

    private NavigationView s_navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        student_id=getIntent().getExtras().getString("s_id");
        Log.i("studentIdFromStud", student_id);
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
            Toast.makeText(this,"presult",Toast.LENGTH_SHORT).show();

            Intent showResult = new Intent(getApplicationContext(), result_show_activity.class);
            showResult.putExtra("studentMode_s_id", student_id);
            showResult.putExtra("mode", "student");
            startActivity(showResult);

            mDrawerLayout.closeDrawers();
        }
        if (id == R.id.s_settings){
            Toast.makeText(this,"psettings",Toast.LENGTH_SHORT).show();
            mDrawerLayout.closeDrawers();
        }
        if (id == R.id.s_viewSubject){
            Toast.makeText(this,"subject",Toast.LENGTH_SHORT).show();
            mDrawerLayout.closeDrawers();
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
