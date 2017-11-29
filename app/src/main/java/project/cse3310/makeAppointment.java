package project.cse3310;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class makeAppointment extends AppCompatActivity {

    Button confirmButton;
    CheckBox twelveOne;
    CheckBox oneTwo;
    CheckBox twoThree;
    CheckBox threeFour;
    CheckBox fourFive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);

        //Making all the buttons invisible.
        confirmButton = findViewById(R.id.button9);
        confirmButton.setVisibility(View.INVISIBLE);

        twelveOne = findViewById(R.id.checkBox4);
        twelveOne.setVisibility(View.INVISIBLE);

        oneTwo = findViewById(R.id.checkBox5);
        oneTwo.setVisibility(View.INVISIBLE);

        twoThree = findViewById(R.id.checkBox6);
        twoThree.setVisibility(View.INVISIBLE);

        threeFour = findViewById(R.id.checkBox7);
        threeFour.setVisibility(View.INVISIBLE);

        fourFive = findViewById(R.id.checkBox8);
        fourFive.setVisibility(View.INVISIBLE);
    }


    public void chooseTime(View view)
    {
        confirmButton.setVisibility(View.VISIBLE);
        twelveOne.setVisibility(View.VISIBLE);
        oneTwo.setVisibility(View.VISIBLE);
        twoThree.setVisibility(View.VISIBLE);
        threeFour.setVisibility(View.VISIBLE);
        fourFive.setVisibility(View.VISIBLE);

    }

    public void confirm(View view)
    {
        Intent intent = new Intent(this, viewSchedule.class);
        startActivity(intent);
    }

}
