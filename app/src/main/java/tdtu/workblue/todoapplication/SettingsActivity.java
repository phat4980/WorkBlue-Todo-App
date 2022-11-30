package tdtu.workblue.todoapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int MY_REQUEST_CODE = 10;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView topNavigationViewSettings;
    private EditText edtName, edtEmail;
    private Button btnUpdateProfile;
    private ImageView edtImgAvatar;
    private Uri mUri;
    private ImageView imgAvatar;
    private TextView tvName, tvEmail;
    private ProgressBar progressBar;

    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if(intent == null) {
                    return;
                }
            }
            Uri uri = getIntent().getData();
            setUri(uri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri );
                this.setBitmapImageView(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void setBitmapImageView(Bitmap bitmapImageView) {
            edtImgAvatar.setImageBitmap(bitmapImageView);
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initUi();
        progressBar.setVisibility(View.GONE);
        setSupportActionBar(toolbar); //kích hoạt drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setUserInformation();
        settingsProfile();
        showUserInformation();

    }


    private void initUi() // ánh xạ
    {
        toolbar = findViewById(R.id.toolbar_settings);
        progressBar = findViewById(R.id.progressBarST);
        drawerLayout = findViewById(R.id.drawer_layout_settings);
        topNavigationViewSettings = findViewById(R.id.top_navigation_settings);
        topNavigationViewSettings.setNavigationItemSelectedListener(this);
        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        btnUpdateProfile = findViewById(R.id.btnUpdate_profile);
        edtImgAvatar = findViewById(R.id.edt_img_avatar);
        imgAvatar = topNavigationViewSettings.getHeaderView(0).findViewById(R.id.profile_image);
        tvName = topNavigationViewSettings.getHeaderView(0).findViewById(R.id.profile_name);
        tvEmail = topNavigationViewSettings.getHeaderView(0).findViewById(R.id.profile_email);

    }

    //get user in4
    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            return;
        }
        edtName.setText(user.getDisplayName());
        edtEmail.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.ic_avatar_default).into(edtImgAvatar);
    }

    //bắt sự kiện listener
    private void settingsProfile() {
        edtImgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
            }
        });
    }

    //xin quyền
    private void onClickRequestPermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if(this.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,0,0) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            this.requestPermissions(permissions, MY_REQUEST_CODE);
        }
    }

    @Override //cho phép user sử dụng hay từ chối
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Please give permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() { // mở thư viện ảnh
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    public void setUri(Uri mUri) {
        this.mUri = mUri;
    }

    private void onClickUpdateProfile() { //ấn để update
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        String strName = edtName.getText().toString().trim();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strName)
                .setPhotoUri(mUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingsActivity.this, "Update profile successful", Toast.LENGTH_SHORT).show();
                            showUserInformation();
                        } else {
                            Toast.makeText(SettingsActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    @Override // hiển thị menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.main) {
            Intent intentProfile = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intentProfile);
        } else if(id == R.id.settings) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }  else if(id == R.id.help_feedback) {
            Intent intentProfile = new Intent(SettingsActivity.this, HelpFeedActivity.class);
            startActivity(intentProfile);
        }  else if(id == R.id.terms) {
            Intent intentProfile = new Intent(SettingsActivity.this, TermsActivity.class);
            startActivity(intentProfile);
        }  else if(id == R.id.contact) {
            Intent intentProfile = new Intent(SettingsActivity.this, ContactActivity.class);
            startActivity(intentProfile);
        } else if(id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
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