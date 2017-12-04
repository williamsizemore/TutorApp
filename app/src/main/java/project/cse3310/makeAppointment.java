package project.cse3310;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class makeAppointment extends AppCompatActivity {

    private Button confirmButton;
    private UserData userData;
    private TextView profileName;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String stdntName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);

        userData = getIntent().getExtras().getParcelable("User");

        confirmButton = findViewById(R.id.button9);
        profileName = findViewById(R.id.profile_name);
        profileName.setText(userData.getName() +"\n"+userData.getHours());
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Spinner spinner = findViewById(R.id.appt_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.appt_hours, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        String[] availDays = getDays();
        Spinner days = findViewById(R.id.appt_days_spinner);
        ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,availDays);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        days.setAdapter(daysAdapter);
        days.setPrompt("Select Day");

    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("User",userData);
        setResult(RESULT_OK,intent);
        finish();
        overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
    }
    @Override
    public void onPause(){
        finish();
        overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
        super.onPause();
    }
    public String[] getDays(){
        String days = userData.getDays();
        String[] availDays = days.split("(?=[A-Z])");
        for (int i=0; i < availDays.length; i++){
            switch (availDays[i]){
                case "Mon":
                    availDays[i] = "Monday";
                    break;
                case "Tues":
                    availDays[i] = "Tuesday";
                    break;
                case "Wed":
                    availDays[i] = "Wednesday";
                    break;
                case "Thur":
                    availDays[i] = "Thursday";
                    break;
                case "Fri":
                    availDays[i] = "Friday";
                    break;
                case "Sat":
                    availDays[i] = "Saturday";
                    break;
                case "Sun":
                    availDays[i] = "Sunday";
                    break;
                default:
                    break;
            }
            Log.d("DAYS", availDays[i]);
        }
        return availDays;
    }

    public void cancelMakeAppointment(View view){
        finish();
        overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
    }
    public void confirm(View view)
    {
        Spinner daySpinnerView = findViewById(R.id.appt_days_spinner);
        Spinner hourSpinnerView = findViewById(R.id.appt_spinner);
        if (daySpinnerView.getSelectedItem().toString().equals("") || daySpinnerView.getSelectedItem().toString() == null) {
            Toast.makeText(makeAppointment.this,"Please select a day",Toast.LENGTH_SHORT).show();
        }
        else {
            databaseReference = FirebaseDatabase.getInstance().getReference();
            Random random = new Random();
            String randID = ""+ random.nextInt(1000);
            Appointment appt = new Appointment(user.getUid(), userData.getName(),daySpinnerView.getSelectedItem().toString(),hourSpinnerView.getSelectedItem().toString(),"valid",randID );
            databaseReference.child("appointments").push().setValue(appt);
            Intent intent = new Intent(this, Payment.class);
            intent.putExtra("User", userData);
            intent.putExtra("Appointment", appt);
            startActivity(intent);
        }
    }
}
