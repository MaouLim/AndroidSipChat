package bupt.androidsipchat;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import bupt.androidsipchat.mainfragment.ChannelFragment;
import bupt.androidsipchat.mainfragment.MessageFragment;

/**
 * Created by sheju on 2017/7/4.
 *
 */

public class MainActivity extends AppCompatActivity {


    private FragmentManager fragmentManager;
    private MessageFragment messageFragment;
    private ChannelFragment channelFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initDrawerView();

        initFragment();

        initSearchEdit();

    }

    private void initDrawerView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_icons8_menu);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_string, R.string.drawer_string);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.drawer_channel: {
                        if (!channelFragment.isVisible()) {
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_content_layout, channelFragment);
                            fragmentTransaction.commit();
                        }


                        break;
                    }
                    case R.id.drawer_contract: {
                        Intent intent = new Intent(MainActivity.this, ContactAcitivity.class);
                        startActivity(intent);

                        break;
                    }
                    case R.id.drawer_message: {
                        if (!messageFragment.isVisible()) {
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_content_layout, messageFragment);
                            fragmentTransaction.commit();
                        }

                        break;
                    }

                    default: {
                        break;
                    }
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void initFragment() {
        messageFragment = new MessageFragment();
        channelFragment = new ChannelFragment();

        fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_content_layout, messageFragment);
        fragmentTransaction.commit();

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

                channelFragment.setChannelList(channelFragment.getChannels());
                messageFragment.setMessageList(messageFragment.getMessages());
                if (fitter.equals("")) {
                    channelFragment.updateChannelView(channelFragment.getChannels());
                    messageFragment.updateMessageView(messageFragment.getMessages());
                } else {
                    channelFragment.notifyFitter(fitter);
                    messageFragment.notifyFitter(fitter);

                }


            }
        });


    }

}
