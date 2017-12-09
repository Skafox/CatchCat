package com.example.hp1.catchcat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private Button bt_register;
    private EditText tf_name;
    private EditText tf_email;
    private EditText tf_password;
    private EditText tf_phone;
    private ImageView profileThumbnail;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        bt_register = (Button) findViewById(R.id.bt_register);
        tf_name = (EditText) findViewById(R.id.tf_name);
        tf_email = (EditText) findViewById(R.id.tf_email);
        tf_password = (EditText) findViewById(R.id.tf_password);
        tf_phone = (EditText) findViewById(R.id.tf_phone);
        profileThumbnail = (ImageView) findViewById(R.id.imageViewProfile);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("catchcat-cab36");

        progressDialog = new ProgressDialog(this);

        bt_register.setOnClickListener(this);

    }

    public void saveInDatabase(){
        String name = tf_name.getText().toString().trim();
        String email = tf_email.getText().toString().trim();
        String password = tf_password.getText().toString().trim();
        String phone = tf_phone.getText().toString().trim();
        UserInformation userInformation = new UserInformation(name, email,  phone);

        FirebaseUser user = firebaseAuth.getCurrentUser();


        databaseReference.child(user.getUid()).setValue(userInformation);

        finish();
        Intent intentMenu = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intentMenu);
    }

    public void registerUser(){
        String name = tf_name.getText().toString().trim();
        String email = tf_email.getText().toString().trim();
        String password = tf_password.getText().toString().trim();
        String phone = tf_phone.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, this.getString(R.string.namevalidation), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, this.getString(R.string.emailvalidation), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, this.getString(R.string.passwordvalidation), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, this.getString(R.string.phonevalidation), Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage(this.getString(R.string.pg_registering));
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            saveInDatabase();
                        }
                        else{
                            Toast.makeText(SignInActivity.this,"@string/signinerror", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }



    @Override
    public void onClick(View view){
        if (view == bt_register){
            registerUser();
        }
        /*if(view == profileThumbnail){

        }*/
    }

}
