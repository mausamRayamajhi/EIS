package com.example.mausam.eis;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Show_Subject extends AppCompatActivity implements  View.OnClickListener{

    String s_id;
    private int currentSemester;
    int specificSemesterClicked;
    Button btnSem;
    LinearLayout semButtonSubject;
    LinearLayout ppSubject;
    int programId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__subject);
        s_id = getIntent().getExtras().getString("subject_s_id");
        semButtonSubject=(LinearLayout)findViewById(R.id.semButtonSubject);
        ppSubject=(LinearLayout)findViewById(R.id.ppSubject);

        GetStudentInfo getStudentInfo = new GetStudentInfo();
        getStudentInfo.execute("http://" + MainActivity.ip + ":8080/exam-project-8/ApiStudent/GetStudent/" + s_id);
    }

    class GetSubject extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = new ReUsableClass().getMethod("GET", urls);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("subjectsFormserver",result);

            try {
                JSONArray j = new JSONArray(result);
                for (int i = 0; i < j.length(); i++) {
                    JSONObject rec = j.getJSONObject(i);
                    String subjectName = rec.getString("subject_name");
                    String subject_code = rec.getString("subject_code");
                    String theory_cr = rec.getString("theory_cr");
                    String internal_practical = rec.getString("internal_practical");
                    String internal_theory = rec.getString("internal_theory");

                    Log.i("subName",subjectName);

                    if (true) {

                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View  child = (View) inflater.inflate(R.layout.subject, null);


                        TextView  subjectN=(TextView)child.findViewById(R.id.subjectNames);
                        subjectN.setText(subjectName);

                        TextView  subjectCred=(TextView)child.findViewById(R.id.subjectCredit);
                        subjectCred.setText(theory_cr);

                        TextView   subjectInt=(TextView)child.findViewById(R.id.subjectInternal);
                        subjectInt.setText(internal_theory);

                        TextView  subjectPra=(TextView)child.findViewById(R.id.subjectPractical);
                        subjectPra.setText(internal_practical);

                        TextView subjectCod=(TextView)child.findViewById(R.id.subjectCode);
                        subjectCod.setText(subject_code);

                        TextView totalSubject =(TextView)findViewById(R.id.totalSubject);
                        totalSubject.setText("Total Subjects : " + (i+1));


                        ppSubject.addView(child);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class GetStudentProgram extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = new ReUsableClass().getMethod("GET", urls);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("studentProgram",result);



            try {
                JSONArray j  = new JSONArray(result);
                for (int i = 0; i < j.length(); i++) {
                    JSONObject rec = j.getJSONObject(i);
                JSONObject program = rec.getJSONObject("program");
                    programId= program.getInt("program_id");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }



            GetSubject getSubject = new GetSubject();
            getSubject.execute("http://" + MainActivity.ip + ":8080/exam-project-8/ApiSubject/GetSubjectByParameters/" + programId + "/" + specificSemesterClicked);

        }
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

            Pattern pattern = Pattern.compile("<current_semester>(.*?)</current_semester>");
            Matcher matcher = pattern.matcher(result);
            while (matcher.find()) {

                currentSemester = Integer.parseInt(matcher.group(1));

            }

            for (int i = 0; i < currentSemester; i++) {

                btnSem = new Button(Show_Subject.this);
                btnSem.setId(i + 1);
                btnSem.setText("" + (i + 1));
                btnSem.setTag(i);
                btnSem.setTextColor(getResources().getColor(R.color.white));
                btnSem.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.weight = 1;
                btnSem.setLayoutParams(p);
                semButtonSubject.addView(btnSem);
                btnSem.setOnClickListener(Show_Subject.this);

            }


        }
    }
    @Override
    public void onClick(View view) {

         specificSemesterClicked = view.getId();
        //view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        int count = ppSubject.getChildCount();
        Log.i("countm", "= " + count);
        if (count > 0) {
            ppSubject.removeAllViewsInLayout();
        }

        GetStudentProgram studentProgram = new GetStudentProgram();
        studentProgram.execute("http://" + MainActivity.ip + ":8080/exam-project-8/ApiStudentsProgram/GetStudentsProgramByStudentId/" + s_id );


          }
}
