package com.example.troll.contacts;

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
       updateList();

    }
    public void updateList()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                List<DocumentSnapshot> documents=documentSnapshots.getDocuments();
                Collections.sort(documents, new Comparator<DocumentSnapshot>() {
                    @Override
                    public int compare(DocumentSnapshot documentSnapshot, DocumentSnapshot t1) {
                        return documentSnapshot.getString("firstName").compareToIgnoreCase(t1.getString("firstName"));
                    }
                });
                contactsListView=findViewById(R.id.contacts);
                contactsListView.setAdapter(new Contactsadapter(ContactsHome.this,documents));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ContactsHome.this, "failed to fetch contacts", Toast.LENGTH_SHORT).show();
            }
        });
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
        contactsListView=findViewById(R.id.contacts);
        contactsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                    if (i==SCROLL_STATE_IDLE);
                        fab.show();
                    if(i==SCROLL_STATE_TOUCH_SCROLL)
                        fab.hide();
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {


            }
        });
        Toast.makeText(this, "Viewing Contacts "+mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
       // mAuth.signOut();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       if(item.getItemId()==R.id.profile)
       {
         //  mAuth.signOut();
         //  startActivity(new Intent(ContactsHome.this,MainActivity.class));

           mAuth= FirebaseAuth.getInstance();
           FirebaseFirestore db = FirebaseFirestore.getInstance();
           nContact contact=new nContact();
           contact.address="awfafafafafafawrawraw";
           contact.firstName="gfxfzxfgHarsh";
           contact.lastName="Jain";
           contact.contactNumbers.add(new contactNumber("+91","mobile","7974567256"));
           contact.contactNumbers.add(new contactNumber("+91","mobile2","7974567246"));

           db.collection(mAuth.getCurrentUser().getUid()).document().set(contact).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {
                   Toast.makeText(ContactsHome.this, "added", Toast.LENGTH_SHORT).show();
                   updateList();
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(ContactsHome.this, "fail", Toast.LENGTH_SHORT).show();
               }
           });


           return true;
       }
        return super.onOptionsItemSelected(item);
    }
}
