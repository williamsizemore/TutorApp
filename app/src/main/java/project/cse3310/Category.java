package project.cse3310;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Category extends AppCompatActivity {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private ListView tutors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        String category = getIntent().getStringExtra("Category");
        
        ((TextView)findViewById(R.id.category_name)).setText(category);

        tutors = findViewById(R.id.tutors_listview);

        final ArrayList<String> names = new ArrayList<>();
        final ArrayList<UserData> data = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        tutors.setAdapter(arrayAdapter);

        tutors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent tutor = new Intent(Category.this,Communications.class);
                tutor.putExtra("User", data.get(position));
                startActivity(tutor);
                overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
            }
        });

        DatabaseReference tutors = mDatabase.getReference("tutors");
            tutors.orderByChild("category").equalTo(category).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    UserData user = dataSnapshot.getValue(UserData.class);
                        data.add(user);
                        names.add(user.getName() + " (" + user.getCategory() + ")");
                    arrayAdapter.notifyDataSetChanged();
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

    @Override
    public void onPause(){
        super.onPause();

        overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
    }
}
