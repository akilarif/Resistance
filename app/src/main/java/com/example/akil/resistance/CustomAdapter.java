package com.example.akil.resistance;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CustomAdapter extends ArrayAdapter<Warriors> {

    CustomAdapter(Context context,Warriors[] warriors) {
        super(context,R.layout.adapter_customrow,warriors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View customView=layoutInflater.inflate(R.layout.adapter_customrow, parent, false);

        Warriors warrior=getItem(position);

        TextView textView_contactnameinlist=(TextView)customView.findViewById(R.id.textView_contactnameinlist);
        ImageView imageView_contactpicsmall=(ImageView)customView.findViewById(R.id.imageView_contactpicsmall);
        LinearLayout linearlayout_list=(LinearLayout)customView.findViewById(R.id.linearlayout_list);

        textView_contactnameinlist.setText(warrior.get_name());

        AddWarrior.pictureSetter(warrior.get_imgpath(),imageView_contactpicsmall,getContext());

        TextView textView_idlist=(TextView)customView.findViewById(R.id.textView_idlist);
        textView_idlist.setText(Integer.toString(warrior.get_id()));

        if(warrior.get_affiliation().equalsIgnoreCase("Light Side"))
            linearlayout_list.setBackgroundColor(Color.parseColor("#42A5F5"));
        else if(warrior.get_affiliation().equalsIgnoreCase("Dark Side"))
            linearlayout_list.setBackgroundColor(Color.parseColor("#EF5350"));

        return customView;

    }
}
