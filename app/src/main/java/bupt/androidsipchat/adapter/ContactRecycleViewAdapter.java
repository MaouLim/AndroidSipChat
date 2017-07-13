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

/**
 * Created by sheju on 2017/7/12.
 */


public class ContactRecycleViewAdapter extends RecyclerView.Adapter<ContactRecycleViewAdapter.ViewHolder> {

    private List<MessageStruct> messages = new ArrayList<>();

    LayoutInflater inflater;

    public ContactRecycleViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_recycleview_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.title.setText(messages.get(position).getTitle());
        holder.subTitle.setText(messages.get(position).getContent());

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick != null) {
                    itemClick.onItemClick(holder.getAdapterPosition());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView subTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.contact_who);
            subTitle = (TextView) itemView.findViewById(R.id.contact_address);


        }
    }


    public void initMessages(List<MessageStruct> m) {
        this.messages.addAll(m);
    }

    public void addNewMessage(MessageStruct m) {
        this.messages.add(new MessageStruct(m));
        notifyDataSetChanged();

        Log.e("AddNewMessage Size", "" + this.messages.size());
    }

    public interface ItemClick {
        void onItemClick(int position);

    }

    ItemClick itemClick;

    public void setItemClick(ItemClick i) {
        this.itemClick = i;
    }

}
