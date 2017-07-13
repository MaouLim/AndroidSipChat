package bupt.androidsipchat;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bupt.androidsipchat.adapter.ChatRecycleViewAdapter;
import bupt.androidsipchat.datestruct.MessageStruct;
import bupt.androidsipchat.service.MessageService;

/**
 * Created by sheju on 2017/7/5.
 */

public class ChatActivity extends AppCompatActivity {


    private RecyclerView chartsView;
    private ChatRecycleViewAdapter crlva;


    private MessageStruct currentMessage;
    private int currentMessageId = -1;
    TextView title;

    String to;


    MessageService messageService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MessageService.DataBinder dataBinder = (MessageService.DataBinder) service;
            messageService = dataBinder.getService();


            messages.clear();
            messages.addAll(messageService.getChatMessageHistory(currentMessageId));

            messageService.setChatNotify(new MessageService.ChatNotify() {
                @Override
                public void onNewMessageArrive(MessageStruct message, boolean isFind) {
                    final MessageStruct m = message;
                    if (m.getSpecialId() == currentMessageId) {
                        m.setViewType(ChatRecycleViewAdapter.OTHERTYPE);
                        newMessageCome(m);
                        chartsView.post(new Runnable() {
                            @Override
                            public void run() {
                                crlva.newMessageCome(m);
                            }
                        });
                    }

                }
            });

            if (crlva != null) {
                crlva.initData(messages);
                crlva.notifyDataSetChanged();
            }

            if (title != null) {
                title.setText(to);
            }




        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private List<MessageStruct> messages = new ArrayList<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Intent intent = getIntent();
        currentMessageId = intent.getIntExtra("dialogId", -1);
        to = intent.getStringExtra("from");



        initRecycleView();

        initToolBar();

        initButtonListener();

        bindService(new Intent(ChatActivity.this, MessageService.class), serviceConnection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(serviceConnection);
    }



    private void initRecycleView() {
        chartsView = (RecyclerView) findViewById(R.id.chart_recycler_view);
        crlva = new ChatRecycleViewAdapter(this);

        crlva.initData(messages);

        chartsView.setLayoutManager(new LinearLayoutManager(this));
        chartsView.setAdapter(crlva);

    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.chart_toolbar);
        title = (TextView) toolbar.findViewById(R.id.chart_toolbar_title);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
    }

    private void initButtonListener() {
        final TextInputEditText tiet = (TextInputEditText) findViewById(R.id.chart_input);
        Button button = (Button) findViewById(R.id.chart_send_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tiet.getText().toString();
                if (!text.equals("")) {
                    final int currentChatId = currentMessageId;


                    currentMessage = new MessageStruct(messageService.user.getUserName(), text, 0);
                    currentMessage.setViewType(ChatRecycleViewAdapter.SELFTYPE);
                    tiet.setText("");
                    crlva.newMessageCome(currentMessage);
                    messages.add(currentMessage);
                    messageService.sendMessage(to, text, currentChatId);
                }
            }
        });
    }

    public void newMessageCome(MessageStruct ms) {
        int i = 0;
        for (; i < messages.size(); i++) {
            if (messages.get(i).getMessageId() == ms.getMessageId()) {
                messages.remove(i);
                break;
            }
        }
        ms.setViewType(ChatRecycleViewAdapter.OTHERTYPE);
        messages.add(ms);
    }

}
