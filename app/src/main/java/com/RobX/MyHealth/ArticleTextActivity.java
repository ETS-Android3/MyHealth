package com.RobX.MyHealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ArticleTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_text);

        // Set the colour of status bar
        int flags = getWindow().getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));

        // region Declare and Link Variables
        TextView article = findViewById(R.id.ArticleText);
        TextView title = findViewById(R.id.ArticleTitle);
        TextView date = findViewById(R.id.ArticleDate);
        TextView author = findViewById(R.id.ArticleAuthor);
        ImageView image = findViewById(R.id.ArticleImage);
        NewsDatabaseHelper newsDatabaseHelper = new NewsDatabaseHelper(getApplicationContext());
        // endregion

        // Get the type and id of the article from calling intent
        int type = getIntent().getIntExtra("type", -1);
        String id = getIntent().getStringExtra("id");

        // Display the full text of the article
        // Based on the type of article, different functions will be called
        switch (type) {
            case 0:
                article.setText(newsDatabaseHelper.getPSText(id));
                title.setText(newsDatabaseHelper.getPSTitle(id));
                date.setText(newsDatabaseHelper.getPSDate(id));
                author.setText(newsDatabaseHelper.getPSAuthor(id));
                byte[] byteArray0 = newsDatabaseHelper.getPSImage(id);
                Bitmap bitmap0 = BitmapFactory.decodeByteArray(byteArray0, 0, byteArray0.length);
                image.setImageBitmap(bitmap0);
                break;
            case 1:
                article.setText(newsDatabaseHelper.getRCText(id));
                title.setText(newsDatabaseHelper.getRCTitle(id));
                date.setText(newsDatabaseHelper.getRCDate(id));
                author.setText(newsDatabaseHelper.getRCAuthor(id));
                byte[] byteArray1 = newsDatabaseHelper.getRCImage(id);
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.length);
                image.setImageBitmap(bitmap1);
                break;
            case 2:
                article.setText(newsDatabaseHelper.getVKText(id));
                title.setText(newsDatabaseHelper.getVKTitle(id));
                date.setText(newsDatabaseHelper.getVKDate(id));
                author.setText(newsDatabaseHelper.getVKAuthor(id));
                byte[] byteArray2 = newsDatabaseHelper.getVKImage(id);
                Bitmap bitmap2 = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2.length);
                image.setImageBitmap(bitmap2);
                break;
        }
    }
}