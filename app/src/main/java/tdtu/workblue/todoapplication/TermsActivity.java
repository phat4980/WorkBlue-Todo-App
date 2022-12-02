package tdtu.workblue.todoapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TermsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView topNavigationViewTerms;
    private TextView tvName, tvEmail;
    private ImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        initUi();
        setSupportActionBar(toolbar); //kích hoạt drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        showUserInformation();
    }

    private void initUi() // ánh xạ
    {
        toolbar = findViewById(R.id.toolbar_terms);
        drawerLayout = findViewById(R.id.drawer_layout_terms);
        topNavigationViewTerms = findViewById(R.id.top_navigation_terms);
        topNavigationViewTerms.setNavigationItemSelectedListener(this);
        imgAvatar = topNavigationViewTerms.getHeaderView(0).findViewById(R.id.profile_image);
        tvName = topNavigationViewTerms.getHeaderView(0).findViewById(R.id.profile_name);
        tvEmail = topNavigationViewTerms.getHeaderView(0).findViewById(R.id.profile_email);
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
            Intent intentProfile = new Intent(TermsActivity.this, MainActivity.class);
            startActivity(intentProfile);
        } else if(id == R.id.settings) {
            Intent intentProfile = new Intent(TermsActivity.this, SettingsActivity.class);
            startActivity(intentProfile);
        }  else if(id == R.id.help_feedback) {
            Intent intentProfile = new Intent(TermsActivity.this, HelpFeedActivity.class);
            startActivity(intentProfile);
        }  else if(id == R.id.terms) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }  else if(id == R.id.contact) {
            Intent intentProfile = new Intent(TermsActivity.this, ContactActivity.class);
            startActivity(intentProfile);
        } else if(id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(TermsActivity.this, LoginActivity.class);
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