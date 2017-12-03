package project.cse3310;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Appointments_Menu extends AppCompatActivity {
    private UserData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments__menu);

        userData = getIntent().getExtras().getParcelable("User");

    }

    public void makeAppointment(View view)
    {
        Intent intent = new Intent(this, makeAppointment.class);
        intent.putExtra("User",userData);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
    }

    public void viewAppointments(View view)
    {
        Intent intent = new Intent(this, viewSchedule.class);
        intent.putExtra("User",userData);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("User",userData);
        setResult(RESULT_OK,intent);
        finish();
        //super.onBackPressed();
    }
    @Override
    public void onPause(){
        Intent intent = new Intent();
        intent.putExtra("User",userData);
        setResult(RESULT_OK,intent);
        finish();
        super.onPause();
        overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                userData = data.getExtras().getParcelable("User");
            }
        }
    }
}
