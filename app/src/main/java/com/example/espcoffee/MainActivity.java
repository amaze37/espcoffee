package com.example.espcoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.espcoffee.http.RequestHelper;
import de.keyboardsurfer.android.widget.crouton.Crouton;

public class MainActivity extends AppCompatActivity {
    private Bundle mainActivityBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        mainActivityBundle = new Bundle();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.app_bar_power){
            sendGetRequest("http://192.168.1.150:80/socket1power");
        }
        if (id == R.id.app_bar_refresh){
            sendGetRequest("http://192.168.1.150:80/");
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
    }
}

