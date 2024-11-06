package com.eraysirdas.dictionaryproject;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.eraysirdas.dictionaryproject.databinding.ActivityMainSignBinding;
import com.google.android.material.tabs.TabLayout;

public class MainSignActivity extends AppCompatActivity {
    private ActivityMainSignBinding binding;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainSignBinding.inflate(getLayoutInflater());
        View view =binding.getRoot();
        setContentView(view);

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Giriş Yap"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Kayıt Ol"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPagerAdapter = new ViewPagerAdapter(fragmentManager,getLifecycle());
        binding.viewPager.setAdapter(viewPagerAdapter);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });
    }
}