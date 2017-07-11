package bupt.androidsipchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bupt.androidsipchat.R;
import bupt.androidsipchat.datestruct.MessageStruct;

/**
 * Created by sheju on 2017/7/11.
 */

public class ChannelRecycleViewAdapter extends RecyclerView.Adapter<ChannelRecycleViewAdapter.ChannelViewHolder> {


    private LayoutInflater inflater;

    private List<MessageStruct> channelMessages = new ArrayList<>();

    public ChannelRecycleViewAdapter(Context context) {

        inflater = LayoutInflater.from(context);
    }

    @Override
    public ChannelRecycleViewAdapter.ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChannelViewHolder(inflater.inflate(R.layout.item_recycleview_channel, parent, false));
    }

    @Override
    public void onBindViewHolder(ChannelRecycleViewAdapter.ChannelViewHolder holder, int position) {

        holder.title.setText(channelMessages.get(position).getTitle());
        holder.author.setText(channelMessages.get(position).getSubTitle());
        holder.content.setText(channelMessages.get(position).getContent());


    }

    @Override
    public int getItemCount() {
        return channelMessages.size();
    }

    public class ChannelViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView author;
        TextView content;

        public ChannelViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.item_channel_title);
            author = (TextView) itemView.findViewById(R.id.item_channel_author);
            content = (TextView) itemView.findViewById(R.id.item_channel_content);

        }
    }

    public void initData(List<MessageStruct> messageStructs) {
        this.channelMessages.addAll(messageStructs);
    }

    public void newMessageCome(MessageStruct ms) {
        int i = 0;


        for (; i < channelMessages.size(); i++) {
            if (channelMessages.get(i).getMessageId() == ms.getMessageId()) {
                channelMessages.remove(i);
                break;
            }
        }
        channelMessages.add(0, ms);
        notifyDataSetChanged();
    }

    public void updateItems(List<MessageStruct> channelMessages) {
        this.channelMessages.clear();
        this.channelMessages.addAll(channelMessages);
        notifyDataSetChanged();

    }

}
