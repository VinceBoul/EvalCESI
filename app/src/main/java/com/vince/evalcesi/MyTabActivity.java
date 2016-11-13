package com.vince.evalcesi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.vince.evalcesi.adapter.TabsPagerAdapter;
import com.vince.evalcesi.helper.PreferenceHelper;

public class MyTabActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_my_tab);

            String token = PreferenceHelper.getToken(MyTabActivity.this);
            if (token == null) {
                Toast.makeText(this, this.getString(R.string.error_no_token), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MyTabActivity.this, MainActivity.class));
                finish();
            }

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText("TODO List"));
            tabLayout.addTab(tabLayout.newTab().setText("Utilisateurs"));
            tabLayout.addTab(tabLayout.newTab().setText("Bientôt disponible"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            final TabsPagerAdapter adapter = new TabsPagerAdapter
                    (getSupportFragmentManager(), token);
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.tchat_disconnect) {
                PreferenceHelper.setToken(MyTabActivity.this,"");
                startActivity(new Intent(this,MainActivity.class));

                this.finish();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }