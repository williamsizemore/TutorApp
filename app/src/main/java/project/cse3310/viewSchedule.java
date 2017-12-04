package project.cse3310;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewSchedule extends AppCompatActivity {
    private UserData userData;
    private Appointment appt;
    private Appointment appointment;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView appointmentsView;
    private ArrayList<String> apptList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView apptListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);
        //userData = getIntent().getExtras().getParcelable("User");

        apptListView = findViewById(R.id.appt_list);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("appointments");
        user = FirebaseAuth.getInstance().getCurrentUser();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,apptList);
        apptListView.setAdapter(adapter);

        databaseReference.orderByChild("studentUID").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Appointment temp = ds.getValue(Appointment.class);
                    if (temp.getStatus().equals("valid"))
                        apptList.add(temp.getTutorName() + "\n" + temp.getDay()+" - "+temp.getHour());
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        apptListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String[] name = apptListView.getItemAtPosition(i).toString().split("\n");
                System.out.println(name[0]);

                databaseReference = firebaseDatabase.getReference();

                databaseReference.child("tutors").orderByChild("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        loadTutor(dataSnapshot, name[0]);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("Failed: "+ databaseError.getMessage());
                    }
                });
            }
        });
    }
    public void loadTutor(DataSnapshot dataSnapshot, String value){
        ArrayList<UserData> userArray = new ArrayList<>();

        for (DataSnapshot ds: dataSnapshot.getChildren()){
            UserData userData = new UserData();

            userData.setName(ds.getValue(UserData.class).getName());
            userData.setCategory(ds.getValue(UserData.class).getCategory());
            userData.setEmail(ds.getValue(UserData.class).getEmail());
            userData.setState(ds.getValue(UserData.class).getState());
            userData.setUserType(ds.getValue(UserData.class).getUserType());
            userData.setDateOfBirth(ds.getValue(UserData.class).getDateOfBirth());
            userData.setZip(ds.getValue(UserData.class).getZip());
            userData.setPhone(ds.getValue(UserData.class).getPhone());
            userData.setDays(ds.getValue(UserData.class).getDays());
            userData.setHours(ds.getValue(UserData.class).getHours());
            userData.setAddress(ds.getValue(UserData.class).getAddress());
            userData.setRating(ds.getValue(UserData.class).getRating());

            userArray.add(userData);
        }
        for (int i =0; i < userArray.size(); i++) {
            if (userArray.get(i).getName().equalsIgnoreCase(value))
            {
                Intent intent = new Intent(this, Review.class);
                UserData tutor = userArray.get(i);

                //System.out.println(userData.toString() + " VIEWSCHEDULE TO REVIEW");
                intent.putExtra("User", tutor);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("User",userData);
        setResult(RESULT_OK,intent);
        finish();
    }
    public void goHome(View view){
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
    private boolean userLoggedIn(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }
}
