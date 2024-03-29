package com.usts.fee_front;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.usts.fee_front.databinding.ActivityMainBinding;
import com.usts.fee_front.pojo.Student;
import com.usts.fee_front.utils.NetworkConstants;
import com.usts.fee_front.utils.OkHttpCallback;
import com.usts.fee_front.utils.OkHttpUtils;
import com.usts.fee_front.utils.ResponseCode;
import com.usts.fee_front.utils.Result;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author zdaneel
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private AppBarConfiguration appBarConfiguration;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        /*
         * 处理导航信息
         */
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_setting,
                R.id.feeListFragment, R.id.feeAddFragment, R.id.feeDetailFragment, R.id.feeEditFragment,
                R.id.commentListFragment, R.id.commentAddFragment, R.id.commentDetailFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.navView.setOnItemSelectedListener(item -> {
            NavigationUI.onNavDestinationSelected(item, navController);
            return true;
        });
        /*
         * 处理MyApplication中的student数据
         */
        if ( null == MyApplication.getStudent()) {
            OkHttpUtils.get(NetworkConstants.QUERY_ME_URL, new OkHttpCallback() {
                @Override
                public void onFinish(String dataJson) throws JsonProcessingException {
                    if (dataJson != null) {
                        Student student = mapper.readValue(dataJson, Student.class);
                        MyApplication.setStudent(student);
                    }
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}