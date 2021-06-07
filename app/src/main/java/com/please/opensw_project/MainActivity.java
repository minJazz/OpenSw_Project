package com.please.opensw_project;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    EditText pName;
    Button btn_search;
    LinearLayout listv;



    String temp = new String("두");



    private DatabaseReference databaseReference;

    DataSnapshot list;
    HashMap<String, Object> pList = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("왜안되요?");
        pName = findViewById(R.id.product_search);
        btn_search = findViewById(R.id.btn_search);
        listv = findViewById(R.id.listv);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("productcodemap");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                for (DataSnapshot key : snapshot.getChildren()){

                    pList.put(key.getKey(),key.getValue());

                    System.out.println(pList);
                   // System.out.println(key);
//                    if(key.getKey().equals("A197")) {
//                        System.out.println(key.child("pSize"));
//                        System.out.println(key.child("product"));
//                        System.out.println(key.child("pName"));
//                    }
                    if(key.child("pName").getValue().toString().contains(temp)) {

                      // System.out.println(key.child("pName"));
//                        TextView t = new TextView(getApplicationContext());
//                        t.setText(key.child("pName").getValue().toString());


//                        listv.addView(t);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });




        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pName_value;
                pName_value = pName.getText().toString();
                listv.removeAllViews();

                Iterator<String> keys = pList.keySet().iterator();
                while(keys.hasNext()){
                    HashMap<String,String> tmp = (HashMap<String,String>)pList.get(keys.next());
                    if(tmp.get("pName").contains(pName_value)){
                        TextView t = new TextView(getApplicationContext());

                        t.setText(tmp.get("pName"));

                        listv.addView(t);
                    }
                }
            }
        });

    }
}