package tdtu.workblue.todoapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout nameTextInputSU, emailTextInputSU, passwordTextInputSU;
    TextInputEditText nameEditTextSU, emailEditTextSU, passwordEditTextSU;
    Button btnSignUp;
    LinearLayout ToLogPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initUi();
        createUser();
        changeToLoginPage();

    }

    private void createUser() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignUp();
            }
        });
    }

    private void onClickSignUp() {
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        String name = nameEditTextSU.getText().toString().trim();
        String email = emailEditTextSU.getText().toString().trim();
        String password = passwordEditTextSU.getText().toString().trim();

        if(TextUtils.isEmpty(name))
        {
            nameTextInputSU.setError("Name is required");
        }
        if(TextUtils.isEmpty(email))
        {
            emailTextInputSU.setError("Email is required");
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailTextInputSU.setError("Email is invalid");
        }
        if(TextUtils.isEmpty(password))
        {
            passwordTextInputSU.setError("Password is required");
        }
        if(password.length() <=8)
        {
            passwordTextInputSU.setError("Enter at least 8 characters");
        } else {
            nameTextInputSU.setError(null);
            emailEditTextSU.setError(null);
            passwordTextInputSU.setError(null);
            Auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                NavigateToLog();
                                finishAffinity();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

                            /*String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                            switch (errorCode) {

                                case "ERROR_INVALID_CUSTOM_TOKEN":
                                    Toast.makeText(RegisterActivity.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                    Toast.makeText(RegisterActivity.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_INVALID_CREDENTIAL":
                                    Toast.makeText(RegisterActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_INVALID_EMAIL":
                                    Toast.makeText(RegisterActivity.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                    emailTextInputSU.setError("The email address is badly formatted.");
                                    emailEditTextSU.requestFocus();
                                    break;

                                case "ERROR_WRONG_PASSWORD":
                                    Toast.makeText(RegisterActivity.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                    passwordTextInputSU.setError("password is incorrect ");
                                    passwordEditTextSU.requestFocus();
                                    passwordEditTextSU.setText("");
                                    break;

                                case "ERROR_USER_MISMATCH":
                                    Toast.makeText(RegisterActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_REQUIRES_RECENT_LOGIN":
                                    Toast.makeText(RegisterActivity.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                    Toast.makeText(RegisterActivity.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    Toast.makeText(RegisterActivity.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                    passwordTextInputSU.setError("The email address is already in use by another account.");
                                    passwordEditTextSU.requestFocus();
                                    break;

                                case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                    Toast.makeText(RegisterActivity.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_DISABLED":
                                    Toast.makeText(RegisterActivity.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_TOKEN_EXPIRED":
                                    Toast.makeText(RegisterActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_NOT_FOUND":
                                    Toast.makeText(RegisterActivity.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_INVALID_USER_TOKEN":
                                    Toast.makeText(RegisterActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_OPERATION_NOT_ALLOWED":
                                    Toast.makeText(RegisterActivity.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_WEAK_PASSWORD":
                                    Toast.makeText(RegisterActivity.this, "The given password is invalid.", Toast.LENGTH_LONG).show();
                                    passwordTextInputSU.setError("The password is invalid it must 6 characters at least");
                                    passwordEditTextSU.requestFocus();
                                    break;

                            }
                        }*/
    }

    private void initUi() // ánh xạ
    {
        nameTextInputSU =  findViewById(R.id.nameSU_text_input);
        nameEditTextSU =  findViewById(R.id.nameSU_edit_text);
        emailTextInputSU =  findViewById(R.id.emailSU_text_input);
        emailEditTextSU =  findViewById(R.id.emailSU_edit_text);
        passwordTextInputSU =  findViewById(R.id.passwordSU_text_input);
        passwordEditTextSU = findViewById(R.id.passwordSU_edit_text);
        btnSignUp =  findViewById(R.id.btnSU);
        ToLogPage =  findViewById(R.id.tvToLogin);

    }

    private void changeToLoginPage() //Sang trang đăng nhập
    {
        ToLogPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigateToLog();
            }
        });
    }

    private void NavigateToLog() //điều hướng
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}