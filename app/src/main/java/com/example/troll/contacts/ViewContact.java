package com.example.troll.contacts;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewContact extends AppCompatActivity {

    LinearLayout numbers;
    TextView fullname;
    TextView add;
    Button del,edit;
    nContact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_contact);
        numbers= findViewById(R.id.nwrappervc);
        fullname=findViewById(R.id.fullnamevc);
        add=findViewById(R.id.addressvc);
        contact=(nContact) getIntent().getSerializableExtra("contact");
        fullname.setText(contact.getFirstName()+" "+contact.getLastName());
        add.setText(contact.getAddress());
        del=findViewById(R.id.deletecontactvc);
        edit=findViewById(R.id.editcontactvc);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add=new Intent(ViewContact.this,AddContactActivity.class);
                add.putExtra("contact",contact);
                add.putExtra("contactid",getIntent().getStringExtra("contactid"));
                add.putExtra("edit",true);
                startActivity(add);
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                del.setText("Deleting please wait");
                del.setEnabled(false);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
               FirebaseAuth mAuth= FirebaseAuth.getInstance();
                db.collection(mAuth.getCurrentUser().getUid()).document(getIntent().getStringExtra("contactid"))
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ViewContact.this, "Contact Deleted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ViewContact.this,ContactsHome.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ViewContact.this, "Cannot Delete try again later", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        for(int i=0;i<contact.contactNumbers.size();i++)
        {
            contactNumber tempc=contact.getContactNumbers().get(i);
            TextView temp=new TextView(ViewContact.this);
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            temp.setPadding(16,16,16,16);
            temp.setTextSize(20);
            temp.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_phone_black_24dp,0);
            temp.setLayoutParams(lp);
            temp.setText(tempc.getCountryCode()+tempc.getNumber());
            numbers.addView(temp);
        }
    }
}
