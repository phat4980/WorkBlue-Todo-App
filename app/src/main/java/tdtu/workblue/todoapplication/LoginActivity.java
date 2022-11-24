package tdtu.workblue.todoapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.facebook.FacebookSdk;



public class LoginActivity extends AppCompatActivity {
    TextInputLayout emailTextInputSI,passwordTextInputSI;
    TextInputEditText emailEditTextSI, passwordEditTextSI;
    Button btnSignin, btnGGle, btnFace;
    LinearLayout ToRegPage;
    private ProgressBar progressBar;
    private CallbackManager fbCallback;
    private  FirebaseAuth Auth;
    private GoogleSignIn client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(LoginActivity.this); //sdkInitialize Facebook

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initUi();
        changeInProcess(false);
        startSingIn();
        facebookLogin();
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
        progressBar = findViewById(R.id.progressBarSI);
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
            changeInProcess(true);
            emailEditTextSI.setError(null);
            passwordTextInputSI.setError(null);

            Auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            changeInProcess(false);
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

    //Google login
    private void googleLogin()
    {

    }

    //Facebook login
    private void facebookLogin()
    {
        // Initialize Facebook Login button
        Auth = FirebaseAuth.getInstance();
        fbCallback = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.btnFacebook);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(fbCallback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = Auth.getCurrentUser();
                            updateUI(user);
                            NavigateToReg();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fbCallback.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI(FirebaseUser user) {
        if(user != null)
        {
            NavigateToReg();
        } else {
            Toast.makeText(this, "Sign in to continue", Toast.LENGTH_SHORT).show();
        }
    }

    private void changeInProcess(boolean inProgress) // thanh progess và button
    {
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btnSignin.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btnSignin.setVisibility(View.VISIBLE);
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