package project.cse3310;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReviewsPage extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ListView mListview;
    private ArrayList<String> reviews = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private UserData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_page);
        userData = getIntent().getExtras().getParcelable("User");

        setTitle(userData.getName());
        mListview = findViewById(R.id.reviews_list);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("reviews");
        final RatingBar reviewStars;
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, reviews);
        mListview.setAdapter(arrayAdapter);

        databaseReference.orderByChild("reviews").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Rating rating = dataSnapshot.getValue(Rating.class);
                System.out.println("USERDATA OBJECT: "+userData.toString());
                System.out.println("RATING OBJECT: "+rating.toString());
                if (rating.getTutorName().equals(userData.getName()) && rating.getTutorName() != null) {
                    reviews.add("Rating: " + rating.getRating() + "/5\n" + rating.getMessage());
                    arrayAdapter.notifyDataSetChanged();
                }
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
}
