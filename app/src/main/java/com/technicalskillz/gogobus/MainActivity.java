package com.technicalskillz.gogobus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.technicalskillz.gogobus.BusStation.RecyclerviewAdapterStation;
import com.technicalskillz.gogobus.BusStation.StationList;
import com.technicalskillz.gogobus.BusStation.Value;
import com.technicalskillz.gogobus.Utils.ViewPagerAdatper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerviewAdapterStation adapterStation;
    TextView textView;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdatper adatper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.include2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Go Go Bus");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);


        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);
        adatper=new ViewPagerAdatper(getSupportFragmentManager(),this,1);

        viewPager.setAdapter(adatper);

    }

}


