package project.cse3310;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
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

public class Search extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Query query;
    private ListView mListview;
    private ArrayList<String> users= new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //handleIntent(getIntent());
        setContentView(R.layout.activity_search);

        mListview = findViewById(R.id.search_list_view);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("tutors");

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, users);
        mListview.setAdapter(arrayAdapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                users.add(userData.getName() + "  (" + userData.getCategory() +")");
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
        mListview.setOnItemClickListener(Search.this);

    }

    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            //search(query);
            startSearch(query);
        }
    }

    @Override
    protected void onNewIntent(Intent intent){
        handleIntent(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                arrayAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem loginItem = menu.findItem(R.id.action_login);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            loginItem.setIcon(getResources().getDrawable(R.drawable.logout_icon));
        else
            loginItem.setIcon(getResources().getDrawable(R.drawable.login_icon));
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                if (!userLoggedIn()){
                    startActivity(new Intent(this, LoginAndReg.class));
                    overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                }
                else
                    logoutUser();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);    //enter and exit for going to prev
    }
    /* returns whether a user is currently logged in or not */
    private boolean userLoggedIn(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }
    private void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(Search.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
        //restart activity to refresh content and UI
        //startActivity(new Intent(this, Search.class));
        recreate();
    }

    public void startSearch(final String value){
        //LISTEN FOR SEARCH VALUES
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("tutors");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                search(dataSnapshot, value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void search(DataSnapshot dataSnapshot, String value){
        ArrayList<UserData> userArray = new ArrayList<>();
        ArrayList<UserData> resultArray = new ArrayList<>();
        for (DataSnapshot ds: dataSnapshot.getChildren()){
            UserData userData = new UserData();

            userData.setName(ds.getValue(UserData.class).getName());
            userData.setCategory(ds.getValue(UserData.class).getCategory());
            userData.setEmail(ds.getValue(UserData.class).getEmail());
            userData.setState(ds.getValue(UserData.class).getEmail());
            userData.setUserType(ds.getValue(UserData.class).getUserType());
            userArray.add(userData);
            System.out.println("USERNAME::::: " + userData.getName());
        }
        for (int i =0; i < userArray.size(); i++) {
            if (userArray.get(i).getName().endsWith(value) || userArray.get(i).getName().startsWith(value) || userArray.get(i).getName().equalsIgnoreCase(value)
                    || userArray.get(i).getCategory().equalsIgnoreCase(value)) {
                System.out.println("USER RESULT===== " + userArray.get(i).getName());
                resultArray.add(userArray.get(i));
            }

        }
        displayResult(resultArray);
    }
    public void displayResult(ArrayList<UserData> userArray){
        ListView resultView = findViewById(R.id.search_list_view);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println("---- ITEM WAS CLICKED ----"+ adapterView.getItemAtPosition(i).toString());
        //TODO: implement finding user == name, and going to communications page
    }
}
