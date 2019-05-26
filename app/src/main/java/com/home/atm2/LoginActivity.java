package com.home.atm2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.provider.MediaStore;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private static final int CAMERA_CODE = 1;
    EditText ed_Account,ed_Passwd;
    Button btn_Login,btn_Left;
    CheckBox cb_Account ;
    private final int RESULTCODE_LOGIN=-1;
    private boolean isChecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findviews();
        remember();

        if(ContextCompat
           .checkSelfPermission(this, Manifest.permission.CAMERA)==
                PackageManager.PERMISSION_GRANTED){
            //takephoto();
        }
        else{
            ActivityCompat.requestPermissions
                    (this,new String[]{Manifest.permission.CAMERA},CAMERA_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CAMERA_CODE)
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takephoto();
            }
    }

    private void takephoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);


    }

    public void remember(){
        isChecked = getSharedPreferences("atm",MODE_PRIVATE)
                .getBoolean("isChecked",false);
        cb_Account.setChecked(isChecked);

        if(isChecked == true) {
            ed_Account.setText(getSharedPreferences("atm", MODE_PRIVATE)
                    .getString("account", ""));
            ed_Passwd.setText(getSharedPreferences("atm", MODE_PRIVATE)
                    .getString("passwd", ""));
        }

        cb_Account.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getSharedPreferences("atm",MODE_PRIVATE).edit()
                        .putBoolean("isChecked",isChecked)
                        .commit();

            }
        });
    }



    public void login(View view){
        final String account=ed_Account.getText().toString();
        final String passwd=ed_Passwd.getText().toString();
        FirebaseDatabase.getInstance().getReference("users").child(account).child("password")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String pw =dataSnapshot.getValue(String.class);
                isChecked = getSharedPreferences("atm",MODE_PRIVATE)
                                        .getBoolean("isChecked",false);
                if(pw.equals(passwd)){
                    if(isChecked==true) {
                        getSharedPreferences("atm",MODE_PRIVATE).edit()
                                .putString("account",account)
                                .putString("passwd",passwd)
                                .commit();
                    }
                    setResult(RESULTCODE_LOGIN);
                    finish();
                }
                else{
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("錯誤")
                            .setMessage("帳號或密碼錯誤")
                            .setPositiveButton("OK",null)
                            .show();
        }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void left(View v){ finish(); }
    private void findviews(){
        ed_Account=findViewById(R.id.ed_account);
        ed_Passwd=findViewById(R.id.ed_passwd);
        btn_Login=findViewById(R.id.btn_login);
        btn_Left=findViewById(R.id.btn_left);
        cb_Account=findViewById(R.id.cb_account);
    }
}
