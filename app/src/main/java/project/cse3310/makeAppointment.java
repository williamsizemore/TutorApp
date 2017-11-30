package project.cse3310;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class makeAppointment extends AppCompatActivity {

    Button confirmButton;
    CheckBox twelveOne;
    CheckBox oneTwo;
    CheckBox twoThree;
    CheckBox threeFour;
    CheckBox fourFive;
    private UserData userData;
    private TextView profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);

        userData = getIntent().getExtras().getParcelable("User");
        //Making all the buttons invisible.
        confirmButton = findViewById(R.id.button9);
        //confirmButton.setVisibility(View.INVISIBLE);
        profileName = findViewById(R.id.profile_name);
        profileName.setText(userData.getName());

        Spinner spinner = findViewById(R.id.appt_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.appt_hours, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner days = findViewById(R.id.appt_days_spinner);
        ArrayAdapter<CharSequence> daysAdapter = ArrayAdapter.createFromResource(this,
                R.array.appt_days,android.R.layout.simple_spinner_dropdown_item);
        days.setAdapter(daysAdapter);


    }
    //TODO: FIX BACK BUTTON WITH PARCELABLE
    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("User",userData);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void chooseTime(View view)
    {
        //confirmButton.setVisibility(View.VISIBLE);

    }

    public void confirm(View view)
    {
        Spinner daySpinnerView = findViewById(R.id.appt_days_spinner);
        Spinner hourSpinnerView = findViewById(R.id.appt_spinner);

        String time = "" + daySpinnerView.getSelectedItem().toString() + " - " +hourSpinnerView.getSelectedItem().toString();
        Intent intent = new Intent(this, viewSchedule.class);
        intent.putExtra("User",userData);
        intent.putExtra("Time",time);
        startActivity(intent);

    }

}
