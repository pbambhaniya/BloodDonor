package com.multipz.bloodbook.Chatting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.multipz.bloodbook.Adapter.ChatListAdapter;
import com.multipz.bloodbook.Model.ChatListModelClass;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.ItemClickListener;
import com.multipz.bloodbook.Utils.Shared;
import com.multipz.bloodbook.Utils.SocketManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class ChatListActivity extends AppCompatActivity implements ItemClickListener, Emitter.Listener {
    RecyclerView chatlistview;
    private ArrayList<ChatListModelClass> chatList;
    private ChatListAdapter chatListAdapter;
    LinearLayoutManager layoutManager;
    private SocketManager socketManager;
    private Button btnSend;
    private Context context;
    Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        context = this;
        shared = new Shared(context);
        chatList = new ArrayList<>();
        socketManager = SocketManager.getInstance(getApplicationContext());
        reference();
        init();

    }

    private void init() {
        socketManager.connect();
        SocketConnection();
        getChatUserList();
    }

    private void SocketConnection() {
        final JSONObject objdata = new JSONObject();
        try {
            objdata.put("userid", shared.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put("action", "Connection");
            obj.put("data", objdata);
            Log.d("str", "" + obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socketManager.sendEmit("request", obj);
    }


    private void getChatUserList() {
        // send userdata
        final JSONObject objdata = new JSONObject();
        try {
            objdata.put("userid", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject obj = new JSONObject();
        try {
            obj.put("action", "GetChatUserList");
            obj.put("data", objdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socketManager.sendEmit("request", obj);
        socketManager.addListener("response", this);
    }


    private void reference() {
        chatlistview = (RecyclerView) findViewById(R.id.chatlistview);
        btnSend = (Button) findViewById(R.id.btnSend);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void itemClicked(View View, int position) {
        try {
            ChatListModelClass m = chatList.get(position);
            String recevierID = m.getUser_id();
            String name = m.getName();
            Intent i = new Intent(ChatListActivity.this, ChattingActivity.class);
            i.putExtra("name", name);
            i.putExtra("recevierID", recevierID);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void call(final Object... args) {
        ChatListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject data = (JSONObject) args[0];
                String res, user_id, name, img, phone_no, msg_status, login_status, create_date, update_date, action, typeuserid, typesendid, typestatus;
                try {
                    action = data.getString("action");
                    res = data.getString("status");
                    if (action.equals("GetChatUserList")) {
                        if (res.matches("1")) {
                            JSONArray array = data.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                user_id = object.getString("user_id");
                                name = object.getString("name");
                                img = object.getString("img");
                                phone_no = object.getString("phone_no");
                                msg_status = object.getString("msg_status");
                                login_status = object.getString("login_status");
                                create_date = object.getString("create_date");
                                update_date = object.getString("update_date");

                                ChatListModelClass m = new ChatListModelClass();
                                m.setUser_id(user_id);
                                m.setName(name);
                                m.setImg(img);
                                m.setPhone_no(phone_no);
                                m.setMsg_status(msg_status);
                                m.setLogin_status(login_status);
                                m.setCreate_date(create_date);
                                m.setUpdate_date(update_date);
                                chatList.add(m);
                            }
                            chatListAdapter = new ChatListAdapter(getApplicationContext(), chatList);
                            layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            chatlistview.setLayoutManager(layoutManager);
                            chatlistview.setAdapter(chatListAdapter);
                            chatListAdapter.setClickListener(ChatListActivity.this);
                        }
                    } /*else if (action.matches("Typing")) {
                        JSONObject jsonObject = data.getJSONObject("data");
                        typeuserid = jsonObject.getString("typeuserid");
                        typesendid = jsonObject.getString("typesendid");
                        typestatus = jsonObject.getString("typestatus");
                        ChatListModelClass m = new ChatListModelClass();
                        m.setTypeuserid(typeuserid);
                        m.setTypesendid(typesendid);
                        m.setTypestatus(typestatus);

                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}