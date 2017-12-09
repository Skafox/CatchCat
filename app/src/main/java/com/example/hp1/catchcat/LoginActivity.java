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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_signin;
    private Button bt_login;
    private EditText tf_email;
    private EditText tf_password;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        //VALIDA SE JÁ ESTÁ LOGADO
        /*if (firebaseAuth.getCurrentUser() != null){
            finish();
            Intent intentMenu = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(intentMenu);
        }*/

        bt_signin = (Button) findViewById(R.id.bt_signin);
        bt_login = (Button) findViewById(R.id.bt_login);
        tf_email = (EditText) findViewById(R.id.tf_email);
        tf_password = (EditText) findViewById(R.id.tf_password);

        bt_signin.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);


    }

    public void login(){
        String email = tf_email.getText().toString().trim();
        String password = tf_password.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, this.getString(R.string.emailloginvalidation), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, this.getString(R.string.passwordloginvalidation), Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage(this.getString(R.string.pg_loggingin));
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            //logadocomsucesso - activity menu
                            finish();
                            Intent intentMenu = new Intent(getApplicationContext(), MenuActivity.class);
                            startActivity(intentMenu);
                        }
                        else{
                            //naofoipossivellogar
                        }
                    }
                });




    }


    @Override
    public void onClick(View view) {
        if (view == bt_login){
            login();
        }
        if (view == bt_signin){
            finish();
            startActivity(new Intent (this, SignInActivity.class));
        }
    }
}
