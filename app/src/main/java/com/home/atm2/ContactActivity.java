package com.home.atm2;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    private static final int CONTACT_REQUEST = 100;
    private ArrayList<Contact> contacList;
    RecyclerView recycler ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        int contact =ContextCompat
                .checkSelfPermission(this, Manifest.permission.READ_CONTACTS);  //get permission
        if(contact == PackageManager.PERMISSION_GRANTED){   //if permission granted
            getcontact();                                   //run content provider to get contact data
        }
        else{
            ActivityCompat.requestPermissions               //if permission didn't granted
                 (this,new String[]{Manifest.permission.READ_CONTACTS},CONTACT_REQUEST );
            //throw a request to get permission , no matter what users click , catch by onRequestPermission
        }


    }



    @Override
    public void onRequestPermissionsResult       //catch the throwback call
            (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CONTACT_REQUEST)    //if request code matched && users click accept
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getcontact();         //run content provider
            }
        //if not , do nothing
    }


    @TargetApi(Build.VERSION_CODES.O)   //content provider
    private void getcontact() {
        contacList = new ArrayList<>();
        Cursor c1 = getContentResolver()               //get provider instance && cursor
                .query(ContactsContract.Contacts.CONTENT_URI,null,null,null);
                //query( URI,where )
        Contact contacts;
        ArrayList<Contact> contacList = new ArrayList<>();
        ArrayList<String> phonelist=new ArrayList<>();
        while(c1.moveToNext()){     //move to 1st of queue and proof has/has not data
            String name = c1.getString(c1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));  //get contact name
            int id = c1.getInt(c1.getColumnIndex(ContactsContract.Contacts._ID));   //get contact id
            int hasphone = c1.getInt(c1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)); //get hasphone 1or0
            contacts=new Contact(name,id);
            if(hasphone == 1){  //if has phone number
                Cursor c2 = getContentResolver()     //get cursor 2
                        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                                new String []{String.valueOf(id)}
                                ,null); // unknowing
                while(c2.moveToNext()){ // run through all number
                    String phone = c2.getString(c2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
                    phonelist.add(phone);
                    contacts.setPhone(phonelist);
                }
            }
            contacList.add(contacts);
        }
        recycler=findViewById(R.id.recycler);
        ContactAdapter Adapter = new ContactAdapter(contacList);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(Adapter);
    }


    public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {
        ArrayList<Contact> contacList;

        public ContactAdapter(ArrayList<Contact> contacList) {
            this.contacList = contacList;
        }

        @NonNull
        @Override
        public ContactHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = getLayoutInflater()
                    .inflate(android.R.layout.simple_list_item_2,viewGroup,false);
            return new ContactHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactHolder contactHolder, int i) {
            Contact contact = contacList.get(i);
            contactHolder.text1.setText(contact.getName());
//            StringBuilder sb =new StringBuilder();

//            for (String phone : contact.getPhone()) {
//                sb.append(phone);
//                sb.append(" ");
//            }
//            contactHolder.text2.setText(sb.toString());



        }

        @Override
        public int getItemCount() {
            return contacList.size();
        }

        public class ContactHolder extends RecyclerView.ViewHolder{
            TextView text1 ;
            TextView text2 ;

            public ContactHolder(@NonNull View itemView) {
                super(itemView);
                text1=itemView.findViewById(android.R.id.text1);
                text2=itemView.findViewById(android.R.id.text2);
            }
        }
    }
}