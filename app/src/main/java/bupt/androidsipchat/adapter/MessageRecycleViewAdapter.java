package bupt.androidsipchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bupt.androidsipchat.R;
import bupt.androidsipchat.datestruct.MessageStruct;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sheju on 2017/7/4.
 *
 */

public class MessageRecycleViewAdapter extends RecyclerView.Adapter<MessageRecycleViewAdapter.MessageViewHolder> {

    private LayoutInflater inflater;
    private List<MessageStruct> messages = new ArrayList<>();

    public MessageRecycleViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MessageViewHolder(inflater.inflate(R.layout.item_recycleview_message, parent, false));
    }


    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {

        holder.civ.setImageResource(messages.get(position).getMessageImage());
        holder.title.setText(messages.get(position).getTitle());
        holder.lastContent.setText(messages.get(position).getContent());

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onItemClick(holder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        if (messages != null) {
            Log.i("message", " " + messages.size());
            return messages.size();
        } else {
            return 0;
        }

    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civ;
        TextView title;
        TextView lastContent;
        View parentView;


        private MessageViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            civ = (CircleImageView) itemView.findViewById(R.id.message_circle_head);
            title = (TextView) itemView.findViewById(R.id.message_text_title);
            lastContent = (TextView) itemView.findViewById(R.id.message_text_content);
        }
    }

    public void initData(List<MessageStruct> messages) {
        this.messages.addAll(messages);
    }

    public interface ItemClick {
        void onItemClick(int position);
    }

    private ItemClick itemClick;

    public void setItemListener(ItemClick itemListener) {
        itemClick = itemListener;
    }

    public void newMessageCome(MessageStruct ms) {
        for (MessageStruct m : messages) {
            if (m.getMessageId() == ms.getMessageId()) {
                messages.remove(m);
                break;
            }
        }
        messages.add(0, ms);
        notifyDataSetChanged();
    }


}
