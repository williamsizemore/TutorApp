package project.cse3310;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Review extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference fDatabase;
    private UserData userData;
    private TextView tutorName;
    private RatingBar ratingBar;
    private EditText message;
    private ProgressDialog progressDialog;
    private ArrayList<Rating> ratings= new ArrayList<>();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        mAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tutors = mDatabase.getReference("reviews");

        userData = getIntent().getExtras().getParcelable("User");

        tutorName = findViewById(R.id.tutor_name);
        ratingBar = findViewById(R.id.set_rating);
        message = findViewById(R.id.review_comment);
        setTitle(userData.getName());
        tutorName.setText(userData.getName());
        ratingBar.setRating(userData.getRating());

        tutors.orderByChild("tutorName").equalTo(userData.getName()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Rating review = dataSnapshot.getValue(Rating.class);
                ratings.add(review);
                calculateAverage();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void submitReview(View view){
        message.setError(null);
        if (message.getText().toString().equals("") || message.getText() == null){
            Toast.makeText(Review.this, "Please Enter Your Comment", Toast.LENGTH_SHORT).show();
            message.setError("Required");
        }
        else {
            Rating rating = new Rating(ratingBar.getRating(), message.getText().toString(), userData.getName());
            fDatabase.child("reviews").push().setValue(rating);
            changeApptStatus();
            Toast.makeText(Review.this, "Review posted successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Main.class));
            //finish();
            overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);

        }
    }

    /* set status of appointment to reviewed */
    public void changeApptStatus(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final String[] appointmentID = new String[1];
        databaseReference.child("appointments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Appointment> appointmentList = new ArrayList<>();
                FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Appointment temp = ds.getValue(Appointment.class);
                    if (ds.getValue(Appointment.class).getTutorName().equals(userData.getName()) &&
                            ds.getValue(Appointment.class).getStudentUID().equals(curUser.getUid()) &&
                            ds.getValue(Appointment.class).getStatus().equals("valid"))
                    {
                        appointmentID[0] = ds.getKey().toString();
                    }
                    appointmentList.add(temp);
                }

                for (int i=0; i<appointmentList.size();i++){
                    if (appointmentList.get(i).getStudentUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) &&
                            appointmentList.get(i).getTutorName().equals(userData.getName()) &&
                            appointmentList.get(i).getStatus().equals("valid"))
                    {
                        appointmentList.get(i).setStatus("reviewed");
                        Log.d("app status",appointmentList.get(i).toString());
                        databaseReference.child("appointments").child(appointmentID[0]).setValue(appointmentList.get(i));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void calculateAverage(){
        float avg, sum=0; int count = 0;
        for (int i =0; i < ratings.size(); i++){
            sum += ratings.get(i).getRating();
            count++;
        }
        avg = sum / count;
        userData.setRating(avg);

        DatabaseReference fdb = FirebaseDatabase.getInstance().getReference("tutors");
        fdb.orderByChild("name").equalTo(userData.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){
                    Log.d("PARENT", "PARENT: " + childDataSnapshot.getKey());
                    Log.d("TAG",""+childDataSnapshot.child("name").getValue());
                    setTutorRating(childDataSnapshot.getKey(),userData);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setTutorRating(String uid, UserData user){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("tutors").child(uid).setValue(user);
    }
    public void cancelReview(View view){
        finish();
        overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
    }
}
