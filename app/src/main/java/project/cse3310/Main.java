package project.cse3310;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main extends AppCompatActivity{

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_login, menu);
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
                if (!userLoggedIn())
                    startActivity(new Intent(this, LoginAndReg.class));
                else
                    logoutUser();
                return true;
            case R.id.action_search:
                //TODO: implement search activity start here
                return true;
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
        startActivity(new Intent(this, Main.class));
    }

}
