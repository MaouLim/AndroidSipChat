package bupt.androidsipchat.mainfragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import bupt.androidsipchat.service.MessageService;

/**
 * Created by sheju on 2017/7/11.
 */

public class MessageFragment extends Fragment {
    private RecyclerView mainMessagesView;
    private MessageRecycleViewAdapter mrlva;

    MessageService messageService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MessageService.DataBinder dataBinder = (MessageService.DataBinder) service;
            messageService = dataBinder.getService();

            messages.clear();
            messages.addAll(messageService.getDialogs());

            messageService.setMessageNotify(new MessageService.MessageNotify() {
                @Override
                public void onNewMessageArrive(final MessageStruct message) {
                    newMessageCome(message);
                    if (mrlva != null) {
                        mainMessagesView.post(new Runnable() {
                            @Override
                            public void run() {
                                mrlva.newMessageCome(message);
                            }
                        });

                    }
                }
            });


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private List<MessageStruct> messages = new ArrayList<>();

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

        getActivity().bindService(new Intent(getActivity(), MessageService.class), serviceConnection, Context.BIND_AUTO_CREATE);


    }


    private void initRecycleView(View parentView) {
        mainMessagesView = (RecyclerView) parentView.findViewById(R.id.main_recycleview_message);
        mrlva = new MessageRecycleViewAdapter(getActivity());

        mrlva.initData(messages);

        mrlva.setItemListener(new MessageRecycleViewAdapter.ItemClick() {
            @Override
            public void onItemClick(int position) {
                Log.i("Item", position + "");
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("sequenceId", messages.get(position).getMessageId());
                intent.putExtra("from", messages.get(position).getTitle());
                intent.putExtra("dialogId", messages.get(position).getSpecialId());

                startActivity(intent);
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

    public void newMessageCome(MessageStruct ms) {
        int i = 0;
        for (; i < messages.size(); i++) {
            if (messages.get(i).getMessageId() == ms.getMessageId()) {
                messages.remove(i);
                break;
            }
        }
        messages.add(0, ms);
    }

}
