package com.example.suryasolanki.firebasesampleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private final static String TAG=MainActivity.class.getSimpleName();
    private TextView txtView;
    private EditText editFirstName,editLastName,editAddress;
    private Button btnSave;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        txtView=(TextView) findViewById(R.id.txt_user);
        editFirstName=(EditText) findViewById(R.id.firstName);
        editLastName=(EditText)findViewById(R.id.lastName);
        editAddress=(EditText) findViewById(R.id.Address);
        btnSave=(Button)findViewById(R.id.btn_save);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("users");

        firebaseDatabase.getReference("app_title").setValue("Sample  Firebase");

        firebaseDatabase.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG,"App title uploaded");

                String appTitle=dataSnapshot.getValue(String.class);

                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG,"Failed to change title",databaseError.toException());
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FirsName=editFirstName.getText().toString();
                String LastName=editLastName.getText().toString();
                String Address=editAddress.getText().toString();

                if(TextUtils.isEmpty(userId)){
                    CreateUser(FirsName,LastName,Address);
                }
                else{
                    UpdateUser(FirsName,LastName,Address);
                }
            }
        });

        toggleButton();
    }

    public void toggleButton(){
        if(TextUtils.isEmpty(userId)){
            btnSave.setText("Save");
        }
        else {
            btnSave.setText("Update");
        }
    }

    private void CreateUser(String FirstName,String LastName,String Adddress){

        if(TextUtils.isEmpty(userId)){
            userId=databaseReference.push().getKey();
        }

        Users user=new Users(FirstName,LastName,Adddress);

        databaseReference.child(userId).setValue(user);

        addUserChangeListner();

    }

    private void addUserChangeListner(){

        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users user=dataSnapshot.getValue(Users.class);

                if(user==null){
                    Log.e(TAG,"User is NULL");
                    return;
                }

                Log.e(TAG,"User Data Changed"+user.FirstName +" ,"+user.LastName+ ", "+user.Address);

                txtView.setText(user.FirstName +" ,"+user.LastName+ ", "+user.Address);

                editFirstName.setText("");
                editLastName.setText("");
                editAddress.setText("");

                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read user", databaseError.toException());
            }
        });
    }

    private void UpdateUser(String FirstName,String LastName,String Address){

        if(TextUtils.isEmpty(FirstName))
            databaseReference.child("FirstName").setValue(FirstName);
        if(TextUtils.isEmpty(LastName))
            databaseReference.child("LastName").setValue(LastName);
        if(TextUtils.isEmpty(Address))
            databaseReference.child("LastName").setValue(Address);
    }
}
