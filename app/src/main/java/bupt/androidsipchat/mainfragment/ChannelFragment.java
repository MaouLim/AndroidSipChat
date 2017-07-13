package bupt.androidsipchat.mainfragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import bupt.androidsipchat.ChannelActivity;
import bupt.androidsipchat.R;
import bupt.androidsipchat.adapter.MessageRecycleViewAdapter;
import bupt.androidsipchat.datestruct.Channel;
import bupt.androidsipchat.datestruct.MessageStruct;
import bupt.androidsipchat.recycleviewdecoration.ItemDecoration;
import bupt.androidsipchat.service.MessageService;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sheju on 2017/7/11.
 *
 */

public class ChannelFragment extends Fragment {
    private RecyclerView mainChannelView;
    private MessageRecycleViewAdapter mrlva;
    SweetAlertDialog pDialog;

    int numsId = 3000;

    private List<MessageStruct> messages = new ArrayList<>();

    MessageService messageService;

    String temp;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MessageService.DataBinder dataBinder = (MessageService.DataBinder) service;
            messageService = dataBinder.getService();

            messages.clear();
            messages.addAll(messageService.getChannelList());

            messageService.setChannelSubResult(new MessageService.ChannelSubResult() {
                @Override
                public void onSubscribeResponse(boolean status) {
                    pDialog.dismiss();
                    if (status) {
                        mainChannelView.post(new Runnable() {
                            @Override
                            public void run() {
                                MessageStruct messageStruct = new MessageStruct(temp, " ", numsId);
                                Channel channel = new Channel(messageStruct.getTitle(), " ", " ");
                                messageStruct.setSpecialId(channel.getId());
                                mrlva.newMessageCome(messageStruct);
                                messages.add(messageStruct);
                                numsId--;
                                Toast.makeText(getActivity(), "成功订阅", Toast.LENGTH_SHORT).show();
                                messageService.addNewChannel(channel);

                            }
                        });
                    } else {
                        mainChannelView.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "订阅失败", Toast.LENGTH_SHORT).show();
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

        initButtonListener(view);

        initRecycleView(view);

        getActivity().bindService(new Intent(getActivity(), MessageService.class), serviceConnection, Context.BIND_AUTO_CREATE);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(serviceConnection);
    }


    private void initRecycleView(View parentView) {
        mainChannelView = (RecyclerView) parentView.findViewById(R.id.main_recycleview_channel);
        mrlva = new MessageRecycleViewAdapter(getActivity());

        mrlva.initData(messages);

        mrlva.setItemListener(new MessageRecycleViewAdapter.ItemClick() {
            @Override
            public void onItemClick(int position) {
                Log.i("Item", position + "");
                Intent intent = new Intent(getActivity(), ChannelActivity.class);
                intent.putExtra("channelId", messages.get(position).getSpecialId());
                intent.putExtra("channelName", messages.get(position).getTitle());

                startActivity(intent);
            }
        });

        mainChannelView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mainChannelView.setAdapter(mrlva);

        mainChannelView.addItemDecoration(new ItemDecoration(getActivity(), ItemDecoration.HORIZONTAL_LIST));

    }

    private void initButtonListener(View view) {
        Button button = (Button) view.findViewById(R.id.add_sub_new_channel);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialDialog.Builder(getActivity())
                        .title("新订阅")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("输入你需要订阅的频道名", " ", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                if (!input.toString().equals("")) {
                                    temp = input.toString();
                                    temp = temp.substring(1);
                                    Log.e("Channel", temp);
                                    messageService.subscribeChannel(temp);

                                    pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
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
