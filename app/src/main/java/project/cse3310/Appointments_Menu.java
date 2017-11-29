package project.cse3310;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Appointments_Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments__menu);
    }

    public void makeAppointment(View view)
    {
        Intent intent = new Intent(this, makeAppointment.class);
        startActivity(intent);
    }

    public void viewAppointments(View view)
    {
        Intent intent = new Intent(this, viewSchedule.class);
        startActivity(intent);
    }
}
