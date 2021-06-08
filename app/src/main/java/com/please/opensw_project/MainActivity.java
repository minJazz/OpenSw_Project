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
    /*
        Tips
        key = String ex) A000
        value = HashMap<String, String> ex) key = (pName, product, pSize) value = String...
     */

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
        listv = findViewById(R.id.listv);

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
                pName_value = pName.getText().toString();

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


    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);

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
                Intent intent = new Intent(this, InfoActivit.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });

        return v;
    }

/*
    public LinearLayout addProductView1(Object obj) {


        String 이름;
        String 주소;
        String 거리;
        String 가격;
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

        TextView t주소 = new TextView(mContext);
        t주소.setLayoutParams(params3);
        TextView t거리 = new TextView(mContext);
        t거리.setLayoutParams(params1);

        TextView t크기 = new TextView(mContext);

        TextView t가격 = new TextView(mContext);

        t가격.setText("12345");
        t주소.setText("선문로221번길 40-3 다온빌 307호");
        t거리.setText("5Km");
        t이름.setText(이름);
        t크기.setText(크기);

        LinearLayout 가격사이즈 = new LinearLayout(mContext);
        가격사이즈.setOrientation(LinearLayout.VERTICAL);
        가격사이즈.addView(t가격);
        가격사이즈.addView(t크기);
        가격사이즈.setLayoutParams(params1);

        LinearLayout 주소거리 = new LinearLayout(mContext);
        주소거리.setOrientation(LinearLayout.HORIZONTAL);
        주소거리.addView(t주소);
        주소거리.addView(t거리);

        LinearLayout 이름_주소거리 = new LinearLayout(mContext);
        이름_주소거리.setOrientation(LinearLayout.VERTICAL);
        이름_주소거리.addView(t이름);
        이름_주소거리.addView(주소거리);
        이름_주소거리.setLayoutParams(params4);

        v.addView(이름_주소거리);
        v.addView(가격사이즈);


        return v;
    }

 */

}