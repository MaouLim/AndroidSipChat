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

import bupt.androidsipchat.ChatActivity;
import bupt.androidsipchat.R;
import bupt.androidsipchat.adapter.MessageRecycleViewAdapter;
import bupt.androidsipchat.datestruct.MessageStruct;
import bupt.androidsipchat.recycleviewdecoration.ItemDecoration;

/**
 * Created by sheju on 2017/7/11.
 */

public class MessageFragment extends Fragment {
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_message, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initRecycleView(view);


    }


    private void initRecycleView(View parentView) {
        mainMessagesView = (RecyclerView) parentView.findViewById(R.id.main_recycleview_message);
        mrlva = new MessageRecycleViewAdapter(getActivity());

        mrlva.initData(messages);

        mrlva.setItemListener(new MessageRecycleViewAdapter.ItemClick() {
            @Override
            public void onItemClick(int position) {
                Log.i("Item", position + "");
                startActivity(new Intent(getActivity(), ChatActivity.class));
            }
        });

        mainMessagesView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mainMessagesView.setAdapter(mrlva);

        mainMessagesView.addItemDecoration(new ItemDecoration(getActivity(), ItemDecoration.HORIZONTAL_LIST));

    }

    public void updateMessageView(List<MessageStruct> messages) {
        mrlva.updateItems(messages);
    }

    public void setMessageList(List<MessageStruct> messages) {
        this.messages = messages;
        mrlva.setMessages(messages);
    }

    public void notifyFitter(String str) {
        mrlva.notifyFitter(str);
    }

    public List<MessageStruct> getMessages() {
        return messages;
    }

}
