package com.project.kalpawijesooriya.patient;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Chat extends AppCompatActivity {
    private Toolbar mToolbar;
    private String mchatUser;
    private TextView  mlasatSeenView;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference myrefdatabase;
    private DatabaseReference mRootref;
    private String currentUser;
    private ImageButton add,send;
    private EditText mmessege;
    private RecyclerView mMessagesList;
    private SwipeRefreshLayout mrefreshlayout;
    private final List<Messages>messageslist=new ArrayList<>();
    private LinearLayoutManager mLinnearLayout;
    private MessageAdapter mAdapter;
    private static final int TOTAL_ITEMS_TO_LOAD=10;
    private int mcurrentpage=1;
    private static String imageurl="";
    private int itempossition=0;
    private String last_mas="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mlasatSeenView=(TextView)findViewById(R.id.online);
        auth = FirebaseAuth.getInstance();
        add=(ImageButton)findViewById(R.id.add);
        send=(ImageButton)findViewById(R.id.send);
        mmessege=(EditText)findViewById(R.id.messege);
        mrefreshlayout=(SwipeRefreshLayout)findViewById(R.id.swipe_message);
        mAdapter=new MessageAdapter(messageslist);
        mMessagesList=(RecyclerView)findViewById(R.id.messages_list);
        mLinnearLayout=new LinearLayoutManager(this);
        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinnearLayout);

        mMessagesList.setAdapter(mAdapter);



        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(Chat.this, Login.class));
                    finish();
                }
            }
        };
        currentUser=auth.getCurrentUser().getUid();
        mToolbar=(Toolbar)findViewById(R.id.chat_page_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionbar=getSupportActionBar();

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowCustomEnabled(true);
       // actionbar.setTitle("Viweka Chat");
        myrefdatabase= FirebaseDatabase.getInstance().getReference("User").child("patient").child(auth.getCurrentUser().getUid());
        mRootref=FirebaseDatabase.getInstance().getReference();
        mchatUser="employee01";
        LayoutInflater inflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view=inflater.inflate(R.layout.chat_custom_bar,null);
        getSupportActionBar().setCustomView(action_bar_view);
        //actionbar.setTitle("Viweka Chat");

        mRootref.child("User").child("patient").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageurl=dataSnapshot.child("image").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        loadMessages();

        mRootref.child("User").child("employee").child("employee01").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String online=dataSnapshot.child("online").getValue().toString();
                if (online.equals("true"))
                {
                    mlasatSeenView.setText("Online");
                }
                else
                    mlasatSeenView.setText("Offline");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRootref.child("Chat").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(mchatUser))
                {
                    Map chatmap=new HashMap();
                    chatmap.put("seen",false);
                    chatmap.put("timestamp",ServerValue.TIMESTAMP);

                    Map chatUsermap=new HashMap();
                    chatUsermap.put("Chat/"+currentUser+"/"+"employee01",chatmap);
                    chatUsermap.put("Chat/"+"employee01"+"/"+currentUser,chatmap);
                    mRootref.updateChildren(chatUsermap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                              if(databaseError!=null)
                              {
                                  Log.d("CHAT_LOG",databaseError.getMessage().toString());
                              }
                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                            }
                });

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        sendMessege();
                        mmessege.setText("");
                }
             });

         mrefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                mcurrentpage++;
                itempossition=0;
                loadMoreMessages();
             }
         });
    }
   private void loadMoreMessages(){
       DatabaseReference messageRef=  mRootref.child("messages").child(currentUser).child( mchatUser);
       Query messageQuery=messageRef.orderByKey().endAt(last_mas).limitToLast(10);
       messageQuery.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               Messages message=dataSnapshot.getValue(Messages.class);
               messageslist.add(itempossition++,message);
               if (itempossition==1){
                   String messagekey=dataSnapshot.getKey();
                   last_mas=messagekey;

               }

               mAdapter.notifyDataSetChanged();

               mrefreshlayout.setRefreshing(false);
               mLinnearLayout.scrollToPositionWithOffset(10,0);
           }

           @Override
           public void onChildChanged(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onChildRemoved(DataSnapshot dataSnapshot) {

           }

           @Override
           public void onChildMoved(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
    }

    private void loadMessages() {

    DatabaseReference messageRef=  mRootref.child("messages").child(currentUser).child( mchatUser);
        Query messageQuery=messageRef.limitToLast(mcurrentpage* TOTAL_ITEMS_TO_LOAD);

        messageQuery.addChildEventListener(new ChildEventListener() {
      @Override
      public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              Messages message=dataSnapshot.getValue(Messages.class);

              itempossition++;

              if (itempossition==1){
                  String messagekey=dataSnapshot.getKey();
                  last_mas=messagekey;

              }
              messageslist.add(message);
              mAdapter.notifyDataSetChanged();

              mMessagesList.scrollToPosition(messageslist.size()-1);
              mrefreshlayout.setRefreshing(false);
      }

      @Override
      public void onChildChanged(DataSnapshot dataSnapshot, String s) {

      }

      @Override
      public void onChildRemoved(DataSnapshot dataSnapshot) {

      }

      @Override
      public void onChildMoved(DataSnapshot dataSnapshot, String s) {

      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
  });
    }


    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
        FirebaseUser currentuser=auth.getCurrentUser();
        if(currentuser==null)
        {


        }
        else{
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            myrefdatabase.child("online").setValue("true");
            }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
        FirebaseUser currentuser = auth.getCurrentUser();
        if (currentuser != null) {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            myrefdatabase.child("online").setValue(ServerValue.TIMESTAMP);
            myrefdatabase.child("LastSeen").setValue(ServerValue.TIMESTAMP);


        }
    }

    private void sendMessege()
    {
      String messege=  mmessege.getText().toString();
      if (!TextUtils.isEmpty(messege))
      {
          String cuttentuserref="messages/"+currentUser+"/"+mchatUser;
          String chatuserref="messages/"+mchatUser+"/"+currentUser;

          DatabaseReference user_messge_push=mRootref.child("messages").child(currentUser).child(mchatUser).push();
          String push_id=user_messge_push.getKey();

          HashMap messegemap=new HashMap();
          messegemap.put("message",messege);
          messegemap.put("seen",false);
          messegemap.put("type","text");
          messegemap.put("time",ServerValue.TIMESTAMP);
          messegemap.put("from",currentUser);
          messegemap.put("image",imageurl);


          Map messageusermap=new HashMap();
          messageusermap.put(cuttentuserref+"/"+push_id,messegemap);
          messageusermap.put(chatuserref+"/"+push_id,messegemap);

          mRootref.updateChildren(messageusermap, new DatabaseReference.CompletionListener() {
              @Override
              public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                  if(databaseError!=null)
                  {
                      Log.d("CHAT_LOG",databaseError.getMessage().toString());
                  }
              }
          });

      }
    }
}
