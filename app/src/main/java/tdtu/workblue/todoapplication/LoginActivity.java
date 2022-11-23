package tdtu.workblue.todoapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout emailTextInputSI,passwordTextInputSI;
    TextInputEditText emailEditTextSI, passwordEditTextSI;
    Button btnSignin, btnGGle, btnFace;
    LinearLayout ToRegPage;

    public LoginActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initUi();
        startSingIn();
        changeToRegPage();


    }

    private void initUi() //ánh xạ
    {
        emailTextInputSI = findViewById(R.id.emailSI_text_input);
        emailEditTextSI = findViewById(R.id.emailSI_edit_text);
        passwordTextInputSI = findViewById(R.id.passwordSI_text_input);
        passwordEditTextSI = findViewById(R.id.passwordSI_edit_text);
        btnSignin = findViewById(R.id.btnSignin);
        btnGGle = findViewById(R.id.btnGoogle);
        btnFace = findViewById(R.id.btnFacebook);
        ToRegPage = findViewById(R.id.tvToRegister);
    }

    private void startSingIn()
    {
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSingIn();
            }
        });
    }

    private void onClickSingIn() {
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        String email = emailEditTextSI.getText().toString().trim();
        String password = passwordEditTextSI.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            emailTextInputSI.setError("Email is required");
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailTextInputSI.setError("Email is invalid");
        }
        if(TextUtils.isEmpty(password))
        {
            passwordTextInputSI.setError("Password is required");
        }
        if(password.length() <=8)
        {
            passwordTextInputSI.setError("Enter at least 8 characters");
        } else {
            emailEditTextSI.setError(null);
            passwordTextInputSI.setError(null);

            Auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(LoginActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                                NavigateToReg();
                                finishAffinity();
                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void changeToRegPage() //Sang trang đăng ký
    {
        ToRegPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void NavigateToReg() //điều hướng
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}