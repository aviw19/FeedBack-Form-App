package com.example.formimplementation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.params.ClientPNames;


public class ViewQuestions extends AppCompatActivity {
    TextView res, question1, fname, fdesc;
    ViewFlipper viewFlipper;

    GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewquestions);
        res = findViewById(R.id.res);
        fname = findViewById(R.id.fname);
        fdesc = findViewById(R.id.fdesc);
        viewFlipper= findViewById(R.id.viewflippers);
        final String em;

        Bundle bundle = getIntent().getExtras();
        // em=bundle.getString("email");

        final int id = bundle.getInt("id");
        RequestParams params = new RequestParams();




        //AsyncHttpClient async_client = new AsyncHttpClient();
        //async_client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        WebserverClient.get("api/mongo", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                res.setText(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String question = "";
                String answertype;
                res.setText(responseString);
                JSONArray jsonArray = null;
                int r = 0;
                try {
                    jsonArray = new JSONArray(responseString);
                    LinearLayout n = findViewById(R.id.container_destacdo);
                    LinearLayout placeHolder1 = findViewById(R.id.item);
                    LinearLayout placeHolder2 = findViewById(R.id.dd);
                    LinearLayout placeHolder3 = findViewById(R.id.shortanswer);
                    //LinearLayout placeHolder4 = findViewById(R.id.lcheckbox);
                    CardView cardView = findViewById(R.id.integercardview);
                    CardView cardView1 = findViewById(R.id.decimalcardview);
                    CardView cardView2 =findViewById(R.id.shortanswercardview);
                    CardView cardView3=findViewById(R.id.checkboxcardview);
                    LinearLayout cdview =findViewById(R.id.checkboxcardviewlinearlayout);
                    //CheckBox cd=findViewById(R.id.cpct);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        //getting json object from the json array
                        JSONObject obj = jsonArray.getJSONObject(i);
                        if (Integer.parseInt(obj.getString("formId")) == id) {
                            if (r == 0) {
                                fname.setText(obj.getString("description"));
                                fdesc.setText(obj.getString("input"));
                                r = 1;

                            }

                            if (obj.getString("AnswerType").equalsIgnoreCase("Integer")) {
                                res.setText("");

                                View v = getLayoutInflater().inflate(R.layout.integercardview, cardView);
                                question1 = v.findViewById(R.id.textView2);
                                question1.setText(obj.getString("Question"));
                                viewFlipper.addView(v);

                            }
                            if (obj.getString("AnswerType").equalsIgnoreCase("Real Number")) {
                                res.setText("");
                               // n.removeView(placeHolder1);
                                //res.setText("");

                                View v = getLayoutInflater().inflate(R.layout.decimalcardview, cardView1);
                                question1 = v.findViewById(R.id.qdecimal);
                                question1.setText(obj.getString("Question"));
                                viewFlipper.addView(v);
                            }
                            if (obj.getString("AnswerType").equalsIgnoreCase("Short Answer")) {
                                res.setText("");
                                //n.removeView(placeHolder1);
                                res.setText("");

                                View v = getLayoutInflater().inflate(R.layout.shortanswercardview, cardView2);
                                question1 = v.findViewById(R.id.qshortanswer);
                                question1.setText(obj.getString("Question"));
                                viewFlipper.addView(v);
                            }
                            if (obj.getString("AnswerType").equalsIgnoreCase("Checkbox")) {
                                res.setText("");
                                int x = 0;
                                if (obj.getString("numberofanswertype") != null)
                                    try {
                                        x = Integer.parseInt(obj.getString("numberofanswertype"));
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                //n.removeView(placeHolder1);
                                // n.removeView(placeHolder2);

                                CardView v = (CardView) getLayoutInflater().inflate(R.layout.checkboxcardview, cardView3);
                                    //LinearLayout  l= (LinearLayout) getLayoutInflater().inflate(R.layout.checkboxcardview,placeHolder4);
                                LinearLayout ll=v.findViewById(R.id.checkboxcardviewlinearlayout);
                                question1 = v.findViewById(R.id.qcheckbox);
                                question1.setText(obj.getString("Question"));

                                JSONArray aMap = obj.getJSONArray("inputs");
                                ArrayList<String> a = new ArrayList<>();
                                for (int j = 0; j < aMap.length(); j++) {
                                    a.add(aMap.getString(j));
                                }


                              LinearLayout l=new LinearLayout(ViewQuestions.this);
                              l.setOrientation(LinearLayout.VERTICAL);


                                if (x == a.size())
                                    for (int k = 0; k < x; k++) {


                                        CheckBox c=new CheckBox(ViewQuestions.this);
                                        c.setText(a.get(k));

                                        ll.addView(c);


                                    }
                                viewFlipper.addView(v);
                            }
                            if (obj.getString("AnswerType").equalsIgnoreCase("Multiple Choice")) {
                                res.setText("");
                                int x = 0;
                                if (obj.getString("numberofanswertype") != null)
                                    try {
                                        x = Integer.parseInt(obj.getString("numberofanswertype"));
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                //n.removeView(placeHolder1);
                                // n.removeView(placeHolder2);

                                CardView ct = (CardView) getLayoutInflater().inflate(R.layout.checkboxcardview, cardView3);
                                LinearLayout ll=ct.findViewById(R.id.checkboxcardviewlinearlayout);

                                question1 = ct.findViewById(R.id.qcheckbox);
                                question1.setText(obj.getString("Question"));

                                JSONArray aMap = obj.getJSONArray("inputs");
                                ArrayList<String> a = new ArrayList<>();
                                for (int j = 0; j < aMap.length(); j++) {
                                    a.add(aMap.getString(j));
                                }
                                //ll.removeAllViews();
                                RadioGroup rg = new RadioGroup(ViewQuestions.this);
                                if (x == a.size())
                                    for (int k = 0; k < x; k++) {
                                        //n.removeView(placeHolder1);
                                        //n.removeView(placeHolder2);
                                        //n.removeView(placeHolder3);
                                        RadioButton c = new RadioButton(ViewQuestions.this);
                                        c.setText(a.get(k));
                                        rg.addView(c);

                                        //n.removeView(rg);
                                        //n.addView(rg);
                                    }
                                ll.addView(rg);
                                viewFlipper.addView(ct);
                            }
                        }
                        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
                        //GestureDetector = new GestureDetector(ViewQuestions.this, customGestureDetector);
                        mGestureDetector=new GestureDetector(ViewQuestions.this,customGestureDetector);
                        //viewFlipper.startFlipping();
                        //getting the name from the json object and putting it inside string array
                    }




                } catch (JSONException e1) {
                    e1.printStackTrace();

                }
            }

        });

    }

     class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
         @Override
         public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

             // Swipe left (next)
             if (e1.getX() > e2.getX()) {
                 viewFlipper.showNext();
             }
             // Swipe right (previous)
             if (e1.getX() < e2.getX()) {
                 viewFlipper.showPrevious();
             }

             return super.onFling(e1, e2, velocityX, velocityY);
         }
     }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent e)
    {
        super.dispatchTouchEvent(e);
        return mGestureDetector.onTouchEvent(e);
    }

}

