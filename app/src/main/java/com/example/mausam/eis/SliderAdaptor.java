package com.example.mausam.eis;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by default on 2/13/2018.
 */

public class SliderAdaptor extends PagerAdapter {

    private Context context;

    private LayoutInflater inflater;

    public SliderAdaptor(Context context){

        this.context=context;

    }

    public int[] slide_image = {

            R.drawable.parents,
            R.drawable.person,
            R.drawable.t
    };

    public String[] slide_heading={

            "PARENTS MODE",
            "PERMISSION",
            "Thank you"
    };

    public String slide_desc[] ={

            "This is a new feature that we have added to provide our students parents/guardians to know about their children activities related to examination, examimination schedule and reports.",
            "In order to activate parents mode in your device you need to provide some basic information about you and your children\n" +
                    "\n  Your mobile number." +
                    "\n  Student Id." +
                    "\n  Relation with student.",
            "Thank you for requestion Parents Mode service in your mobile device. And We are greatfull that we are concerned with your children activities so as we are."
    };

    @Override
    public int getCount() {
        return slide_heading.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImageView = (ImageView)view.findViewById(R.id.slideImage);
        TextView slideHeading = (TextView)view.findViewById(R.id.slideHeading);
        TextView slideDesc = (TextView)view.findViewById(R.id.slideDescription);


        slideImageView.setImageResource(slide_image[position]);
        slideHeading.setText(slide_heading[position]);
        slideDesc.setText(slide_desc[position]);

        container.addView(view);

        return  view;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
