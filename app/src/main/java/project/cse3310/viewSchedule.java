package project.cse3310;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class viewSchedule extends AppCompatActivity {
    private UserData userData;
    private String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);
        userData = getIntent().getExtras().getParcelable("User");
        time = getIntent().getExtras().getString("Time");

        TextView appointments = findViewById(R.id.appointment);
        if (time == null || time.equals(""))
            appointments.setText("No Appointments to show");
        else
            appointments.setText( userData.getName()+"\n" +  time);

    }
    public void reviewTutor(View view){

        Intent review = new Intent(this, Review.class);
        review.putExtra("User",userData);
        startActivity(review);
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
    }
    public void apptToReview(View view){
        Intent review = new Intent(this, Review.class);
        review.putExtra("User",userData);
        startActivity(review);
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("User",userData);
        setResult(RESULT_OK,intent);
        finish();
    }
    public void goHome(View view){
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
