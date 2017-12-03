package project.cse3310;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main extends AppCompatActivity{

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ListView categories = findViewById(R.id.main_categories_list);
        categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent category = new Intent(Main.this,Category.class);
                category.putExtra("Category", adapterView.getItemAtPosition(position).toString());
                startActivity(category);
                overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_login, menu);

        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
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
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_login:
                if (!userLoggedIn()) {
                    startActivity(new Intent(this, LoginAndReg.class));
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
                else
                    logoutUser();
                return true;
            case R.id.search:
                startActivity(new Intent(this, Search.class));
                overridePendingTransition(R.anim.slide_in,R.anim.slide_out); //slide in and out for starting activity
                return true;
            case R.id.schedule:
                if (userLoggedIn()){
                    startActivity(new Intent(this, viewSchedule.class));
                    overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                }
                else {
                    Toast.makeText(Main.this,"You need to be logged in to see schedule",Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }
    /* returns whether a user is currently logged in or not */
    private boolean userLoggedIn(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }
    private void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(Main.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
        //restart activity to refresh content and UI
        recreate();
    }

}
