package project.cse3310;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Payment extends AppCompatActivity {
    private UserData userData;
    private Appointment appt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setTitle("Payment");

        userData = getIntent().getExtras().getParcelable("User");
        appt = getIntent().getExtras().getParcelable("Appointment");
    }
    public void confirmPayment(View view){
        EditText nameView = findViewById(R.id.card_name);
        EditText cardNumView= findViewById(R.id.card_number);
        EditText cvcView = findViewById(R.id.card_cvc);
        nameView.setError(null);
        cardNumView.setError(null);
        cvcView.setError(null);

        if (nameView.getText().toString().equals("") || nameView.getText().toString() == null){
            Toast.makeText(Payment.this, "Please enter the card name", Toast.LENGTH_SHORT).show();
            nameView.setError("Required");
        }
        else if (cardNumView.getText().toString().equals("") || cardNumView.getText().toString() == null){
            Toast.makeText(Payment.this, "Please enter card number", Toast.LENGTH_SHORT).show();
            cardNumView.setError("Required");
        }
        else if (!cardNumView.getText().toString().matches("[0-9]+")){
            Toast.makeText(Payment.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            cardNumView.setError("Digits only");
        }
        else if (cvcView.getText().toString().equals("") || cvcView.getText().toString() == null){
            Toast.makeText(Payment.this, "Please enter security code", Toast.LENGTH_SHORT).show();
            cvcView.setError("Required");
        }
        else if (!cvcView.getText().toString().matches("[0-9]+")){
            Toast.makeText(Payment.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            cvcView.setError("Digits only");
        }
       else {

            Intent intent = new Intent(Payment.this,viewSchedule.class);
            intent.putExtra("User",userData);
            //intent.putExtra("Appointment",appt);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
        }
    }

    public void cancelPayment(View view){
        finish();
    }

}
