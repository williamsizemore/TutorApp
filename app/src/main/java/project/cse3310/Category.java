package project.cse3310;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Category extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        String category = getIntent().getStringExtra("Category");

        ((TextView)findViewById(R.id.category_name)).setText(category);
    }
}
