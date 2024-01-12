package ba.sum.fsre.nramu_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import ba.sum.fsre.nramu_project.adapter.ViewPagerAdapter;



public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tabLayout = findViewById(R.id.tabLayout);
        this.viewPager = findViewById(R.id.viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        this.viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(this.tabLayout, this.viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Objave");
                            break;
                        case 1:
                            tab.setText("Dodaj objavu");
                            break;
                        case 2:
                            tab.setText("Profil");
                            break;
                    }
                }
        ).attach();

    }
}