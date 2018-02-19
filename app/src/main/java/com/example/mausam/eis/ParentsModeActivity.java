package com.example.mausam.eis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ParentsModeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout dots;
    private SliderAdaptor sliderAdaptor;
    private Button btnBack;
    private Button btnext;
    private int currentPage;
    private TextView[] mDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_mode);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        dots = (LinearLayout) findViewById(R.id.dots);

        btnBack = (Button) findViewById(R.id.btnBack);

        btnext = (Button) findViewById(R.id.btnNext);

        sliderAdaptor = new SliderAdaptor(this);

        viewPager.setAdapter(sliderAdaptor);

        addDotsIndicator(0);

        viewPager.addOnPageChangeListener(viewListner);

        btnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btnext.getText().toString().equalsIgnoreCase("Finish")) {

                    Toast.makeText(ParentsModeActivity.this, "Finished clicked", Toast.LENGTH_SHORT).show();

                    Intent parentsModeInfoGather= new Intent(getApplicationContext(),ParentsModeInfoGatherActivity.class);

                    startActivity(parentsModeInfoGather);

                } else {

                    viewPager.setCurrentItem(currentPage + 1);

                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewPager.setCurrentItem(currentPage - 1);

            }
        });

    }

    public void addDotsIndicator(int position) {

        mDots = new TextView[3];

        dots.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {

            mDots[i] = new TextView(this);

            mDots[i].setText(Html.fromHtml("&#8226;"));

            mDots[i].setTextSize(50);

            mDots[i].setTextColor(getResources().getColor(R.color.white));

            dots.addView(mDots[i]);
        }

        if (mDots.length > 0) {

            mDots[position].setTextColor(getResources().getColor(R.color.transpatentWhite));

        }
    }

    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);

            currentPage = position;

            if (position == 0) {

                btnext.setEnabled(true);
                btnBack.setEnabled(false);
                btnBack.setVisibility(View.INVISIBLE);

                btnext.setText("Next");
                btnBack.setText("");
            } else if (position == mDots.length - 1) {
                btnext.setEnabled(true);
                btnBack.setEnabled(true);
                btnBack.setVisibility(View.VISIBLE);

                btnext.setText("Finish");
                btnBack.setText("Back");
            } else {
                btnext.setEnabled(true);
                btnBack.setEnabled(true);
                btnBack.setVisibility(View.VISIBLE);

                btnext.setText("Next");
                btnBack.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
