package com.example.akil.resistance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddWarrior extends AppCompatActivity{

    private static String[] species={"<Don't Know>","Human","Mammalian","Reptilian","Avian","Craniopod","Droid","Humanoid"};
    private static String[] planets={"<Don't Know>","Alderaan","Bespin","Coruscant","Dagobah","Endor","Geonosis","Hoth","Kamino",
            "Mustafar","Naboo","Tatooine","Utapau","Yavin"};

    private static Spinner spinner_species,spinner_planets;

    ArrayAdapter<String> adapter_species,adapter_planets;

    private static MyDBHandler dbHandler;

    String imgpath_value="";

    ImageView imageView_contactpic;

    int wid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_warrior);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState!=null)
            imgpath_value=savedInstanceState.getString("imgpath");

        spinner_species=(Spinner)findViewById(R.id.spinner_species);
        spinner_planets=(Spinner)findViewById(R.id.spinner_planets);

        adapter_species=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,species);
        adapter_species.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_species.setAdapter(adapter_species);

        adapter_planets=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,planets);
        adapter_planets.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_planets.setAdapter(adapter_planets);

        dbHandler=new MyDBHandler(this,null,null,1);

        imageView_contactpic=(ImageView)findViewById(R.id.imageView_contactpic);
        pictureSetter(imgpath_value,imageView_contactpic,this);

        Button button_changepicture=(Button)findViewById(R.id.button_changepicture);
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
            button_changepicture.setEnabled(false);

        Intent intent=getIntent();
        wid=intent.getIntExtra("edit_warrior_id",-1);
        if(wid!=-1) {
            if(getSupportActionBar()!=null)
                getSupportActionBar().setTitle("Edit Warrior");
            editWarrior(wid);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("imgpath", imgpath_value);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imgpath_value=savedInstanceState.getString("imgpath");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddWarrior.this, MainScreen.class));
        finish();
    }

    public static void pictureSetter(String imgpath, final ImageView imageView,Context context){

       if(!imgpath.equals(""))
           Picasso.with(context).load(imgpath).fit().centerCrop().error(R.drawable.ic_contact_picture_180_holo_light).noFade().into(imageView);

        else
            imageView.setImageResource(R.drawable.ic_contact_picture_180_holo_light);

    }

    public int saveDetailsClicked(View view){

        RadioGroup radioGroup_affiliation=(RadioGroup)findViewById(R.id.radioGroup_affiliation);
        RadioGroup radioGroup_gender=(RadioGroup)findViewById(R.id.radioGroup_gender);

        String name_value=((EditText)findViewById(R.id.editText_name)).getText().toString();
        String contactnumber_value=((EditText)findViewById(R.id.editText_contactnumber)).getText().toString();
        String affiliation_value=((RadioButton)findViewById(radioGroup_affiliation.getCheckedRadioButtonId())).getText().toString();
        String species_value=spinner_species.getSelectedItem().toString();
        String gender_value=((RadioButton)findViewById(radioGroup_gender.getCheckedRadioButtonId())).getText().toString();
        String date_value=((EditText)findViewById(R.id.editText_date)).getText().toString();
        String planet_value=spinner_planets.getSelectedItem().toString();

        boolean noErrors=true;
        String toast_message="";
        int flag=0;
        int flag2=0;

        if(name_value.length()<=0 || name_value.charAt(0)==' '){
            toast_message+="Enter a valid name (name can't start with space)";
            flag++;
            noErrors=false;
            flag2=1;
        }
        name_value=name_value.trim();
        try{
            long num=Long.parseLong(contactnumber_value);
        }catch (NumberFormatException e) {
            if (flag > 0)
                toast_message += "\n";
            toast_message += "Enter a valid contact number (only digits)";
            flag++;
            noErrors = false;
            flag2=1;
        }


        if(wid!=-1){
            final Warriors warrioredit=dbHandler.warriorDetailsAsWarriorObject(wid);
            if(name_value.equalsIgnoreCase(warrioredit.get_name()) && contactnumber_value.equalsIgnoreCase(warrioredit.get_contactno()))
                flag2=1;
        }

        if(flag2 == 0 && dbHandler.iswarriorexist(name_value,contactnumber_value)){
            toast_message += "Warrior with the specified name and contact number already exists";
            noErrors=false;
            flag2=2;
        }

        if(flag2!=2) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                simpleDateFormat.setLenient(false);
                Date date = simpleDateFormat.parse(date_value);
                String dateString = date_value.substring(date_value.lastIndexOf("/") + 1, date_value.length());
                if (dateString.length() != 4)
                    throw new ParseException("Invalid date- Mention 4 digits in year", 0);
          /*  String date2=simpleDateFormat.format(date);
            if(!date2.equals(date_value))
                throw new ParseException("Invalid date- dd/mm/yyyy format not followed",0); */
            } catch (ParseException e) {
                if (flag > 0)
                    toast_message += "\n";
                toast_message += "Enter a valid date (dd/mm/yyyy)";
                noErrors = false;
            }
        }
        if(!noErrors) {
             Toast toast_errors=Toast.makeText(AddWarrior.this, toast_message, Toast.LENGTH_LONG);
            toast_errors.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);
            View view_errors=toast_errors.getView();
            view_errors.setBackgroundColor(Color.parseColor("#990000"));
            TextView textView_errors=(TextView)view_errors.findViewById(android.R.id.message);
            textView_errors.setBackgroundColor(Color.parseColor("#990000"));
            toast_errors.show();

            return -1;
        }else{
            Warriors warrior = new Warriors(name_value, contactnumber_value, imgpath_value, affiliation_value, species_value, gender_value,
                    date_value, planet_value);
            int warriorID = dbHandler.addWarrior(warrior);

            Toast toast_noerrors=Toast.makeText(AddWarrior.this, "Warrior Details Saved  ", Toast.LENGTH_SHORT);
            View view_noerrors=toast_noerrors.getView();
            view_noerrors.setBackgroundColor(Color.parseColor("#006600"));
            TextView textView_noerrors=(TextView)view_noerrors.findViewById(android.R.id.message);
            textView_noerrors.setBackgroundColor(Color.parseColor("#006600"));
            toast_noerrors.show();

            Intent intent = new Intent(this, WarriorDetails.class);
            intent.putExtra("warrior_id", warriorID);
            startActivity(intent);

            return 0;

        }

    }

    public void changeClicked(View view){

        final String[] options={"Choose Photo","Take Photo"};

        new AlertDialog.Builder(AddWarrior.this)
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                       switch (which){
                            case 0:Intent intent_choosepic=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                   if(intent_choosepic.resolveActivity(getPackageManager())!=null)
                                        startActivityForResult(intent_choosepic,10);
                                   break;
                            case 1:Intent intent_takepic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                   if(intent_takepic.resolveActivity(getPackageManager())!=null) {

                                       Uri photouri=getOutputMediaFileUri();
                                       intent_takepic.putExtra(MediaStore.EXTRA_OUTPUT,photouri);
                                       imgpath_value=photouri.toString();

                                       startActivityForResult(intent_takepic, 20);
                                   }
                        }
                    }
                })
                .show();

    }

    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }
    private static File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Resistance- Warrior Pictures");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Resistance Media", "Failed To Create Directory");
                return null;
            }
        }
        File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10 && resultCode == RESULT_OK) {
            try {
                imgpath_value = data.getData().toString();
                pictureSetter(imgpath_value, imageView_contactpic, getApplicationContext());
            } catch (Exception e) {
                new AlertDialog.Builder(AddWarrior.this)
                        .setMessage("File Not Found!")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                imgpath_value="";
            }
        } else if (requestCode == 20 && resultCode == RESULT_OK) {

            try {
                pictureSetter(imgpath_value,imageView_contactpic,getApplicationContext());
            }catch (Exception e){
                new AlertDialog.Builder(AddWarrior.this)
                        .setMessage("Error Loading Image!")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                imgpath_value="";
            }

        }

    }

    public void editWarrior(final int warrior_id){

        final Warriors warrior=dbHandler.warriorDetailsAsWarriorObject(warrior_id);

        EditText editText_name=(EditText)findViewById(R.id.editText_name);
        EditText editText_contactnumber=(EditText)findViewById(R.id.editText_contactnumber);
        EditText editText_date=(EditText)findViewById(R.id.editText_date);
        RadioGroup radioGroup_affiliation=(RadioGroup)findViewById(R.id.radioGroup_affiliation);
        RadioGroup radioGroup_gender=(RadioGroup)findViewById(R.id.radioGroup_gender);
        ImageView imageView_contactpic=(ImageView)findViewById(R.id.imageView_contactpic);

        imgpath_value=warrior.get_imgpath();

        editText_name.setText(warrior.get_name());
        editText_contactnumber.setText(warrior.get_contactno());
        editText_date.setText(warrior.get_date());

        pictureSetter(imgpath_value,imageView_contactpic,this);

        if(warrior.get_affiliation().equalsIgnoreCase("Light Side"))
            radioGroup_affiliation.check(R.id.radioButton_lightside);
        else if(warrior.get_affiliation().equalsIgnoreCase("Dark Side"))
            radioGroup_affiliation.check(R.id.radioButton_darkside);

        if(warrior.get_gender().equalsIgnoreCase("Male"))
            radioGroup_gender.check(R.id.radioButton_male);
        else if(warrior.get_gender().equalsIgnoreCase("Female"))
            radioGroup_gender.check(R.id.radioButton_female);
        else if(warrior.get_gender().equalsIgnoreCase("Machine"))
            radioGroup_gender.check(R.id.radioButton_machine);

        for(int i=0;i<species.length;i++) {
            if (warrior.get_species().equalsIgnoreCase(species[i])) {
                spinner_species.setSelection(adapter_species.getPosition(species[i]));
                break;
            }
        }

        for(int i=0;i<planets.length;i++) {
            if (warrior.get_planet().equalsIgnoreCase(planets[i])) {
                spinner_planets.setSelection(adapter_planets.getPosition(planets[i]));
                break;
            }
        }

        Button button_save=(Button)findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n=saveDetailsClicked(v);
                if(n==0)
                    dbHandler.deleteWarrior(warrior_id);
            }
        });

    }

}
