package com.please.opensw_project;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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


public class MainActivity extends AppCompatActivity {

    EditText pName;
    Button btn_search;
    ImageButton btn_map;
    LinearLayout listv;

    private Context mContext;

    private DatabaseReference databaseReference;

    public static HashMap<String, HashMap<String, String>> pList = new HashMap<>();
    public static HashMap<String, HashMap<String, Object>> infoList = new HashMap<>();
    public static HashMap<String, HashMap<String, String>> location = new HashMap<>();
    /*
        Tips
        key = String ex) A000
        value = HashMap<String, String> ex) key = (pName, product, pSize) value = String...
     */

    public  static LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
    public  static LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
    public  static LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);

    /*
    private void getHashKey() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pName = findViewById(R.id.product_search);
        btn_search = findViewById(R.id.btn_search);
        btn_map = findViewById(R.id.btn_map);
        listv = findViewById(R.id.listv2);

        mContext = getApplicationContext();


        databaseReference = FirebaseDatabase.getInstance().getReference().child("productcodemap");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                for (DataSnapshot key : snapshot.getChildren()) {
                    pList.put(key.getKey(), (HashMap<String, String>) key.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("products");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                for (DataSnapshot key : snapshot.getChildren()) {
                    infoList.put(key.getKey(), (HashMap<String, Object>) key.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("geocode");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                for (DataSnapshot key : snapshot.getChildren()) {
                    location.put(key.getKey(), (HashMap<String, String>) key.getValue());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Map.class);
                startActivity(intent);
            }
        });

        pName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_search.performClick();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pName_value;
                pName_value = pName.getText().toString().replace(" ", "" );
                listv.removeAllViews();

                Iterator<String> keys = pList.keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    HashMap<String, String> tmp = pList.get(key);
                    if (tmp.get("pName").contains(pName_value)) {
                        Object obj = pList.get(key);

                        LinearLayout l = addProductView(key, obj);

                        listv.addView(l);
                    }
                }
            }
        });
    }




    public LinearLayout addProductView(String key, Object obj) {


        String 이름;
        String 크기;

        HashMap<String, String> ds = (HashMap<String, String>) obj;

        이름 = ds.get("pName");
        크기 = ds.get("pSize");


        LinearLayout v = new LinearLayout(mContext);

        v.setPadding(40, 40, 40, 40);
        params1.weight = 1f;
        params3.weight = 3f;
        params4.weight = 4f;


        TextView t이름 = new TextView(mContext);

        t이름.setLayoutParams(params3);


        TextView t크기 = new TextView(mContext);

        t크기.setLayoutParams(params1);
        t크기.setGravity(Gravity.CENTER);

        t이름.setText(이름);
        t크기.setText(크기);

        v.addView(t이름);
        v.addView(t크기);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                intent.putExtra("key", key);
//                System.err.println("여기요"+infoList.get(key));
                startActivity(intent);
            }
        });

        return v;
    }
}