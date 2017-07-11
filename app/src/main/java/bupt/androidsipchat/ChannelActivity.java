package bupt.androidsipchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import bupt.androidsipchat.adapter.ChannelRecycleViewAdapter;
import bupt.androidsipchat.datestruct.MessageStruct;

/**
 * Created by sheju on 2017/7/11.
 */

public class ChannelActivity extends AppCompatActivity {

    private RecyclerView channelView;
    private ChannelRecycleViewAdapter crva;

    private List<MessageStruct> channelMessages = new ArrayList<>();

    {
        MessageStruct ms = new MessageStruct(R.drawable.lamu, "DailyNews", "壮壮荒神罪难6速6通关啦！！！", 0);
        ms.setSubTitle("Author:壮壮");
        channelMessages.add(ms);
        channelMessages.add(ms);
        channelMessages.add(ms);
        channelMessages.add(ms);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_channel);

        initRecycleView();
        initToolBar();


    }

    private void initRecycleView() {
        channelView = (RecyclerView) findViewById(R.id.channel_list);
        crva = new ChannelRecycleViewAdapter(this);

        crva.initData(channelMessages);

        channelView.setLayoutManager(new LinearLayoutManager(this));

        channelView.setAdapter(crva);

    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.channel_toolbar);
        toolbar.setTitle("Channel");

        setSupportActionBar(toolbar);
    }


}
