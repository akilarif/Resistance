package com.example.akil.resistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class WarriorList extends AppCompatActivity {

    private static MyDBHandler myDBHandler;
    ListView listView_warriorlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warrior_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDBHandler=new MyDBHandler(this,null,null,1);
        Warriors[] warriors=myDBHandler.warriorsArrayGetter();

        ListAdapter adapter = new CustomAdapter(this, warriors);
        listView_warriorlist = (ListView) findViewById(R.id.listView_warriorlist);
        listView_warriorlist.setAdapter(adapter);
        TextView textView_emptylist=(TextView)findViewById(R.id.textView_emptylist);
        listView_warriorlist.setEmptyView(textView_emptylist);

        listView_warriorlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textView=(TextView)view.findViewById(R.id.textView_idlist);
                int warriorID=Integer.parseInt(textView.getText().toString());

                Intent intent=new Intent(WarriorList.this,WarriorDetails.class);
                intent.putExtra("warrior_id",warriorID);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WarriorList.this,MainScreen.class));
        finish();
    }
}
