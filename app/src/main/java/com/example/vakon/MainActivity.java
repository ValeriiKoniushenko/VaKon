package com.example.vakon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ActionBar actionBar;
    public static final String TAG = "MainActivity";

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.bringToFront();
        navigationView.requestLayout();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_menu, R.string.close_menu);

        if ((actionBar = getSupportActionBar()) != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Menu menu = navigationView.getMenu();
        boolean isLogged = false;
        if (isLogged)
        {
            menu.findItem(R.id.item_profile_login).setVisible(false);
            menu.findItem(R.id.item_profile_signup).setVisible(false);
        }
        else
        {
            menu.findItem(R.id.item_profile_logout).setVisible(false);
        }

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.item_home);
    }

    public boolean closeNavigationBar()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    public boolean changeFragment(int id)
    {
        Fragment fragment = null;

        if (id == R.id.item_home)
        {
            fragment = new HomeFragment();
            navigationView.setCheckedItem(R.id.item_home);
        }
        if (id == R.id.main_menu_item_location)
        {
            fragment = new LocationFragment();
            navigationView.setCheckedItem(R.id.main_menu_item_location);
        }

        if (fragment != null)
        {
            closeNavigationBar();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment, fragment);
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (!closeNavigationBar())
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.main_menu_item_misc_share)
        {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "You can spy for this cool project. Just download an app: https://github.com/ValeriiKoniushenko/VaKon");
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, "Share"));

            closeNavigationBar();
            return true;
        }
        else if (id == R.id.main_menu_item_communications_phone || id == R.id.main_menu_item_communications_viber)
        {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + getResources().getString(R.string.phone_number)));
            startActivity(intent);

            closeNavigationBar();
            return true;
        }
        else if (id == R.id.main_menu_item_communications_telegram)
        {
            String url = getResources().getString(R.string.link_to_telegram);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

            closeNavigationBar();
            return true;
        }
        else if (id == R.id.main_menu_item_communications_email)
        {
            String[] TO = { getResources().getString(R.string.email) };
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");

            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello Valerii");

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                finish();
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this,
                        "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }

            closeNavigationBar();
            return true;
        }
        else
        {
            return changeFragment(id);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Rect viewRect = new Rect();
        navigationView.getGlobalVisibleRect(viewRect);

        if (!viewRect.contains((int)ev.getRawX(), (int)ev.getRawY()))
        {
            closeNavigationBar();
        }
        return super.dispatchTouchEvent(ev);
    }
}