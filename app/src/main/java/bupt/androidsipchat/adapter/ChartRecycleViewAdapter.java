package bupt.androidsipchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleTextView;

import java.util.ArrayList;
import java.util.List;

import bupt.androidsipchat.R;
import bupt.androidsipchat.datestruct.MessageStruct;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sheju on 2017/7/5.
 */

public class ChartRecycleViewAdapter extends RecyclerView.Adapter<ChartRecycleViewAdapter.ChartViewHolder> {

    private LayoutInflater inflater;
    private List<MessageStruct> messages = new ArrayList<>();

    public ChartRecycleViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }


    public static final int SELFTYPE = 402;
    public static final int OTHERTYPE = 279;


    @Override
    public ChartRecycleViewAdapter.ChartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == SELFTYPE) {
            return new ChartViewHolder(inflater.inflate(R.layout.item_recycleview_chart_myself, parent, false), viewType);
        } else if (viewType == OTHERTYPE) {
            return new ChartViewHolder(inflater.inflate(R.layout.item_recycleview_chart_other, parent, false), viewType);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(ChartRecycleViewAdapter.ChartViewHolder holder, int position) {
        holder.civ.setImageResource(messages.get(position).getMessageImage());
        holder.title.setText(messages.get(position).getTitle());
        holder.bubbleTextView.setText(messages.get(position).getContent());
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ChartViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civ;
        TextView title;
        BubbleTextView bubbleTextView;


        public ChartViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == SELFTYPE) {
                civ = (CircleImageView) itemView.findViewById(R.id.chart_circle_head_self);
                title = (TextView) itemView.findViewById(R.id.chart_text_title_self);
                bubbleTextView = (BubbleTextView) itemView.findViewById(R.id.chart_text_content_self);
            } else if (viewType == OTHERTYPE) {
                civ = (CircleImageView) itemView.findViewById(R.id.chart_circle_head);
                title = (TextView) itemView.findViewById(R.id.chart_text_title);
                bubbleTextView = (BubbleTextView) itemView.findViewById(R.id.chart_text_content);
            }
        }
    }

    public void initData(List<MessageStruct> messages) {
        this.messages.addAll(messages);
    }

    public void newMessageCome(MessageStruct ms) {
        messages.add(ms);
        notifyItemChanged(messages.size() - 1);
    }

}
