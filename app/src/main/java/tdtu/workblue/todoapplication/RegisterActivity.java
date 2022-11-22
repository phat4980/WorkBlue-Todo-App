package tdtu.workblue.todoapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout nameTextInputSU, emailTextInputSU, passwordTextInputSU;
    TextInputEditText nameEditTextSU, emailEditTextSU, passwordEditTextSU;
    Button btnSingUp;
    LinearLayout ToLogPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initUi();
        startSingUp();
        changeToLoginPage();
    }

    private void initUi() // ánh xạ
    {
        nameTextInputSU = findViewById(R.id.nameSU_text_input);
        nameEditTextSU = findViewById(R.id.nameSU_edit_text);
        emailTextInputSU = findViewById(R.id.emailSU_text_input);
        emailEditTextSU = findViewById(R.id.emailSU_edit_text);
        passwordTextInputSU = findViewById(R.id.passwordSU_text_input);
        passwordEditTextSU = findViewById(R.id.passwordSU_edit_text);
        btnSingUp = findViewById(R.id.btnSU);
        ToLogPage = findViewById(R.id.tvToLogin);
    }

    private void createAccount()
    {
        String strEmail = emailEditTextSU.getText().toString().trim();
        String strPassword = passwordEditTextSU.getText().toString().trim();

    }

    private void startSingUp() //Đăng ký
    {
        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSingUp();
            }
        });
    }

    private void onClickSingUp() //Bắt sự kiện đăng ký
    {
        String strEmail = emailEditTextSU.getText().toString().trim();
        String strPassword = passwordEditTextSU.getText().toString().trim();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            NavigateToLog();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void displayErrorEmail() //hiển thị lỗi lúc nhập
    {
        emailEditTextSU.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (validateRegEmail(emailEditTextSU.getText().toString().trim())) {
                    emailTextInputSU.setError(null); //Clear the error
                }
                return false;
            }
        });
    }

    private void displayErrorPassword() //hiển thị lỗi lúc nhập
    {
        passwordEditTextSU.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (validateRegPassword(passwordEditTextSU.getText())) {
                    passwordTextInputSU.setError(null); //Clear the error
                }
                return false;
            }
        });
    }

    private boolean validateRegEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateRegPassword(Editable password) {
        return password != null && password.length() >= 8;
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
        finishAffinity();
    }
}