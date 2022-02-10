package com.RobX.MyHealth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Vector;

public class RumourClarificationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rumour_clarification, container, false);

        // region Instantiate classes and other tools as preparation
        NewsDatabaseHelper newsDatabaseHelper = new NewsDatabaseHelper(getContext());
        Vector<String> RCIDS = newsDatabaseHelper.getRumourClarificationsIDs();
        // region

        // Use a for loop to create the exact number of views
        for (int i = 0; i < RCIDS.size(); i ++)
        {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.article_tab, null);
            String ArticleID = RCIDS.get(i);

            // region Declare and Link Variables
            ImageView image = v.findViewById(R.id.articleImage);
            TextView title = v.findViewById(R.id.articleTitle);
            TextView date = v.findViewById(R.id.articleDate);
            // endregion

            // Set information and picture
            byte[] byteArray0 = newsDatabaseHelper.getRCImage(ArticleID);
            Bitmap bitmap0 = BitmapFactory.decodeByteArray(byteArray0, 0, byteArray0.length);
            image.setImageBitmap(bitmap0);
            title.setText(newsDatabaseHelper.getRCTitle(ArticleID));
            date.setText(newsDatabaseHelper.getRCDate(ArticleID));
            LinearLayout article = v.findViewById(R.id.article);
            article.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ArticleTextActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("id", ArticleID);
                    startActivity(intent);
                }
            });

            // Insert the newly-created view to the fragment
            ViewGroup insertPoint = view.findViewById(R.id.insert_point);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        // Inflate the layout for this fragment
        return view;
    }
}