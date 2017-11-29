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

        userData = getIntent().getExtras().getParcelable("User");

        tutorName = findViewById(R.id.tutor_name);
        ratingBar = findViewById(R.id.set_rating);
        message = findViewById(R.id.review_comment);

        tutorName.setText(userData.getName());
        ratingBar.setRating(userData.getRating());

        mAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tutors = mDatabase.getReference("reviews");
        DataSnapshot dataSnapshot;

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
        Rating rating = new Rating(ratingBar.getRating(), message.getText().toString(), userData.getName());
        fDatabase.child("reviews").push().setValue(rating);
        //averageRating(userData.getName());
        finish();
        Toast.makeText(Review.this,"Review posted successfully!", Toast.LENGTH_SHORT).show();
    }
    public void calculateAverage(){
        float avg, sum=0; int count = 1;
        for (int i =0; i < ratings.size(); i++){
            sum += ratings.get(i).getRating();
            count++;
        }
        avg = sum / count;
        userData.setRating(avg);
        //TODO ISSUES HERE CAUSING PROBLEMS IN DATABASE
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
