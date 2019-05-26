package com.home.atm2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE=100;
    private final int RESULTCODE_LOGIN=-1;
    private ArrayList<Function> flist;
    private String[] fnames;
    private RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        findviews();

        Intent intent=new Intent(this,LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
        setdata();





    }

    //recyclerview
    private void setdata() {
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new GridLayoutManager(this,3));
        flist = new ArrayList<>();
        fnames = getResources().getStringArray(R.array.fnames);
        flist.add(new Function(fnames[0],R.drawable.func_balance));
        flist.add(new Function(fnames[1],R.drawable.func_transaction));
        flist.add(new Function(fnames[2],R.drawable.func_finance));
        flist.add(new Function(fnames[3],R.drawable.func_contacts));
        flist.add(new Function(fnames[4],R.drawable.func_exit));

        recyclerview.setAdapter(new IconAdapter());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE) {
            if (resultCode != RESULTCODE_LOGIN)
                finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void findviews(){
        recyclerview = findViewById(R.id.recycler);

    }


    class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconHolder>
    {

        @NonNull
        @Override
        public IconHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = getLayoutInflater()
                    .inflate(R.layout.function,viewGroup,false);
            return new IconHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull IconHolder iconHolder, int i) {
            final Function f = flist.get(i);
            iconHolder.tx_IconName.setText(f.getName());
            iconHolder.img_Icon.setImageResource(f.getIcon());
            iconHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicking(f);
                }
            });

        }

        @Override
        public int getItemCount() {
            return flist.size();
        }

        class IconHolder extends RecyclerView.ViewHolder{
            TextView tx_IconName;
            ImageView img_Icon;
            public IconHolder(@NonNull View itemView) {
                super(itemView);
                tx_IconName = itemView.findViewById(R.id.tx_iconname);
                img_Icon= itemView.findViewById(R.id.img_icon);

                //use itemviw to findview , not mainActivity!!
            }
        }
    }

    private void clicking(Function f) {
        switch (f.getIcon()) {
            case R.drawable.func_balance:
                break;
            case R.drawable.func_contacts:
                Intent contacts = new Intent(this,ContactActivity.class);
                startActivity(contacts);
                break;
            case R.drawable.func_finance:
                break;
            case R.drawable.func_transaction:
                break;
            case R.drawable.func_exit:
                finish();
                break;
        }
    }


}
