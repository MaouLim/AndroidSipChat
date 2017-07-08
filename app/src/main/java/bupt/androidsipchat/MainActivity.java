package bupt.androidsipchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import bupt.androidsipchat.adapter.MessageRecycleViewAdapter;
import bupt.androidsipchat.datestruct.MessageStruct;
import bupt.androidsipchat.recycleviewdecoration.ItemDecoration;

/**
 * Created by sheju on 2017/7/4.
 *
 */

public class MainActivity extends AppCompatActivity {


    private RecyclerView mainMessagesView;
    private MessageRecycleViewAdapter mrlva;

    private List<MessageStruct> messages = new ArrayList<>();

    {
        messages.add(new MessageStruct(R.drawable.lamu, "叁伍零捌", "林华：还有代码没写完，赶紧过来继续写！！！", 0));
        messages.add(new MessageStruct(R.drawable.lamu, "叁伍零捌", "林华：还有代码没写完，赶紧过来继续写！！！", 1));
        messages.add(new MessageStruct(R.drawable.lamu, "叁伍零捌", "林华：还有代码没写完，赶紧过来继续写！！！", 2));
        messages.add(new MessageStruct(R.drawable.lamu, "叁伍零捌", "林华：还有代码没写完，赶紧过来继续写！！！", 3));
        messages.add(new MessageStruct(R.drawable.lamu, "叁伍零捌", "林华：还有代码没写完，赶紧过来继续写！！！", 4));
        messages.add(new MessageStruct(R.drawable.lamu, "3508", "ZhuangZhuang：= =", 4));

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initDrawerView();

        initRecycleView();

        initSearchEdit();

    }

    private void initDrawerView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_icons8_menu);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_string, R.string.drawer_string);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });
    }

    private void initRecycleView() {
        mainMessagesView = (RecyclerView) findViewById(R.id.main_recycleview_message);
        mrlva = new MessageRecycleViewAdapter(this);

        mrlva.initData(messages);

        mrlva.setItemListener(new MessageRecycleViewAdapter.ItemClick() {
            @Override
            public void onItemClick(int position) {
                Log.i("Item", position + "");
                startActivity(new Intent(MainActivity.this, ChartActivity.class));
            }
        });

        mainMessagesView.setLayoutManager(new LinearLayoutManager(this));

        mainMessagesView.setAdapter(mrlva);

        mainMessagesView.addItemDecoration(new ItemDecoration(this, ItemDecoration.HORIZONTAL_LIST));

    }

    private void initSearchEdit() {
        EditText search = (EditText) findViewById(R.id.main_search);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String fitter = s.toString();
                mrlva.setMessages(messages);
                if (fitter.equals("")) {
                    mrlva.updateItems(messages);
                } else {

                    mrlva.notifyFitter(fitter);

                }


            }
        });


    }

}
