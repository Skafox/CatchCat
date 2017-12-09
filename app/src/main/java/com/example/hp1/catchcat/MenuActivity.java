package com.example.hp1.catchcat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {

    private Button bt_logout;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        firebaseAuth = FirebaseAuth.getInstance();

        //controla login
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intentLogin);
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        bt_logout = (Button) findViewById(R.id.bt_logout);


    }

    public void logOut (View view){
        firebaseAuth.signOut();
        finish();
        Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intentLogin);
    }

    public void openNewCat (View view){
        Intent intentNewCat = new Intent(getApplicationContext(), NewCatActivity.class);
        startActivity(intentNewCat);
    }


    public void openAdoptCat (View view){
        Intent intentAdoptCat = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intentAdoptCat);
    }

    public void openProfile(View view){
        Intent intentProfile = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intentProfile);
    }


}
