package project.cse3310;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class viewSchedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        TextView appointments = (TextView) findViewById(R.id.appointment);
        appointments.setText("William Sizemore \n Monday 1pm");
    }
}
