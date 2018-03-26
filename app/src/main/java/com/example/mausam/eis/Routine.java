package com.example.mausam.eis;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Routine extends AppCompatActivity {
    View child;
    private LinearLayout ppp;
    static SharedPreferences saveUserCrediantial;
    private String s_id;
    private LinearLayout routineSection;
    private LinearLayout routineContainerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        saveUserCrediantial = getApplicationContext().getSharedPreferences("com.example.mausam.eis", Context.MODE_PRIVATE);

        String value = getIntent().getExtras().getString("mode");
        if (value.equalsIgnoreCase("parent")){
            s_id = saveUserCrediantial.getString("studentId", "");
        }else{
            String s = getIntent().getExtras().getString("studentMode_s_id");
            s_id =s;
        }

        ppp=(LinearLayout)findViewById(R.id.ppp);

        routineContainerLayout = (LinearLayout) findViewById(R.id.routineContainerLayout);
        routineSection = (LinearLayout) findViewById(R.id.routineSection);

        GetRoutine getRoutine = new GetRoutine();
        getRoutine.execute("http://" + MainActivity.ip + ":8080/exam-project-8/ApiExam/getRoutineForParentsMode/" + s_id);
    }

    class GetRoutine extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = new ReUsableClass().getMethod("GET", urls);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            Log.i("results", result);

            try {
                JSONArray j = new JSONArray(result);
                Log.i("results", "" + j.length());
                for (int i = 0; i < j.length(); i++) {
                    JSONObject rec = j.getJSONObject(i);
                    String whichTerms = rec.getString("type_name");
                    String subject = rec.getString("subject_name");
                    String fm = rec.getString("full_marks");
                    String pm = rec.getString("pass_marks");
                    String exam_date = rec.getString("exam_date");


                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    child = (View) inflater.inflate(R.layout.routine, null);


                    TextView whichTerm = (TextView) child.findViewById(R.id.whichTermr);
                    whichTerm.setText(whichTerms);

                    TextView routineSn = (TextView) child.findViewById(R.id.routineSn);
                    routineSn.setText("" + (i + 1));

                    TextView routineSub = (TextView) child.findViewById(R.id.routineSubject);
                    routineSub.setText(subject);

                    TextView resultFm = (TextView) child.findViewById(R.id.routineFm);
                    resultFm.setText(fm);

                    TextView resultpm = (TextView) child.findViewById(R.id.routinePm);
                    resultpm.setText(pm);

                    TextView date = (TextView) child.findViewById(R.id.routineDate);
                    date.setText(exam_date);

                    ppp.addView(child);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
