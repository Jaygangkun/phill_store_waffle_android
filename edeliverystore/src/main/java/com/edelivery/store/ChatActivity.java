package com.edelivery.store;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edelivery.store.adapter.ChatAdapter;
import com.edelivery.store.models.datamodel.Message;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ChatActivity extends BaseActivity {
    private ChatAdapter chatAdapter;
    private RecyclerView rcvChat;
    private DatabaseReference firebaseDatabaseReference;
    private Button btnSend;
    private EditText messageEditText;
    private String MESSAGES_CHILD = "Demo";
    private SimpleDateFormat webFormat;
    private String CHAT_TYPE = "";
    private String receiver_id = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getIntent().getStringExtra(Constant.TITLE));

        //setToolbarEditIcon(false, 0);

        MESSAGES_CHILD = getIntent().getStringExtra(Constant.ORDER_ID);
        CHAT_TYPE = getIntent().getStringExtra(Constant.TYPE);
        receiver_id = getIntent().getStringExtra(Constant.RECEIVER_ID);

        initFirebaseChat();
        rcvChat = findViewById(R.id.rcvChat);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        btnSend.setEnabled(false);
        btnSend.setAlpha(0.5f);
        messageEditText = findViewById(R.id.messageEditText);
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    btnSend.setEnabled(true);
                    btnSend.setAlpha(1f);
                } else {
                    btnSend.setEnabled(false);
                    btnSend.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        initChatRcv();
    }


    @Override
    public void onPause() {
        if (chatAdapter != null) {
            chatAdapter.stopListening();
        }

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (chatAdapter != null) {
            chatAdapter.startListening();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu1) {
        super.onCreateOptionsMenu(menu1);
        menu = menu1;
        setToolbarEditIcon(false, 0);
        setToolbarCameraIcon(false);
        return true;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                sendMessage();
                break;
            default:
                // do with default
                break;
        }

    }

    private void initChatRcv() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        SnapshotParser<Message> parser = new SnapshotParser<Message>() {
            @Override
            public Message parseSnapshot(DataSnapshot dataSnapshot) {
                Message chatMessage = dataSnapshot.getValue(Message.class);
                return chatMessage;
            }
        };


        DatabaseReference messagesRef = firebaseDatabaseReference.child(MESSAGES_CHILD).child(CHAT_TYPE);
        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(messagesRef, parser)
                        .build();

        chatAdapter = new ChatAdapter(this, options);
        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = chatAdapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager
                        .findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of
                // the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition ==
                                (positionStart - 1))) {
                    rcvChat.scrollToPosition(positionStart);
                } else {
                    rcvChat.scrollToPosition(friendlyMessageCount - 1);
                }
            }
        });

        rcvChat.setLayoutManager(linearLayoutManager);
        rcvChat.setAdapter(chatAdapter);


    }

    private void initFirebaseChat() {
        webFormat = new SimpleDateFormat(Constant.DATE_TIME_FORMAT_WEB, Locale.US);
        webFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void setAsReadMessage(String id) {
        DatabaseReference chatMessage = firebaseDatabaseReference.child(MESSAGES_CHILD).child(CHAT_TYPE).child(id)
                .child("is_read");
        chatMessage.setValue(true);
    }

    private void sendMessage() {
        if (firebaseDatabaseReference != null) {
            String key = firebaseDatabaseReference.child(MESSAGES_CHILD).child(CHAT_TYPE).push().getKey();
            if (!TextUtils.isEmpty(key)) {
                Message chatMessage = new Message(key, Integer.parseInt(CHAT_TYPE),
                        messageEditText.getText()
                                .toString().trim(), webFormat.format(new
                        Date()), Constant.STORE_CHAT_ID, receiver_id, false);
                Utilities.printLog(ChatActivity.class.getSimpleName(), chatMessage + "");
                firebaseDatabaseReference.child(MESSAGES_CHILD).child(CHAT_TYPE).child(key).setValue(chatMessage);
                messageEditText.setText("");
            }
        }
    }
}
