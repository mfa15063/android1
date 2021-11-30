package com.ebookfrenzy.foodon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ChefLogin extends AppCompatActivity {


    TextInputLayout email, pass;
    Button Signin;
    TextView Forgotpassword, signup;
    FirebaseAuth FAuth;
    String em;
    String pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheflogin);

        AnimationDrawable animationDrawable = ReusableCodeForAll.anim(getResources());
        ConstraintLayout bgimage = findViewById(R.id.back3);
        bgimage.setBackgroundDrawable(animationDrawable);
        animationDrawable.start();

        try {
            email = (TextInputLayout) findViewById(R.id.Lemail);
            pass = (TextInputLayout) findViewById(R.id.Lpassword);
            Signin = (Button) findViewById(R.id.button4);
            signup = (TextView) findViewById(R.id.textView3);
            Forgotpassword = (TextView) findViewById(R.id.forgotpass);
            FAuth = FirebaseAuth.getInstance();
            Signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    em = email.getEditText().getText().toString().trim();
                    pwd = pass.getEditText().getText().toString().trim();
                    if (isValid()) {

                        final ProgressDialog mDialog = new ProgressDialog(ChefLogin.this);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.setCancelable(false);
                        mDialog.setMessage("SignIn please wait......");
                        mDialog.show();
                        FAuth.signInWithEmailAndPassword(em, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    mDialog.dismiss();
                                    if (FAuth.getCurrentUser().isEmailVerified()) {
                                        Toast.makeText(ChefLogin.this, "Congratulations! You Have Successfully Login", Toast.LENGTH_SHORT).show();
                                        Intent z = new Intent(ChefLogin.this, ChefFoodPanel_BottomNavigation.class);
                                        startActivity(z);
                                        finish();


                                    } else {
                                        ReusableCodeForAll.ShowAlert(ChefLogin.this, "Verification Failed", "Please Verify your Email");
                                    }

                                } else {

                                    mDialog.dismiss();
                                    ReusableCodeForAll.ShowAlert(ChefLogin.this, "Error", task.getException().getMessage());
                                }
                            }
                        });

                    }
                }
            });

            signup.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           startActivity(new Intent(ChefLogin.this, ChefRegisteration.class));
                                           finish();


                }
            });

            Forgotpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent a = new Intent(ChefLogin.this, ChefForgotPassword.class);
                    startActivity(a);
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public boolean isValid() {

        boolean isvalidemail = false, isvalidpassword = false, isvalid = false;
        if (TextUtils.isEmpty(em)) {
            email.setError("Email is required");
            email.setFocusable(true);
        } else {
            if (em.matches(emailpattern)) {
                isvalidemail = true;
            } else {
                email.setError("Enter a valid Email Address");
                email.setFocusable(true);
            }

        }
        if (TextUtils.isEmpty(pwd)) {
            pass.setError("Password is required");
            pass.setFocusable(true);
        } else {
            isvalidpassword = true;
        }
        isvalid = (isvalidemail && isvalidpassword) ? true : false;
        return isvalid;
    }
}

