package bupt.androidsipchat;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import bupt.androidsipchat.adapter.ChannelRecycleViewAdapter;
import bupt.androidsipchat.datestruct.MessageStruct;
import bupt.androidsipchat.service.MessageService;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sheju on 2017/7/11.
 */

public class ChannelActivity extends AppCompatActivity {

    private RecyclerView channelView;
    private ChannelRecycleViewAdapter crva;

    private List<MessageStruct> channelMessages = new ArrayList<>();

    private MessageStruct currentMessage;
    private int currentChannelId = -1;
    TextView title;

    String channelName;

    SweetAlertDialog pDialog;
    String temp;

    MessageService messageService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MessageService.DataBinder dataBinder = (MessageService.DataBinder) service;
            messageService = dataBinder.getService();

            channelMessages.clear();
            channelMessages.addAll(messageService.getChannelMessages(currentChannelId));

            messageService.setChannelMessageNotify(new MessageService.ChannelMessageNotify() {
                @Override
                public void onChannelMessageArrive(MessageStruct messageStruct) {
                    Log.e("ChannelActivity", "NewMessageArrive:" + " " + messageStruct.getTitle());
                    final MessageStruct m = messageStruct;
                    if (m.getSpecialId() == currentChannelId) {
                        channelMessages.add(new MessageStruct(m));
                        channelView.post(new Runnable() {
                            @Override
                            public void run() {
                                crva.newMessageCome(m);
                            }
                        });
                    }


                }
            });

            messageService.setChannelPubResult(new MessageService.ChannelPubResult() {
                @Override
                public void onPublishResponse(boolean status) {
                    pDialog.dismiss();
                    if (status) {
//                        MessageStruct messageStruct = new MessageStruct(" ", temp, 0);
//                        currentMessage = messageStruct;
//                        channelMessages.add(currentMessage);
//                        messageService.addNewChannelMessage(currentMessage);
//
//                        channelView.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                crva.newMessageCome(currentMessage);
//                            }
//                        });
                    } else {
                        channelView.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChannelActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

            if (crva != null) {
                crva.initData(channelMessages);
                crva.notifyDataSetChanged();
            }

            if (title != null) {
                title.setText(channelName);
            }


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        currentChannelId = intent.getIntExtra("channelId", -1);
        channelName = intent.getStringExtra("channelName");

        setContentView(R.layout.activity_channel);

        initRecycleView();

        initToolBar();


        bindService(new Intent(ChannelActivity.this, MessageService.class), serviceConnection, BIND_AUTO_CREATE);

        initButtonListener();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(serviceConnection);
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
        toolbar.setTitle("");
        title = (TextView) toolbar.findViewById(R.id.channel_name);

        setSupportActionBar(toolbar);
    }

    public void initButtonListener() {
        Button button = (Button) findViewById(R.id.channel_new_message_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(ChannelActivity.this)
                        .title("新消息")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("输入你的消息内容", " ", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                if (!input.toString().equals("")) {
                                    temp = input.toString();
                                    messageService.publishToChannel(" ", temp, channelName);

                                    pDialog = new SweetAlertDialog(ChannelActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                                    pDialog.setCancelable(false);
                                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                    pDialog.setTitleText("Loading");
                                    pDialog.setCancelable(false);
                                    pDialog.show();
                                }
                            }
                        }).show();

            }
        });
    }






}
