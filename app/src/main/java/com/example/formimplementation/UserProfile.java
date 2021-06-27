package com.example.formimplementation;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class UserProfile extends AppCompatActivity {
    TextView namet,emailt;
   // TextView res;
    Button Signout;
    Button cont;
    ImageView I;
    GoogleSignInClient mGoogleSignInClient;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);
        cont=findViewById(R.id.cont);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // final String[] mDataset={"Avi","Unnati","Srijan","Sonia"};


        //res=findViewById(R.id.info_text);
        retreive();
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserProfile.this,MainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("email",emailt.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        Signout=findViewById(R.id.button2);
        Signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                signOutq();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mGoogleSignInClient = GoogleSignIn.getClient(UserProfile.this, gso);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
        final LinearLayout linearLayout=findViewById(R.id.user);
        RequestParams  params=new RequestParams();
        final ArrayList<DataSet> mDataSet =new ArrayList<>();
        WebserverClient.get("api/mongo",params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                       // res.setText(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                //res.setText(responseString);
                JSONArray jsonArray;
                int j=10001;
                int k=1;
                try {
                    jsonArray = new JSONArray(responseString);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        if(obj.getString("label").equalsIgnoreCase(emailt.getText().toString()));
                        {


                                if (j == Integer.parseInt(obj.getString("formId"))) {
                                    mDataSet.add(new DataSet(obj.getString("description"), (obj.getString("input")), Integer.parseInt(obj.getString("formId"))));
                                    //if (k == jsonArray.length())

                                    j = j + 1;
                                }


                        }


                    }
                    mAdapter = new MyAdapter(mDataSet);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(UserProfile.this, recyclerView, new ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Intent intent=new Intent(UserProfile.this,ViewQuestions.class);
                            Bundle bunlde=new Bundle();
                            bunlde.putInt("id",mDataSet.get(position).getId());
                            intent.putExtras(bunlde);
                            startActivity(intent);



                        }

                        @Override
                        public void onLongClick(View view, int position) {
                            Intent intent=new Intent(UserProfile.this,ViewQuestions.class);
                            Bundle bunlde=new Bundle();
                            bunlde.putInt("id",mDataSet.get(position).getId());
                            intent.putExtras(bunlde);
                            startActivity(intent);
                        }
                    }));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void retreive() {
        I=findViewById(R.id.imageView);
        namet=(TextView)findViewById(R.id.textViewname);
        emailt=(TextView)findViewById(R.id.textViewemail);
        Intent intent=new Intent();
        Bundle bundle=getIntent().getExtras();
        String name=bundle.getString("name");
        String email=bundle.getString("email");
        namet.setText(name);
        emailt.setText(email);
        //Uri photoUrl = null;
        //photoUrl=Uri.parse(bundle.getString("photoUrl"));
        //Glide.with(UserProfile.this).load(photoUrl).into(I);

        // if(photoUrl!=null) {
        //    I.setImageURI(null);
        //  I.setImageURI(photoUrl);
        //}


    }
    private void signOutq() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(UserProfile.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        // ...
                        Intent i=new Intent(UserProfile.this,LoginMain.class);
                        startActivity(i);
                    }
                });
        Intent i=new Intent(UserProfile.this,LoginMain.class);
        startActivity(i);

    }
    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }



}
