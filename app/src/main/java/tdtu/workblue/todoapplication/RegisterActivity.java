package tdtu.workblue.todoapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    private TextView ToLogPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ToLogPage = findViewById(R.id.tvToLogin);

        ToLogPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigateToLog();
            }
        });


    }

    private void NavigateToLog()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}