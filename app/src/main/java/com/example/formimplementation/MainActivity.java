package com.example.formimplementation;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText answer,count;
    EditText desc;
    EditText question;
    Spinner dropdown;
    TextView t2;
    String v1,v2,v3,v4;
    Button k;
    Button done;
    String label;
    List<EditText> allEDs = new ArrayList<EditText>();
    List<EditText> all=new ArrayList<EditText>();
    int x;
    final ArrayList<String>  aMap=new ArrayList<>();
    String[] ar=new String[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createform);
        Bundle  bundle=getIntent().getExtras();
        label=bundle.getString("email");
        desc=findViewById(R.id.editText2);
        dropdown = findViewById(R.id.spinner1);
        question = findViewById(R.id.question);
        count=findViewById(R.id.count);
        done=findViewById(R.id.done);
        t2 = findViewById(R.id.textView);
        answer = findViewById(R.id.answer);
        k = findViewById(R.id.addqb);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ViewQuestions.class);
                Bundle bundle=new Bundle();
                bundle.putString("email",label);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        String[] items = new String[]{"Integer", "Real Number", "Short Answer", "CheckBox", "Multiple Choice"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        dropdown.setOnItemSelectedListener(MainActivity.this);
        v1 = question.getText().toString();


        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
               // v1="helldddddd;";
               // v2="sdssddssddsds";
                for(int h=0;h<x;h++)
                {
                    aMap.add(allEDs.get(h).getText().toString());
                }

                params.put("label",label);
                params.put("description",desc.getText().toString());
                params.put("Question",v1);
                params.put("AnswerType",v2);
                params.put("numberofanswertype",v3);

               //Gson gson = new GsonBuilder().create();
                //JsonArray myCustomArray = gson.toJsonTree(aMap).getAsJsonArray();
               params.put("inputs",aMap);
              // params.put("inputs","2");
               //params.put("inputs","3");

                WebserverClient.post("api/mongo/", params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        t2.setText("Failure"+responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        t2.setText("Success"+responseString);
                        Toast.makeText(MainActivity.this, "Question Added Successfully", Toast.LENGTH_SHORT).show();
                        question.setText("");
                    }
                });

               /* WebserverClient.get("api/mongo", params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                        t2.setText(responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        t2.setText("Upload success" + responseString);


                    }
                });*/
                //System.out.println(i[0]);


            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                answer.setVisibility(View.INVISIBLE);
                answer.setInputType(InputType.TYPE_CLASS_NUMBER);
                count.setVisibility(View.INVISIBLE);
                v2 = dropdown.getItemAtPosition(0).toString();
                break;
            case 1:
                answer.setVisibility(View.INVISIBLE);
                count.setVisibility(View.INVISIBLE);
                answer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                v2 = dropdown.getItemAtPosition(1).toString();
                break;

            // Whatever you want to happen when the second item gets selected

            case 2:
                answer.setVisibility(View.INVISIBLE);
                count.setVisibility(View.INVISIBLE);
                v2 = dropdown.getItemAtPosition(2).toString();
                count.requestFocus();

                // Whatever you want to happen when the thrid item gets selected
                break;
            case 3:
                answer.setVisibility(View.INVISIBLE);
                count.setText(null);
                //v3 = "4";
                count.setVisibility(View.VISIBLE);
                v1=question.getText().toString();
                v2 = dropdown.getItemAtPosition(3).toString();
                count.requestFocus();
                //final int x;



                count.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus)
                        {
                            v3 = count.getText().toString();
                        x =Integer.parseInt(count.getText().toString());

                        LinearLayout ll = (findViewById(R.id.cform));


                        for (int j = 0; j < x; j++) {


                            final EditText et = new EditText(MainActivity.this);
                            et.setHint("Enter your choice " + j);
                            et.setId(j);
                            ll.addView(et);
                            allEDs.add(et);

                        }





                            //if (j == i[0])
                            //break;

                            // consume.
                            // }


                        }

                    }
                });


                // Whatever you want to happen when the thrid item gets selected
              break;
          case 4:
              answer.setVisibility(View.INVISIBLE);
              count.setText(null);
              //v3 = "4";
              count.setVisibility(View.VISIBLE);
              v1=question.getText().toString();
              v2 = dropdown.getItemAtPosition(4).toString();
              count.requestFocus();
              //final int x;



              count.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                  @Override
                  public void onFocusChange(View v, boolean hasFocus) {
                      if (!hasFocus) {
                          v3 = count.getText().toString();
                          x = Integer.parseInt(count.getText().toString());

                          LinearLayout ll = (findViewById(R.id.cform));


                          for (int j = 0; j < x; j++) {


                              final EditText et = new EditText(MainActivity.this);
                              et.setHint("Enter your choice " + j);
                              et.setId(j);
                              ll.addView(et);
                              allEDs.add(et);

                          }
                      }
                  }
              });


                // Whatever you want to happen when the thrid item gets selected
                break;


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
