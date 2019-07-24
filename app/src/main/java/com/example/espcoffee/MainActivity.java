package com.example.espcoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.espcoffee.http.RequestHelper;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Bundle mainActivityBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        mainActivityBundle = new Bundle();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*      для запуска анимации
        final ImageView animImageView = (ImageView) findViewById(R.id.imageView3); id элемента
        animImageView.setBackgroundResource(R.drawable.coffee_bean);
        animImageView.post(((AnimationDrawable) animImageView.getBackground())::start);

*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_power) {
            sendGetRequest("http://192.168.1.150:80/socket1power");
        } else if (id == R.id.nav_refresh) {
            sendGetRequest("http://192.168.1.150:80/");
        }

        return true;
    }

    @Deprecated
    public void socket1Power(View view) {
        sendGetRequest("http://192.168.1.150:80/socket1power");
    }

    public void socket2Power(View view) {
        Intent intent = new Intent(MainActivity.this, BeverageActivity.class);
        createBundle(R.drawable.cappuccino, R.string.cappucino_beverage_info, R.string.cappucino, "http://192.168.1.150:80/socket2power", 100);
        intent.putExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE, mainActivityBundle);
        startActivity(intent);
    }

    public void socket3Power(View view) {
        Intent intent = new Intent(MainActivity.this, BeverageActivity.class);
        createBundle(R.drawable.espresso, R.string.espresso_beverage_info, R.string.espresso, "http://192.168.1.150:80/socket3power", 1000);
        intent.putExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE, mainActivityBundle);
        startActivity(intent);
    }

    public void socket4Power(View view) {
        Intent intent = new Intent(MainActivity.this, BeverageActivity.class);
        createBundle(R.drawable.espresso2x, R.string.espresso2x_beverage_info, R.string.espresso2x, "http://192.168.1.150:80/socket4power", 15000);
        intent.putExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE, mainActivityBundle);
        startActivity(intent);
    }

    public void socket5Power(View view) {
        Intent intent = new Intent(MainActivity.this, BeverageActivity.class);
        createBundle(R.drawable.americano, R.string.americano_beverage_info, R.string.americano, "http://192.168.1.150:80/socket5power", 20000);
        intent.putExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE, mainActivityBundle);
        startActivity(intent);
    }

    public void socket6Power(View view) {
        Intent intent = new Intent(MainActivity.this, BeverageActivity.class);
        createBundle(R.drawable.americano2x, R.string.americano2x_beverage_info, R.string.americano2x, "http://192.168.1.150:80/socket6power", 30000);
        intent.putExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE, mainActivityBundle);
        startActivity(intent);
    }

    public String sendGetRequest(String url) {
        RequestHelper requestHelper = new RequestHelper(this, url);
        requestHelper.execute();
        return requestHelper.getCompletableHttpBody();
    }

    private void createBundle(int coffeeImageId, int textId, int nameId, String url, int percent) {
        mainActivityBundle.putInt("coffeeImageId", coffeeImageId);
        mainActivityBundle.putInt("textId", textId);
        mainActivityBundle.putInt("nameId", nameId);
        mainActivityBundle.putString("url", url);
        mainActivityBundle.putInt("percent", percent);
    }
}

