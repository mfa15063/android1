package com.ebookfrenzy.foodon;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class Delivery_registeration extends AppCompatActivity {

//    String[] Punjab = {"Bahawalpur", "Lahore", "Multan"};
//    String[] Sindh = {"Karachi", "dhanot", "Hyderabad"};
//    String[] Bahawalpur = {"Satelite Town", "Model Town A", "Model Town B", "Baghdad Station", "Milad Chowk", "Fawara Chowk", "Ahmadpuri gate", "Fred gate",
//            "DC Chowk", "Kalma Chowk", "Kali Puli", "CMH Chowk", "Gulberg Coloni", "Non House", "Gilgust Road", "Islami Coloni", "Fouji Chowk", "DHA",
//            "Khayabany Ali", "12 BC", "42 chak", "yazman", "Chanan peer", "Cholistan"};
//    String[] Lahore = {"Data Darbar", "Badshahi Masjid", "Minar e pakistan", "Shahi kila", "Model Town"};
//    String[] Multan = {"Chowk kumarah", "Chongi no 9", "Z Town", "Shah Shamas"};
//    String[] Ahemdabad={"Mani Nagar","Thaltej","Prahlad Nagar","Gandhinagar"};
//    String[] Surat={"Agnovad","Akoti","Amroli","Athwalines"};
//    String[] Rajkot={"Kalawad Road","Astron chowk","Kotecha chowk","Trikon bag"};


    TextInputLayout first_name, last_name, _email, _phone, _password, confirm_password, _state, _city, _town, local_address, post_code;
    CountryCodePicker countryCodePicker;
    String firstName, lastName, email, phone, password, confirmPassword, state, city, town, localAddress, postCode, role = "Delivery";
    Button signup;
    TextView signin;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        AnimationDrawable animationDrawable = ReusableCodeForAll.anim(getResources());
        ConstraintLayout bgimage = findViewById(R.id.back3);
        bgimage.setBackgroundDrawable(animationDrawable);
        animationDrawable.start();

        try {
            first_name = (TextInputLayout) findViewById(R.id.Fname);
            last_name = (TextInputLayout) findViewById(R.id.Lname);
            _email = (TextInputLayout) findViewById(R.id.Emailid);
            countryCodePicker = (CountryCodePicker) findViewById(R.id.CountryCode);
            _phone = (TextInputLayout) findViewById(R.id.Mobilenumber);
            _password = (TextInputLayout) findViewById(R.id.Password);
            confirm_password = (TextInputLayout) findViewById(R.id.confirmpass);
            _state = (TextInputLayout) findViewById(R.id.state);
            _city = (TextInputLayout) findViewById(R.id.city);
            _town = (TextInputLayout) findViewById(R.id.town);
            local_address = (TextInputLayout) findViewById(R.id.localAddress);
            post_code = (TextInputLayout) findViewById(R.id.post_code);
            signin = findViewById(R.id.textView3);
            signup = (Button) findViewById(R.id.Signup);

            databaseReference = firebaseDatabase.getInstance().getReference("Delivery");
            FAuth = FirebaseAuth.getInstance();

            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(Delivery_registeration.this, Delivery_Login.class));
                    finish();


                }
            });

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firstName = first_name.getEditText().getText().toString().trim();
                    lastName = last_name.getEditText().getText().toString().trim();
                    email = _email.getEditText().getText().toString().trim();
                    phone = countryCodePicker.getSelectedCountryCodeWithPlus()+_phone.getEditText().getText().toString().trim();
                    password = _password.getEditText().getText().toString().trim();
                    confirmPassword = confirm_password.getEditText().getText().toString().trim();
                    state = _state.getEditText().getText().toString().trim();
                    city = _city.getEditText().getText().toString().trim();
                    town = _town.getEditText().getText().toString().trim();
                    localAddress = local_address.getEditText().getText().toString().trim();
                    postCode = post_code.getEditText().getText().toString().trim();

                    if (isValid()) {
                        final ProgressDialog mDialog = new ProgressDialog(Delivery_registeration.this);
                        mDialog.setCancelable(false);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.setMessage("Registering please wait...");
                        mDialog.show();

                        FAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    databaseReference = FirebaseDatabase.getInstance().getReference("User").child(useridd);
                                    final HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("Role", role);
                                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            HashMap<String, String> hashMappp = new HashMap<>();
                                            hashMappp.put("FirstName", firstName);
                                            hashMappp.put("LastName", lastName);
                                            hashMappp.put("EmailID", email);
                                            hashMappp.put("Phone", phone);
                                            hashMappp.put("Password", password);
                                            hashMappp.put("State", state);
                                            hashMappp.put("City", city);
                                            hashMappp.put("Town", town);
                                            hashMappp.put("LocalAddress", localAddress);
                                            hashMappp.put("PostCode", postCode);
                                            firebaseDatabase.getInstance().getReference("Delivery")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(hashMappp).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        mDialog.dismiss();
                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Delivery_registeration.this);
                                                                        builder.setMessage("Registered Successfully,Please Verify your Email");
                                                                        builder.setCancelable(false);
                                                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                Intent b = new Intent(Delivery_registeration.this, Delivery_Login.class);
                                                                                startActivity(b);
                                                                            }
                                                                        });
                                                                        AlertDialog alert = builder.create();
                                                                        alert.show();

                                                                    } else {
                                                                        mDialog.dismiss();
                                                                        ReusableCodeForAll.ShowAlert(Delivery_registeration.this,"Error",task.getException().getMessage());

                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });
                                        }
                                    });

                                } else {
                                    mDialog.dismiss();
                                    ReusableCodeForAll.ShowAlert(Delivery_registeration.this,"Error",task.getException().getMessage());
                                }
                            }
                        });
                    }


                }
            });
        } catch (Exception e) {
            mDialog.dismiss();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }




    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public boolean isValid() {

        boolean isValidFirstName = false, isValidLastName = false, isValidEmail = false, isValidPhone = false, isValidPassword = false, isValidConfirmPassword = false, isValidState = false, isValidCity = false, isValidTown = false, isValidLocalAddress = false, isValidPostCode = false, isValid;
        if (TextUtils.isEmpty(firstName)) {
            first_name.setError("FirstName is required");
            first_name.setFocusable(true);
        } else {
            isValidFirstName = true;
            first_name.setError("");
        }
        if (TextUtils.isEmpty(lastName)) {
            last_name.setError("LastName is required");
            last_name.setFocusable(true);
        } else {
            isValidLastName = true;
            last_name.setError("");
        }
        if (TextUtils.isEmpty(email)) {
            _email.setError("Email is required");
            _email.setFocusable(true);
        } else {
            if (email.matches(emailpattern)) {
                isValidEmail = true;
                _email.setError("");
            } else {
                _email.setError("Enter a valid Email Address");
                _email.setFocusable(true);
            }
        }
        if (TextUtils.isEmpty(_phone.getEditText().getText().toString())) {
            _phone.setError("Phone is required");
            _phone.setFocusable(true);
        } else {
            isValidPhone = true;
            _phone.setError("");
        }
        if (TextUtils.isEmpty(password)) {
            _password.setError("Password is required");
            _password.setFocusable(true);
        } else {
            isValidPassword = true;
            _password.setError("");
        }
        if (!password.equals(confirmPassword)) {
            confirm_password.setError("Password not matched");
            confirm_password.setFocusable(true);
        } else {
            isValidConfirmPassword = true;
            confirm_password.setError("");
        }
        if (TextUtils.isEmpty(state)) {
            _state.setError("State is required");
            _state.setFocusable(true);
        } else {
            isValidState = true;
            _state.setError("");
        }
        if (TextUtils.isEmpty(city)) {
            _city.setError("City is required");
            _city.setFocusable(true);
        } else {
            isValidCity = true;
            _city.setError("");
        }
        if (TextUtils.isEmpty(town)) {
            _town.setError("Town is required");
            _town.setFocusable(true);
        } else {
            isValidTown = true;
            _town.setError("");
        }
        if (TextUtils.isEmpty(localAddress)) {
            local_address.setError("LocalAddress is required");
            local_address.setFocusable(true);
        } else {
            isValidLocalAddress = true;
            local_address.setError("");
        }
        if (TextUtils.isEmpty(postCode)) {
            post_code.setError("Post Code is required");
            post_code.setFocusable(true);
        } else {
            isValidPostCode = true;
            post_code.setError("");
        }

        isValid = (isValidFirstName && isValidLastName && isValidEmail && isValidPhone && isValidPassword && isValidConfirmPassword && isValidState && isValidCity && isValidTown && isValidLocalAddress && isValidPostCode) ? true : false;
        return isValid;
    }
}




