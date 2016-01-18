package com.example.akil.resistance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


public class WarriorDetails extends AppCompatActivity {

    private int warrior_id;
    private static MyDBHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warrior_details);

        Intent intent=getIntent();
        warrior_id=intent.getIntExtra("warrior_id",-1);

        handler=new MyDBHandler(this,null,null,1);

        Warriors warrior=handler.warriorDetailsAsWarriorObject(warrior_id);

        TextView textView_namedetails=(TextView)findViewById(R.id.textView_namedetails);
        TextView textView_phonenumdetails=(TextView)findViewById(R.id.textView_phonenumdetails);
        TextView textView_aboutwarrior=(TextView)findViewById(R.id.textView_aboutwarrior);
        TextView textView_affiliationdetails=(TextView)findViewById(R.id.textView_affiliationdetails);
        TextView textView_speciesdetails=(TextView)findViewById(R.id.textView_speciesdetails);
        TextView textView_genderdetails=(TextView)findViewById(R.id.textView_genderdetails);
        TextView textView_datedetails=(TextView)findViewById(R.id.textView_datedetails);
        TextView textView_planetdetails=(TextView)findViewById(R.id.textView_planetdetails);

        ImageView imageView_phoneicondetails=(ImageView)findViewById(R.id.imageView_phoneicondetails);
        ImageView imageView_picdetails=(ImageView)findViewById(R.id.imageView_picdetails);

        imageView_phoneicondetails.setImageResource(android.R.drawable.ic_menu_call);

        AddWarrior.pictureSetter(warrior.get_imgpath(),imageView_picdetails,this);

        if(warrior.get_affiliation().equalsIgnoreCase("Light Side")){
            textView_aboutwarrior.setTextColor(Color.parseColor("#42A5F5"));
            imageView_phoneicondetails.setColorFilter(Color.parseColor("#42A5F5"));
            if(warrior.get_imgpath().equals(""))
                imageView_picdetails.setColorFilter(Color.parseColor("#42A5F5"), PorterDuff.Mode.MULTIPLY);
        }else if(warrior.get_affiliation().equalsIgnoreCase("Dark Side")){
            textView_aboutwarrior.setTextColor(Color.parseColor("#EF5350"));
            imageView_phoneicondetails.setColorFilter(Color.parseColor("#EF5350"));
            if(warrior.get_imgpath().equals(""))
                imageView_picdetails.setColorFilter(Color.parseColor("#EF5350"), PorterDuff.Mode.MULTIPLY);
        }

        textView_namedetails.setText(warrior.get_name());
        textView_phonenumdetails.setText(warrior.get_contactno());
        textView_aboutwarrior.setText("About "+warrior.get_name());
        textView_affiliationdetails.setText("Affiliation : "+warrior.get_affiliation());
        textView_speciesdetails.setText("Species : "+warrior.get_species());
        textView_genderdetails.setText("Gender : "+warrior.get_gender());
        textView_datedetails.setText("Last Spotted On : "+warrior.get_date());
        textView_planetdetails.setText("Last Known Presence : "+warrior.get_planet());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WarriorDetails.this,WarriorList.class));
        finish();
    }

    public void editWarriorClicked(View view){

        Intent intent=new Intent(WarriorDetails.this,AddWarrior.class);
        intent.putExtra("edit_warrior_id",warrior_id);
        startActivity(intent);

    }

    public void deleteWarriorClicked(View view){

        new AlertDialog.Builder(WarriorDetails.this)
                .setTitle("Delete Warrior")
                .setMessage("Are you sure you want to delete this warrior?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.deleteWarrior(warrior_id);
                        Toast toast_wardel=Toast.makeText(WarriorDetails.this,"Warrior Deleted  ",Toast.LENGTH_SHORT);
                        View view_wardel=toast_wardel.getView();
                        view_wardel.setBackgroundColor(Color.parseColor("#DAA520"));
                        TextView textView_wardel=(TextView)view_wardel.findViewById(android.R.id.message);
                        textView_wardel.setBackgroundColor(Color.parseColor("#DAA520"));
                        toast_wardel.show();
                        Intent intent=new Intent(WarriorDetails.this,WarriorList.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void sendInfoClicked(View view){

        String msg=handler.warriorDetailsToString(warrior_id);
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        if(intent.resolveActivity(getPackageManager())!=null)
            startActivity(intent);

    }

}
