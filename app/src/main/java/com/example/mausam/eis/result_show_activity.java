package com.example.mausam.eis;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class result_show_activity extends AppCompatActivity implements View.OnClickListener {
    static SharedPreferences saveUserCrediantial;
    private String s_id;
    //for semester toolbar
    private LinearLayout semLayout;

    Button btnSem;
    Button btnExamtype;
    private int currentSemester;

    //for showiing result
    private LinearLayout resultSection;
    private LinearLayout resultContainerLayout;
    private LinearLayout pp;
    TextView whichTerm;
    TextView resultSn;
    TextView resultSubject;
    TextView resultFmPm;
    TextView resultMarksObtain;
    TextView resultPassFail;
    LinearLayout result;
    GridLayout resultGridView;
    TextView totalFailed;

    View child;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_show_activity);
        saveUserCrediantial = getApplicationContext().getSharedPreferences("com.example.mausam.eis", Context.MODE_PRIVATE);


        String value = getIntent().getExtras().getString("mode");
        if (value.equalsIgnoreCase("parent")) {
            s_id = saveUserCrediantial.getString("studentId", "");
        } else {
            String s = getIntent().getExtras().getString("studentMode_s_id");
            s_id = s;
        }


        Log.i("resll", s_id);
        semLayout = (LinearLayout) findViewById(R.id.semButton);
        resultContainerLayout = (LinearLayout) findViewById(R.id.resultContainerLayout);
        pp = (LinearLayout) findViewById(R.id.pp);
        resultSection = (LinearLayout) findViewById(R.id.resultSection);
        whichTerm = (TextView) findViewById(R.id.whichTerm);
        resultSn = (TextView) findViewById(R.id.resultSn);
        resultSubject = (TextView) findViewById(R.id.resultSubject);
        resultFmPm = (TextView) findViewById(R.id.resultFmPm);
        resultMarksObtain = (TextView) findViewById(R.id.resultMarksObtain);
        resultPassFail = (TextView) findViewById(R.id.resultPassFail);
        result = (LinearLayout) findViewById(R.id.result);
        resultGridView = (GridLayout) findViewById(R.id.resultGridView);
        totalFailed = (TextView) findViewById(R.id.totalFailed);

        GetStudentInfo getStudentInfo = new GetStudentInfo();
        getStudentInfo.execute("http://" + MainActivity.ip + ":8080/exam-project-8/ApiStudent/GetStudent/" + s_id);
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

                btnSem = new Button(result_show_activity.this);
                btnSem.setId(i + 1);
                btnSem.setText("" + (i + 1));
                btnSem.setTag(i);
                btnSem.setTextColor(getResources().getColor(R.color.white));
                btnSem.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.weight = 1;
                btnSem.setLayoutParams(p);
                semLayout.addView(btnSem);
                btnSem.setOnClickListener(result_show_activity.this);

            }


        }
    }

    class GetResult extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = new ReUsableClass().getMethod("GET", urls);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            Log.i("results", result);
            int failCount = 0;
            try {
                JSONArray j = new JSONArray(result);
                Log.i("results", "" + j.length());
                for (int i = 0; i < j.length(); i++) {
                    JSONObject rec = j.getJSONObject(i);
                    String whichTerms = rec.getString("type_name");
                    String subject = rec.getString("subject_name");
                    String fm = rec.getString("full_marks");
                    String pm = rec.getString("pass_marks");
                    String fmpm = fm + " / " + pm;
                    String marks = rec.getString("obtained_marks");
                    String passfail = rec.getString("grade");

                    Log.i("results", whichTerms + " " + subject + " " + fmpm + " " + marks + " " + passfail);


                    if (!passfail.equalsIgnoreCase("null")) {

                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        child = (View) inflater.inflate(R.layout.result, null);


                        TextView whichTerm = (TextView) child.findViewById(R.id.whichTerm);
                        whichTerm.setText(whichTerms);

                        TextView resultSn = (TextView) child.findViewById(R.id.resultSn);
                        resultSn.setText("" + (i + 1));

                        TextView resultSubject = (TextView) child.findViewById(R.id.resultSubject);
                        resultSubject.setText(subject);

                        TextView resultFmPm = (TextView) child.findViewById(R.id.resultFmPm);
                        resultFmPm.setText(fmpm);

                        TextView resultMarksObtain = (TextView) child.findViewById(R.id.resultMarksObtain);
                        resultMarksObtain.setText(marks);

                        TextView resultPassFail = (TextView) child.findViewById(R.id.resultPassFail);
                        resultPassFail.setText(passfail);

                        if (passfail.equalsIgnoreCase("f")) {
                            whichTerm.setBackgroundColor(getResources().getColor(R.color.red));
                            whichTerm.setTextColor(getResources().getColor(R.color.white));

                            resultSn.setBackgroundColor(getResources().getColor(R.color.red));
                            resultSn.setTextColor(getResources().getColor(R.color.white));

                         /*   resultSubject.setBackgroundColor(getResources().getColor(R.color.red));
                            resultSubject.setTextColor(getResources().getColor(R.color.white));*/

                            resultFmPm.setBackgroundColor(getResources().getColor(R.color.red));
                            resultFmPm.setTextColor(getResources().getColor(R.color.white));

                            resultMarksObtain.setBackgroundColor(getResources().getColor(R.color.red));
                            resultMarksObtain.setTextColor(getResources().getColor(R.color.white));

                            resultPassFail.setBackgroundColor(getResources().getColor(R.color.red));
                            resultPassFail.setTextColor(getResources().getColor(R.color.white));

                            failCount++;
                        }
                        totalFailed.setText("Total Failed : " + failCount);
                        pp.addView(child);
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View view) {
        int semester = view.getId();
        //view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        int count = pp.getChildCount();
        Log.i("countm", "= " + count);
        if (count > 0) {
            pp.removeAllViewsInLayout();
        }

        GetResult getResult = new GetResult();
        getResult.execute("http://" + MainActivity.ip + ":8080/exam-project-8/ApiStudentsExams/GetStudentExamByStudentIdAndSemesterNo/" + s_id + "/" + semester);
    }
}
