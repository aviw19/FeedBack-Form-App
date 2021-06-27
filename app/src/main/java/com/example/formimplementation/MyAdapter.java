package com.example.formimplementation;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>  {
    private ArrayList<DataSet> mDataset;

   

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public CardView cardView;
        public TextView textViewName;
        public TextView textViewData;

        /*public MyViewHolder(TextView v) {
            super(v);
            textView = v;
        }*/

        public MyViewHolder(CardView v)
        {
            super(v);
            this.textViewName = (TextView) v.findViewById(R.id.formname);
            this.textViewData=(TextView) v.findViewById(R.id.formdesc);

                cardView=v;


        }

    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<DataSet> myDataset) {
        mDataset = myDataset;


    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
       /* TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_textview, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;*/
      CardView v= (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewitem,parent,false);

        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView textViewName=holder.textViewName;
        TextView textViewData=holder.textViewData;
        textViewName.setText(mDataset.get(position).getS());
        textViewData.setText(mDataset.get(position).getS2());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }



}
