package navigationDrawer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.connectivityReceiver.MyApplication;
import com.example.shoppingapp.login.LoginActivity;
import com.example.shoppingapp.login.ProfileActivity;
import com.example.shoppingapp.sessionManagement.SessionManagement;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Objects;


import static com.example.shoppingapp.apiUrl.ApiClient.BASE_URL;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private DrawerLayout fullLayout;
    int selectedNavItemId;
    RelativeLayout parentLayout;
    public Toolbar toolbar;
    LinearLayout activityContainer;
    HashMap<String,String> hashMap;
    SessionManagement management;
//    ProgressDialog pDialog;

    TextView navUsername, welcomeText;
    ImageView imageView;
    public static final String TAG = NavigationActivity.class.getSimpleName();
    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        parentLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_drawer,null);

        fullLayout = parentLayout.findViewById(R.id.drawer_layout);

        activityContainer = parentLayout.findViewById(R.id.activity_content);

        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(parentLayout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        if(!useToolbar())
        {
            toolbar.setVisibility(View.GONE);
        }

        setUpNavView();
        initialize();

        /*pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        }else {
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        }

        navUsername.setText("Sign In");
        Log.d(TAG, "navUsername: "+navUsername);
        navUsername.setVisibility(View.GONE);

        navUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Menu menu = navigationView.getMenu();
        MenuItem Home = menu.findItem(R.id.nav_Home);
        MenuItem ShopCategory = menu.findItem(R.id.nav_shop_by_category);
        MenuItem Deal = menu.findItem(R.id.nav_deal);

        navigationView.setNavigationItemSelectedListener(this);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
        if(management.isLoggedIn()){


            welcomeText.setVisibility(View.VISIBLE);
            navUsername.setVisibility(View.VISIBLE);
            navUsername.setText(hashMap.get(SessionManagement.NAME));
            Log.d(TAG, "navUsername: "+navUsername.toString());

            String profileImage = hashMap.get(SessionManagement.IMAGE);
            String userimage = BASE_URL+"uploads/"+ profileImage;
            Log.d(TAG, "onCreateimage: "+userimage);
            Glide.with(NavigationActivity.this)
                    .load(userimage)
                    .placeholder(R.drawable.defauleimage)
                    .into(imageView);
//            imageView.setImageResource(hashMap.get(SessionManagement.IMAGE));



            navUsername.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            });



        }
    }
    private void initialize() {

        management = MyApplication.getSessionInstance();
        hashMap = management.getSessionDetails();
        View headerView = navigationView.getHeaderView(0);
        navUsername = headerView.findViewById(R.id.textView3);
        welcomeText = headerView.findViewById(R.id.welcome_text);
        imageView = headerView.findViewById(R.id.circular_image);


    }

    protected boolean useToolbar()
    {
        return true;
    }

    protected void setUpNavView()
    {
        navigationView.setNavigationItemSelectedListener(this);

        if(useDrawerToggle())
        {
            ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, fullLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            fullLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();
        }
        else if(useToolbar() && getSupportActionBar() != null)
        {
            getSupportActionBar().setHomeButtonEnabled(true);

        }
    }

    protected boolean useDrawerToggle()
    {
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        fullLayout.closeDrawer(GravityCompat.START);
        selectedNavItemId = item.getItemId();

        return onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        fullLayout= findViewById(R.id.drawer_layout);
        if (fullLayout.isDrawerOpen(GravityCompat.START)) {
            fullLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        }
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
