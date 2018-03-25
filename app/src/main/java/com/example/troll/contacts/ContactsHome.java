package com.example.troll.contacts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ContactsHome extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ListView contactsListView;
    private FloatingActionButton fab;


    @Override
    protected void onStart() {
        super.onStart();

        // Write a message to the database
        mAuth= FirebaseAuth.getInstance();
        contactsListView=findViewById(R.id.contacts);
        fab=findViewById(R.id.addcontact);
        if(mAuth.getCurrentUser()!=null)
            updateList();

    }
    public void updateList()
    {
        if(mAuth!=null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                    Collections.sort(documents, new Comparator<DocumentSnapshot>() {
                        @Override
                        public int compare(DocumentSnapshot documentSnapshot, DocumentSnapshot t1) {
                            return documentSnapshot.getString("firstName").compareToIgnoreCase(t1.getString("firstName"));
                        }
                    });
                    contactsListView = findViewById(R.id.contacts);
                    contactsListView.setAdapter(new Contactsadapter(ContactsHome.this, documents));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ContactsHome.this, "failed to fetch contacts", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_home);
        mAuth= FirebaseAuth.getInstance();
        fab=findViewById(R.id.addcontact);
        contactsListView=findViewById(R.id.contacts);
        contactsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if(absListView.getCount()>=6) {
                    if (i == SCROLL_STATE_IDLE) ;
                    fab.show();
                    if (i == SCROLL_STATE_TOUCH_SCROLL || i == SCROLL_STATE_FLING)
                        fab.hide();
                    if (absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                        fab.hide();
                    }
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactsHome.this,AddContactActivity.class));
            }
        });
       // mAuth.signOut();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("No", null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       if(item.getItemId()==R.id.profile)
       {
           Intent i=new Intent(ContactsHome.this,ProfileActivity.class);
           startActivity(i);
           return true;
       }
        return super.onOptionsItemSelected(item);
    }
}
