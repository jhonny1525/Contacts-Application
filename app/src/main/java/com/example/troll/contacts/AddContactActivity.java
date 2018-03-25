package com.example.troll.contacts;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddContactActivity extends AppCompatActivity {
    LinearLayout numbers;
    EditText number1;
    Button re,add1;
    boolean edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        numbers=findViewById(R.id.nwrapper);
        Button add=findViewById(R.id.addnumberfield);
        re=findViewById(R.id.removenumberfield);
        number1=findViewById(R.id.number1);
        number1.setTag("1");
        edit=getIntent().getBooleanExtra("edit",false);
        if(edit){
        EditText fnt=findViewById(R.id.firstnameac);
        EditText lnt=findViewById(R.id.lastnameac);
        EditText adt=findViewById(R.id.addressac);
        EditText num1=   numbers.findViewWithTag(1+"");

        nContact ed=(nContact) getIntent().getSerializableExtra("contact");
        num1.setText(ed.getContactNumbers().get(0).getNumber());
        fnt.setText(ed.getFirstName());
        lnt.setText(ed.getLastName());
        adt.setText(ed.getAddress());

            for (int i =1 ; i < ed.contactNumbers.size(); i++) {
                int numbercount=numbers.getChildCount();
                EditText b=new EditText(AddContactActivity.this);
                b.setHint("number");
                b.setVisibility(View.VISIBLE);
                b.setText(ed.contactNumbers.get(i).getNumber());
                b.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                b.setTag((numbercount+1)+"");
                b.setInputType(InputType.TYPE_CLASS_NUMBER);
                numbers.addView(b);
            }
        }



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numbercount=numbers.getChildCount();
                EditText b=new EditText(AddContactActivity.this);
                b.setHint("number");
                b.setVisibility(View.VISIBLE);
                b.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                b.setTag((numbercount+1)+"");
                b.setInputType(InputType.TYPE_CLASS_NUMBER);
                numbers.addView(b);
                if(numbers.getChildCount()>1)
                {
                    re.setVisibility(View.VISIBLE);
                }
            }
        });
        if(numbers.getChildCount()==1)
        {
            re.setVisibility(View.GONE);
        }
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numbercount=numbers.getChildCount();
                Log.d("removecl",numbercount+"");
                EditText et=numbers.findViewWithTag(numbercount+"");
                numbers.removeView(et);
                if(numbers.getChildCount()==1)
                {
                    re.setVisibility(View.GONE);
                }
            }
        });
        add1=findViewById(R.id.addcontactto);
        if(edit)
        {
            add1.setText("Save Changes");
        }
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth= FirebaseAuth.getInstance();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                nContact contact=new nContact();
                EditText fnt=findViewById(R.id.firstnameac);
                EditText lnt=findViewById(R.id.lastnameac);
                EditText adt=findViewById(R.id.addressac);
                contact.address=adt.getText().toString().trim();
                contact.firstName=fnt.getText().toString().trim();
                contact.lastName=lnt.getText().toString().trim();
                for (int i = 1; i <=numbers.getChildCount(); i++) {
                    TextView temp=numbers.findViewWithTag(i+"");
                    contact.contactNumbers.add(new contactNumber("+91","mobile",temp.getText().toString().trim()));
                }
                add1.setEnabled(false);
                if(edit) {
                    add1.setText("Saving Changes");
                }
                else {
                    add1.setText("adding please wait");
                }
                if(edit)
                {
                    db.collection(mAuth.getCurrentUser().getUid()).document(getIntent().getStringExtra("contactid")).set(contact).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddContactActivity.this, "added", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddContactActivity.this,ContactsHome.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddContactActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    db.collection(mAuth.getCurrentUser().getUid()).document().set(contact).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if(!edit)
                                Toast.makeText(AddContactActivity.this, "added", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(AddContactActivity.this, "saved", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddContactActivity.this,ContactsHome.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddContactActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }
}
