package tdtu.workblue.todoapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class HelpFeedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView topNavigationView;

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

        topNavigationView.setNavigationItemSelectedListener(this);
    }

    private void initUi() // ánh xạ
    {
        toolbar = findViewById(R.id.toolbar_hf);
        drawerLayout = findViewById(R.id.drawer_layout_hf);
        topNavigationView = findViewById(R.id.top_navigation_hf);
    }

    @Override // hiển thị menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.profile) {
            Intent intentProfile = new Intent(HelpFeedActivity.this, ProfileActivity.class);
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