package project.cse3310;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TutorAddSession extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();

    private Calendar date;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button mGetDisplayDate;
    private TextView mDateValue;
    private Button mSubmit;
    private Spinner mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_add_session);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mGetDisplayDate = findViewById(R.id.setup_tutoring_date_button);
        mDateValue = findViewById(R.id.setup_tutoring_datevalue_text);
        mSubmit = findViewById(R.id.setup_tutoring_submit_button);
        mCategory = findViewById(R.id.tutor_category_spinner);

        mGetDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(TutorAddSession.this,android.R.style.Theme_Holo_Light_Dialog,mDateSetListener,year,month,day);
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                date = Calendar.getInstance();
                    date.set(year,month,day);
                SimpleDateFormat formatter = new SimpleDateFormat("DD-MM-yyyy",Locale.US);
                    mDateValue.setText(formatter.format(date));
            }
        };

        mSubmit.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                TutoringSession session = new TutoringSession(mCategory.getSelectedItem().toString(),date);
                mDatabase.child("users").child(currentuser.getUid()).child("sessions").push().setValue(session);
            }
        });
    }
}
