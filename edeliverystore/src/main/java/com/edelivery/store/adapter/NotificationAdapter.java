package com.edelivery.store.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edelivery.store.persistentroomdata.Notification;
import com.elluminati.edelivery.store.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Ravi Bhalodi on 20,July,2020 in Elluminati
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifications;

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.tvNotificationMsg.setText(notification.getMessage());
        holder.tvNotificationDate.setText(notification.getDate());
        holder.div.setVisibility(getItemCount() - 1 == position ? View.GONE : View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return notifications == null ? 0 : notifications.size();
    }

    protected class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNotificationTitle, tvNotificationMsg, tvNotificationDate;

        private View div;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotificationMsg = itemView.findViewById(R.id.tvNotificationMsg);
            tvNotificationDate = itemView.findViewById(R.id.tvNotificationDate);
            div = itemView.findViewById(R.id.div);
        }
    }
}
