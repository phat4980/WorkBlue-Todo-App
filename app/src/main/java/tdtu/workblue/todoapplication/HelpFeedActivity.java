package tdtu.workblue.todoapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HelpFeedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView topNavigationViewHF;
    private TextView tvName, tvEmail;
    private ImageView imgAvatar;
    private Spinner spinnerHelpGuild;
    private Button openDialogFeedback, btnNoFeed, btnSendFeed;
    private EditText edtFeedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_feed);

        initUi();
        setSupportActionBar(toolbar); //kích hoạt drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        showUserInformation();
        showHelpGuild();
        openDialog();
    }

    private void initUi() // ánh xạ
    {
        toolbar = findViewById(R.id.toolbar_hf);
        drawerLayout = findViewById(R.id.drawer_layout_hf);
        topNavigationViewHF = findViewById(R.id.top_navigation_hf);
        topNavigationViewHF.setNavigationItemSelectedListener(this);
        imgAvatar = topNavigationViewHF.getHeaderView(0).findViewById(R.id.profile_image);
        tvName = topNavigationViewHF.getHeaderView(0).findViewById(R.id.profile_name);
        tvEmail = topNavigationViewHF.getHeaderView(0).findViewById(R.id.profile_email);
        spinnerHelpGuild = (Spinner) findViewById(R.id.spinner_help);
        openDialogFeedback = findViewById(R.id.press_feedBack);
        btnNoFeed = findViewById(R.id.no_feed);
        btnSendFeed = findViewById(R.id.send_feed);
    }

    private void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrI = user.getPhotoUrl();

        // Check xem có tên chưa
        if(name == null) {
            tvName.setVisibility(View.GONE);
        } else {
            tvName.setVisibility(View.VISIBLE);
            tvName.setText(name);
        }
        tvEmail.setText(email);
        Glide.with(this).load(photoUrI).error(R.drawable.ic_avatar_default).into(imgAvatar);
    }

    private void showHelpGuild() {
        ArrayList<String> arrHelpGuild = new ArrayList<>();
        arrHelpGuild.add("How to use this app");
        arrHelpGuild.add("How to login / sign up and create account");
        arrHelpGuild.add("How to edit profile");
        arrHelpGuild.add("How to add tasks");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrHelpGuild);
        spinnerHelpGuild.setAdapter(arrayAdapter);

        spinnerHelpGuild.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HelpFeedActivity.this, arrHelpGuild.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void openDialog() { // xử lý nút feedback
        openDialogFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opnClickOpenDialog(Gravity.CENTER);
            }
        });
    }

    private void opnClickOpenDialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_feedback);

        //layout
        Window window = dialog.getWindow();
        if(window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //vị trí
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(Gravity.CENTER == gravity);

        btnNoFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSendFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HelpFeedActivity.this, "Send feedback done", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }


    @Override // hiển thị menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.main) {
            Intent intentProfile = new Intent(HelpFeedActivity.this, MainActivity.class);
            startActivity(intentProfile);
        } else if(id == R.id.settings) {
            Intent intentProfile = new Intent(HelpFeedActivity.this, SettingsActivity.class);
            startActivity(intentProfile);
        }  else if(id == R.id.help_feedback) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }  else if(id == R.id.terms) {
            Intent intentProfile = new Intent(HelpFeedActivity.this, TermsActivity.class);
            startActivity(intentProfile);
        }  else if(id == R.id.contact) {
            Intent intentProfile = new Intent(HelpFeedActivity.this, ContactActivity.class);
            startActivity(intentProfile);
        } else if(id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HelpFeedActivity.this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        return true;
    }

    @Override // Xử lý khi ấn back drawer
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}