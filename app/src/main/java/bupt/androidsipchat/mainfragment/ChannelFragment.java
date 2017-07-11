package bupt.androidsipchat.mainfragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import bupt.androidsipchat.ChannelActivity;
import bupt.androidsipchat.R;
import bupt.androidsipchat.adapter.MessageRecycleViewAdapter;
import bupt.androidsipchat.datestruct.MessageStruct;
import bupt.androidsipchat.recycleviewdecoration.ItemDecoration;

/**
 * Created by sheju on 2017/7/11.
 *
 */

public class ChannelFragment extends Fragment {
    private RecyclerView mainChannelView;
    private MessageRecycleViewAdapter mrlva;

    private List<MessageStruct> messages = new ArrayList<>();

    {
        messages.add(new MessageStruct(R.drawable.lamu, "DailyNews", "邱雨壮今日以难6速6通关荒神罪！！！小伙伴还有更快的吗？", 0));
        messages.add(new MessageStruct(R.drawable.lamu, "叁伍零捌", "林华：还有代码没写完，赶紧过来继续写！！！", 1));
        messages.add(new MessageStruct(R.drawable.lamu, "3508", "ZhuangZhuang：= =", 2));

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_channel, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initRecycleView(view);


    }


    private void initRecycleView(View parentView) {
        mainChannelView = (RecyclerView) parentView.findViewById(R.id.main_recycleview_channel);
        mrlva = new MessageRecycleViewAdapter(getActivity());

        mrlva.initData(messages);

        mrlva.setItemListener(new MessageRecycleViewAdapter.ItemClick() {
            @Override
            public void onItemClick(int position) {
                Log.i("Item", position + "");
                startActivity(new Intent(getActivity(), ChannelActivity.class));
            }
        });

        mainChannelView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mainChannelView.setAdapter(mrlva);

        mainChannelView.addItemDecoration(new ItemDecoration(getActivity(), ItemDecoration.HORIZONTAL_LIST));

    }

    public void updateChannelView(List<MessageStruct> messages) {
        mrlva.updateItems(messages);
    }

    public void setChannelList(List<MessageStruct> messages) {
        this.messages = messages;
        mrlva.setMessages(messages);
    }

    public void notifyFitter(String str) {
        mrlva.notifyFitter(str);
    }

    public List<MessageStruct> getChannels() {
        return messages;
    }

}
