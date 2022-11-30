package tdtu.workblue.todoapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import tdtu.workblue.todoapplication.adapter.MyViewPagerAdapter;
import tdtu.workblue.todoapplication.fragment.TasksFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private NavigationView topNavigationView;
    private ViewPager2 viewPagerContent;
    private ImageView imgAvatar;
    private TextView tvName, tvEmail;

    //Bottom navigation
    private static final int FRAGMENT_TASK = 0;
    private static final int FRAGMENT_CALENDAR = 1;

    private int CurrentFragmentB = FRAGMENT_TASK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initUi();
        setSupportActionBar(toolbar); //kích hoạt drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                                    toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        bottomNavigationView.getMenu().getItem(1).setEnabled(false); //xóa placeholder trong menu bottom

        MyViewPagerAdapter adapterViewPager = new MyViewPagerAdapter(this); //khởi tạo viewpager adapter
        viewPagerContent.setAdapter(adapterViewPager);
        
        bottomNavigationChange();
        showUserInformation();
    }

    private void initUi() // ánh xạ
    {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        topNavigationView = (NavigationView) findViewById(R.id.top_navigation);
        topNavigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPagerContent = findViewById(R.id.view_pager);
        imgAvatar = topNavigationView.getHeaderView(0).findViewById(R.id.profile_image);
        tvName = topNavigationView.getHeaderView(0).findViewById(R.id.profile_name);
        tvEmail = topNavigationView.getHeaderView(0).findViewById(R.id.profile_email);
    }

    @Override // hiển thị menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void bottomNavigationChange() //Navigate bằng bottom
    {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.tasks) {
                    if(CurrentFragmentB != FRAGMENT_TASK) {
                        viewPagerContent.setCurrentItem(0);
                        CurrentFragmentB = FRAGMENT_TASK;
                    }
                } else if(id == R.id.calendar) {
                    if(CurrentFragmentB != FRAGMENT_CALENDAR) {
                        viewPagerContent.setCurrentItem(2);
                        CurrentFragmentB = FRAGMENT_CALENDAR;
                    }
                }
                return true;
            }
        });
        viewPagerContent.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        CurrentFragmentB = FRAGMENT_TASK;
                        bottomNavigationView.getMenu().findItem(R.id.tasks).setChecked(true); // check trạng thái đã được chọn chưa
                        break;
                    case 1:
                        CurrentFragmentB = FRAGMENT_CALENDAR;
                        bottomNavigationView.getMenu().findItem(R.id.calendar).setChecked(true);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { // điều hướng drawer
        int id = item.getItemId();
        if(id == R.id.main) {
            drawerLayout.closeDrawer(GravityCompat.START);
            } else if(id == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }  else if(id == R.id.help_feedback) {
            Intent intent = new Intent(MainActivity.this, HelpFeedActivity.class);
            startActivity(intent);
        }  else if(id == R.id.terms) {
            Intent intent = new Intent(MainActivity.this, TermsActivity.class);
            startActivity(intent);
        }  else if(id == R.id.contact) {
            Intent intent = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(intent);
        } else if(id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        return true;
    }

    //get user profile
    private void showUserInformation()
    {
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