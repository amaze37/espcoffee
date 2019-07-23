package com.example.espcoffee;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.espcoffee.http.OkHttpCompleteFeature;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Bundle mainActivityBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            sendGetRequest("http://192.168.1.150:80/socket1power");
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Deprecated
    public void socket1Power(View view) {
        sendGetRequest("http://192.168.1.150:80/socket1power");
    }

    public void socket2Power(View view) {
        Intent intent = new Intent(MainActivity.this, BeverageActivity.class);
        createBundle(R.drawable.cappuccino, R.string.cappucino_beverage_info, R.string.cappucino, "http://192.168.1.150:80/socket2power");
        intent.putExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE, mainActivityBundle);
        startActivity(intent);
    }

    public void socket3Power(View view) {
        Intent intent = new Intent(MainActivity.this, BeverageActivity.class);
        createBundle(R.drawable.espresso, R.string.espresso_beverage_info, R.string.espresso, "http://192.168.1.150:80/socket3power");
        intent.putExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE, mainActivityBundle);
        startActivity(intent);
    }

    public void socket4Power(View view) {
        Intent intent = new Intent(MainActivity.this, BeverageActivity.class);
        createBundle(R.drawable.espresso2x, R.string.espresso2x_beverage_info, R.string.espresso2x, "http://192.168.1.150:80/socket4power");
        intent.putExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE, mainActivityBundle);
        startActivity(intent);
    }

    public void socket5Power(View view) {
        Intent intent = new Intent(MainActivity.this, BeverageActivity.class);
        createBundle(R.drawable.americano, R.string.americano_beverage_info, R.string.americano, "http://192.168.1.150:80/socket5power");
        intent.putExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE, mainActivityBundle);
        startActivity(intent);
    }

    public void socket6Power(View view) {
        Intent intent = new Intent(MainActivity.this, BeverageActivity.class);
        createBundle(R.drawable.americano2x, R.string.americano2x_beverage_info, R.string.americano2x, "http://192.168.1.150:80/socket6power");
        intent.putExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE, mainActivityBundle);
        startActivity(intent);
    }

    public String sendGetRequest(String url) {
        RequestHelper requestHelper = new RequestHelper(url);
        requestHelper.execute();
        return requestHelper.getCompletableHttpBody();
    }

    private void createBundle(int coffeeImageId, int textId, int nameId, String url) {
        mainActivityBundle.putInt("coffeeImageId", coffeeImageId);
        mainActivityBundle.putInt("textId", textId);
        mainActivityBundle.putInt("nameId", nameId);
        mainActivityBundle.putString("url", url);
    }

    private void showErrorMessage(String message) {
        runOnUiThread(() -> {
            ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment(this);
            mainActivityBundle.putString("message", message);
            errorDialogFragment.onCreateDialog(mainActivityBundle).show();
            errorDialogFragment.onDestroy();
        });
    }

    private class RequestHelper extends AsyncTask<Void, Void, Void> {
        private final OkHttpClient client = new OkHttpClient();
        private String url;
        private String responseBody;

        RequestHelper(String url) {
            this.url = url;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            if (!OkHttpCompleteFeature.isComplete.get()) {
                return null;
            }
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            OkHttpCompleteFeature callback = new OkHttpCompleteFeature();
            client.newCall(request).enqueue(callback);
            CompletableFuture<Response> future = callback.getFuture().thenApply(response -> response);
            try {
                responseBody = future.get().body().string();
            } catch (ExecutionException e) {
                showErrorMessage(String.format("Error: fail request %s", e.getMessage()));
            } catch (InterruptedException e) {
                showErrorMessage(String.format("ERROR: forbidden request %s", e.getMessage()));
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            } finally {
                OkHttpCompleteFeature.isComplete.set(true);
            }
            return null;
        }
        String getCompletableHttpBody() {
            return responseBody;
        }
    }
}

