package com.example.solar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.solar.Models.UserInfo;
import com.example.solar.fragment.DontHasPV;
import com.example.solar.fragment.HasPV;
import com.example.solar.pannelManage.PersonnalActivity;
import com.example.solar.pannelManage.RegisterActivity;
import com.example.solar.personManage.LoginActivity;
import com.example.solar.tabPager.CustomViewPager;
import com.example.solar.tabPager.PagerAdapter;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private UserInfo user;

    private CustomViewPager viewPager;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        user = (UserInfo)intent.getSerializableExtra("USER_INFO");



        setupToolBarMenu();
        setupNavigationDrawerMenu();

        setupViewPager();
    }

    private void setupToolBarMenu(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
    }

    private void setupNavigationDrawerMenu(){
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        TextView tv = headerView.findViewById(R.id.navi_tv_name);
        tv.setText(user.getName() + " 님");

        navigationView.getMenu().clear();

        if(user.getId().equals("NONE"))
            navigationView.inflateMenu(R.menu.nonregister_menu);
        else
            navigationView.inflateMenu(R.menu.register_menu);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    public boolean onNavigationItemSelected(MenuItem menuItem){
        String itemName = (String) menuItem.getTitle();

        closeDrawer();

        switch (menuItem.getItemId()){
            case R.id.menu_pannel_create:
                Intent i1 = new Intent(this, RegisterActivity.class);
                i1.putExtra("USER_INFO", user);
                startActivity(i1);
                break;

            case R.id.menu_pannel_read:
                Intent i2 = new Intent(this, PersonnalActivity.class);
                i2.putExtra("USER_INFO", user);
                startActivity(i2);
                break;

            case R.id.menu_user_logout:
                Intent i3 = new Intent(this, LoginActivity.class);
                startActivity(i3);
                finish();
                break;
        }

        return true;
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void showDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            closeDrawer();
        else
            super.onBackPressed();
    }

    private void setupViewPager() {
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        if(user.isHasPV()){
            adapter.addFragment(new HasPV());
        }
        else{
            adapter.addFragment(new DontHasPV());
        }

        viewPager.setAdapter(adapter);
        //viewPager.setOffscreenPageLimit(10);
    }
}
