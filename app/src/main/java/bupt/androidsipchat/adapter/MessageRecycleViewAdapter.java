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

    public void setMessages(List<MessageStruct> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
    }

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
        holder.title.setText(messages.get(position).getTitle());
        //holder.civ.setImageResource(messages.get(position).getMessageImage());
        if (messages.get(position).isChatRoomMessage) {
            holder.title.setText("server");
        }

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
        int i = 0;

        for (; i < messages.size(); i++) {
            if (messages.get(i).getSpecialId() == ms.getSpecialId()) {
                messages.remove(i);
                break;
            }
        }
        messages.add(0, ms);
        notifyDataSetChanged();
    }

    public void updateItems(List<MessageStruct> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
        notifyDataSetChanged();

    }

    public void notifyFitter(String s) {
        List<MessageStruct> fitterMessages = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getTitle().toLowerCase().contains(s.toLowerCase()) || messages.get(i).getContent().toLowerCase().contains(s.toLowerCase())) {
                fitterMessages.add(messages.get(i));
            }
        }

        updateItems(fitterMessages);
    }


}
