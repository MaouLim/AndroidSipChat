package bupt.androidsipchat;

import android.os.Bundle;
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

import bupt.androidsipchat.adapter.ChartRecycleViewAdapter;
import bupt.androidsipchat.datestruct.MessageStruct;

/**
 * Created by sheju on 2017/7/5.
 */

public class ChartActivity extends AppCompatActivity {


    private RecyclerView chartsView;
    private ChartRecycleViewAdapter crlva;


    private MessageStruct currentMessage;
    private int currentMessageId = 0;

    private List<MessageStruct> messages = new ArrayList<>();

    {
        messages.add(new MessageStruct(R.drawable.lamu, "叁伍零捌", "林华：还有代码没写完，赶紧过来继续写！！！", 0));
        messages.add(new MessageStruct(R.drawable.lamu, "叁伍零捌", "林华：还有代码没写完，赶紧过来继续写！！！", 1));
        messages.add(new MessageStruct(R.drawable.lamu, "叁伍零捌", "林华：还有代码没写完，赶紧过来继续写！！！", 2));
        messages.add(new MessageStruct(R.drawable.lamu, "叁伍零捌", "林华：还有代码没写完，赶紧过来继续写！！！", 3));
        messages.add(new MessageStruct(R.drawable.lamu, "叁伍零捌", "林华：还有代码没写完，赶紧过来继续写！！！", 4));

        for (MessageStruct ms : messages) {
            ms.setViewType(ChartRecycleViewAdapter.OTHERTYPE);
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        initRecycleView();

        initToolBar();

        initButtonListener();

    }

    private void initRecycleView() {
        chartsView = (RecyclerView) findViewById(R.id.chart_recycler_view);
        crlva = new ChartRecycleViewAdapter(this);

        crlva.initData(messages);

        chartsView.setLayoutManager(new LinearLayoutManager(this));
        chartsView.setAdapter(crlva);

    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.chart_toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.chart_toolbar_title);
        title.setText("林华");
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
                currentMessageId += 1;
                currentMessage = new MessageStruct(R.drawable.lamu, "壮壮", text, currentMessageId);
                currentMessage.setViewType(ChartRecycleViewAdapter.SELFTYPE);
                tiet.setText("");
                crlva.newMessageCome(currentMessage);
            }
        });
    }

}
