package tdtu.workblue.todoapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout emailTextInput,passwordTextInput;
    TextInputEditText emailEditText, passwordEditText;
    Button btnSingin, btnGGle, btnFace;
    TextView ToRegPage;

    public LoginActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initUi();

        ToRegPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigateToReg();
            }
        });

    }

    private void initUi()
    {
        emailTextInput = findViewById(R.id.emailSI_text_input);
        emailEditText = findViewById(R.id.emailSI_edit_text);
        btnSingin = findViewById(R.id.btnSingin);
        btnGGle = findViewById(R.id.btnGoogle);
        btnFace = findViewById(R.id.btnFacebook);
        ToRegPage = findViewById(R.id.tvToRegister);
    }

    private void NavigateToReg()
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}