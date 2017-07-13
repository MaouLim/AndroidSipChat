package bupt.androidsipchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import bupt.androidsipchat.adapter.ContactRecycleViewAdapter;
import bupt.androidsipchat.datestruct.MessageStruct;

/**
 * Created by sheju on 2017/7/12.
 */

public class ContactAcitivity extends AppCompatActivity {

    int dialogid = 200;

    RecyclerView recyclerView;
    ContactRecycleViewAdapter contactRecycleViewAdapter;

    String contactName;

    List<MessageStruct> messageStructs = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact);

        initRecycleView();
        initButtonListener();

        setItemClick();


    }

    public void initRecycleView() {
        recyclerView = (RecyclerView) findViewById(R.id.contacts);
        contactRecycleViewAdapter = new ContactRecycleViewAdapter(this);

        contactRecycleViewAdapter.initMessages(messageStructs);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(contactRecycleViewAdapter);


    }

    private void initButtonListener() {
        Button button = (Button) findViewById(R.id.contact_add);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialDialog.Builder(ContactAcitivity.this)
                        .title("新联系人")
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT)
                        .input("联系人名字", " ", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                String name = input.toString();
                                contactName = name;
                                String[] t = contactName.split(" ");
                                contactName = t[1];
                                Log.e("InputDialog", contactName);
                                // Do something
                                final MessageStruct messageStruct = new MessageStruct(contactName, "sip:" + contactName + "@" + "dd.dev.com", 0);

                                messageStructs.add(new MessageStruct(messageStruct));
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        contactRecycleViewAdapter.addNewMessage(messageStruct);
                                    }
                                });


                            }
                        })
                        .show();


            }
        });


    }

    public void setItemClick() {
        contactRecycleViewAdapter.setItemClick(new ContactRecycleViewAdapter.ItemClick() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(ContactAcitivity.this, ChatActivity.class);
                intent.putExtra("sequenceId", 0);
                intent.putExtra("from", messageStructs.get(position).getTitle());
                intent.putExtra("dialogId", dialogid);
                dialogid--;

                startActivity(intent);
            }
        });
    }


}
