package com.starsutdio.biglz.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.starsutdio.biglz.R;
import com.starsutdio.biglz.model.http.BigLZApi;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.GONE;
import static android.view.View.TEXT_ALIGNMENT_CENTER;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,View.OnClickListener{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.test_btn)
    Button test_btn;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private String acckey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        test_btn.setOnClickListener(this);
        mPreferences = getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        acckey = mPreferences.getString("accesskey",null);

        initDrawLayout();
        initTabLayout();


    }

    void initTabLayout(){
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_subject_black_24dp));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_explore_black_24dp));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_notifications_black_24dp));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_person_black_24dp));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position){
                    case 0:

                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    void initDrawLayout(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,WriteArticleActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View nav_header_view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        navigationView.setNavigationItemSelectedListener(this);
        LinearLayout mLayout = (LinearLayout)nav_header_view.findViewById(R.id.loginregister_layout);
        TextView login_text = (TextView)nav_header_view.findViewById(R.id.login_text);
        TextView register_text = (TextView)nav_header_view.findViewById(R.id.register_text);
        TextView me_text = (TextView)nav_header_view.findViewById(R.id.me_text);
        login_text.setOnClickListener(this);
        register_text.setOnClickListener(this);
        if (acckey!=null){
            mLayout.setVisibility(View.GONE);
            me_text.setVisibility(View.VISIBLE);
        }
    }

//    void initPopupWindow(Boolean hasLogin){{
//        if (!hasLogin){
//            View view = LayoutInflater.from(this).inflate(R.layout.pop_login,null,false);
//            View rootView = LayoutInflater.from(this).inflate(R.layout.activity_main,null);
//            PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
//            popupWindow.setContentView(view);
//            popupWindow.showAtLocation(rootView,Gravity.BOTTOM,0,0);
//            login_pop_btn = (Button)view.findViewById(R.id.login_pop_btn);
//            register_pop_btn = (Button)view.findViewById(R.id.register_pop_btn);
//            login_pop_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//                    startActivity(intent);
//                }
//            });
//            register_pop_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
//                    startActivity(intent);
//                }
//            });
//
//        }
//    }

//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.test_btn:
                File file = new File(Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/1.jpg");
                if (file!=null) Log.d("---------------",file.getAbsolutePath());
                break;
            case R.id.login_text:
                Intent login_intent =  new Intent(MainActivity.this,LoginActivity.class);
                startActivity(login_intent);
                break;
            case R.id.register_text:
                Intent register_intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(register_intent);
                break;

        }

    }

    void getImage()  {
        File file = new File(getExternalCacheDir().getAbsolutePath()+"/data/image.png");
        if (file!=null){
            Log.d("----------","sucess");
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                URL url = null;
//                try {
//                    url = new URL(url_str);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setDoInput(true);
//                    connection.connect();
//                    InputStream inputStream = connection.getInputStream();
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    File file = new File(getExternalCacheDir().getAbsolutePath()+"/data");
//                    File imageFile = new File(file,"image");
//                    if (!file.exists()) file.mkdir();
//                    FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
//                    boolean issuccess = bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
//                    if (issuccess){
//                        Log.d("----------------","sucess");
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
