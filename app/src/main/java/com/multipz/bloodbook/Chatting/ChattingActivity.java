package com.multipz.bloodbook.Chatting;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.multipz.bloodbook.Adapter.ChattingAdapter;
import com.multipz.bloodbook.Model.ChatMessage;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.Config;
import com.multipz.bloodbook.Utils.ItemClickListener;
import com.multipz.bloodbook.Utils.Shared;
import com.multipz.bloodbook.Utils.SocketManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.emitter.Emitter;

public class ChattingActivity extends Activity implements Emitter.Listener, ItemClickListener {
    private EditText messageET;
    private RecyclerView messagesContainer;
    private ImageView sendBtn;
    private ChattingAdapter chattingAdapter;
    private SocketManager socketManager;
    private String msgData, reqid, image, username, BloodReqID;
    private ArrayList<ChatMessage> chatHistory;
    private Context context;
    private Shared shared;
    private ImageView back;
    private CircleImageView img_profile;
    private TextView txt_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        context = this;
        shared = new Shared(context);
        chatHistory = new ArrayList<>();
        ref();
        Init();


    }

    private void ref() {
        socketManager = SocketManager.getInstance(getApplicationContext());
        sendBtn = (ImageView) findViewById(R.id.chatSendButton);
        messagesContainer = (RecyclerView) findViewById(R.id.messagesContainer);
        back = (ImageView) findViewById(R.id.back);
        txt_username = (TextView) findViewById(R.id.txt_username);
        img_profile = (CircleImageView) findViewById(R.id.img_profile);
        messageET = (EditText) findViewById(R.id.messageEdit);

        reqid = getIntent().getStringExtra("reqid");
        BloodReqID = getIntent().getStringExtra("BloodReqID");
        image = getIntent().getStringExtra("img");
        Glide.with(context).load(image).into(img_profile);
        username = getIntent().getStringExtra("username");
        txt_username.setText(username);

        socketManager.connect();
        SocketConnection();
        getChatMsg();
        scroll();
    }

    private void Init() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageET.getText().toString().equals("")) {
                    Toast.makeText(ChattingActivity.this, "please enter msg", Toast.LENGTH_SHORT).show();
                } else {
                    msgData = messageET.getText().toString();
                    // displayMessage(msgData);
                    SendMsg(msgData);
                    messageET.setText("");

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void SocketConnection() {
        final JSONObject objdata = new JSONObject();
        try {
            objdata.put("UserId", shared.getUserId());
            objdata.put("BloodRequestId", BloodReqID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put("action", "Connection");
            obj.put("data", objdata);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        socketManager.sendEmit("request", obj);
    }

    @Override
    public void call(final Object... args) {
        ChattingActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject data = (JSONObject) args[0];
                String res, sender_id, msg, action, type, receiver_id, typeuserid, typesendid, typestatus;
                try {
                    action = data.getString("action");
                    res = data.getString("status");

                    if (action.equals("GetChatMsg")) {
                        if (res.matches("1")) {
                            JSONArray arr = data.getJSONArray("data");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject object = arr.getJSONObject(i);
                                sender_id = object.getString("sender_id");
                                msg = object.getString("msg");
//                                type = object.getString("type");
                                ChatMessage chatMessage = new ChatMessage();
                                chatMessage.setUserId(sender_id);
                                chatMessage.setMessage(msg);
//                                chatMessage.setMsgType(type);
                                chatHistory.add(chatMessage);
                            }
                            chattingAdapter = new ChattingAdapter(getApplicationContext(), chatHistory);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            messagesContainer.setLayoutManager(layoutManager);
                            messagesContainer.setAdapter(chattingAdapter);
                            scroll();
                        }
                    } else if (action.equals("SendMsg")) {
                        if (res.matches("1")) {
                            chatHistory.clear();
                            JSONArray arr = data.getJSONArray("data");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject object = arr.getJSONObject(i);
                                sender_id = object.getString("sender_id");
                                receiver_id = object.getString("receiver_id");
                                msg = object.getString("msg");
//                                    type = object.getString("type");
                                ChatMessage chatMessage = new ChatMessage();
                                chatMessage.setUserId(sender_id);
                                chatMessage.setReceiver_id(receiver_id);
                                chatMessage.setMessage(msg);
//                                    chatMessage.setMsgType(type);
                                chatHistory.add(chatMessage);
                            }
                        } else if (res.matches("0")) {
                            JSONObject object1 = data.getJSONObject("data");
                            sender_id = object1.getString("sender_id");
                            receiver_id = object1.getString("receiver_id");
                            msg = object1.getString("msg");

                            ChatMessage chatMessage = new ChatMessage();
                            chatMessage.setUserId(sender_id);
                            chatMessage.setReceiver_id(receiver_id);
                            chatMessage.setMessage(msg);
                            chatHistory.add(chatMessage);
                        }
                        chattingAdapter = new ChattingAdapter(getApplicationContext(), chatHistory);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        messagesContainer.setLayoutManager(layoutManager);
                        messagesContainer.setAdapter(chattingAdapter);
                        scroll();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void SendMsg(String msg) {
        // send userdata
        final JSONObject objdata = new JSONObject();
        final JSONObject obj = new JSONObject();

        try {
            objdata.put("UserId", shared.getUserId());
            objdata.put("receiverid", reqid);
            objdata.put("BloodRequestId", BloodReqID);
            objdata.put("msg", msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("action", "SendMsg");
            obj.put("data", objdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        socketManager.sendEmit("request", obj);
        socketManager.addListener("response", this);

    }

    private void getChatMsg() {
        // send userdata
        final JSONObject objdata = new JSONObject();
        try {
            objdata.put("UserId", shared.getUserId());
            objdata.put("receiverid", reqid);
            objdata.put("BloodRequestId", BloodReqID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject obj = new JSONObject();
        try {
            obj.put("action", "GetChatMsg");
            obj.put("data", objdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socketManager.sendEmit("request", obj);
        socketManager.addListener("response", this);
    }

    @Override
    public void itemClicked(View View, int position) {

    }

    private void scroll() {
        messagesContainer.scrollToPosition(chatHistory.size() - 1);
    }


    public void displayMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (chatHistory != null) {
                    if (chatHistory.size() > 0) {
                        ChatMessage m = new ChatMessage();
                        m.setMessage(message);
//                        m.setMsgType("Msg");
                        m.setUserId(shared.getUserId());
                        chatHistory.add(0, m);
                        //chattingAdapter.notifyItemInserted(chatHistory.size() - 1);
                        chattingAdapter.notifyDataSetChanged();
                        scroll();
                    } else {
                        ChatMessage m = new ChatMessage();
                        m.setMessage(message);
//                        m.setMsgType("Msg");
                        m.setUserId(shared.getUserId());
                        chatHistory.add(m);
                        chattingAdapter = new ChattingAdapter(getApplicationContext(), chatHistory);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        messagesContainer.setLayoutManager(layoutManager);
                        messagesContainer.setAdapter(chattingAdapter);
                        chattingAdapter.notifyDataSetChanged();
                        scroll();
                    }
                }
            }
        });
    }


}
