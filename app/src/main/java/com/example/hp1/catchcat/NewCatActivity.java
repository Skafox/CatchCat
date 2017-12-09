package com.example.hp1.catchcat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class NewCatActivity extends AppCompatActivity implements View.OnClickListener{


    private Button bt_save;
    private ImageView cameraThumbnail;
    private ImageView catPicture;
    private EditText description;
    private static final int requisicao_camera = 1;
    public Bitmap catBitmap;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private DatabaseReference databaseReference;

    private DatabaseReference databaseReferenceChild;

    Map<String, Bitmap> cats = new HashMap<String, Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cat);

        catPicture = (ImageView) findViewById(R.id.NewCatThumbnail);
        cameraThumbnail = (ImageView) findViewById(R.id.cameraImageView);
        description = (EditText) findViewById(R.id.NewCatDescription);
        bt_save = (Button) findViewById(R.id.bt_save);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("catchcat-cab36");

        databaseReferenceChild = databaseReference.child("cats");

        progressDialog = new ProgressDialog(this);

        bt_save.setOnClickListener(this);
        cameraThumbnail.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == cameraThumbnail){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.CAMERA)){
                    Toast.makeText(this, "@string/needcamera", Toast.LENGTH_SHORT).show();
                }
                ActivityCompat.requestPermissions(this, new String [] {
                        android.Manifest.permission.CAMERA}, requisicao_camera);
            }
            else{
                dispatchTakePictureIntent();
            }
        }
        if (view == bt_save){
            saveCat();
        }

    }

    public void dispatchTakePictureIntent(){
        Intent takePictureIntent = new
                Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, requisicao_camera);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requisicao_camera && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            catPicture.setImageBitmap(imageBitmap);
            setCatPicture(imageBitmap);
        }
    }

    public void setCatPicture(Bitmap catBitmap){
        this.catBitmap = catBitmap;
    }

    public void saveCat(){
        String catDescription = description.getText().toString().trim();
        Bitmap picture = catBitmap;

        if (TextUtils.isEmpty(catDescription)){
            Toast.makeText(this, this.getString(R.string.descriptionvalidation), Toast.LENGTH_SHORT).show();
            return;
        }

        Cat cat = new Cat(catDescription, picture);

        progressDialog.setMessage(this.getString(R.string.pg_savecat));
        progressDialog.show();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        cats.put(catDescription, catBitmap);

        finish();
        Intent intentMenu = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intentMenu);
    }



}
