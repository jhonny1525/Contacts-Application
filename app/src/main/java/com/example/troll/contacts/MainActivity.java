package com.example.troll.contacts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    //Fields Declarations
    private EditText email,password;
    private Button loginemail;
    private SignInButton logingoogle;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        GoogleSignInOptions  gso =new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        mAuth=FirebaseAuth.getInstance();

        //Initialize fields
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        loginemail=(Button)findViewById(R.id.loginemail);
        logingoogle=(SignInButton) findViewById(R.id.logingoogle);

        logingoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 9001);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            Toast.makeText(this, "Logged in as "+currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
            Intent contactsActivity=new Intent(MainActivity.this,ContactsHome.class);
            startActivity(contactsActivity);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(this, "Google Authentication Successful", Toast.LENGTH_SHORT).show();
                 firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed
                // Using toast to display error
                Toast.makeText(this, "Error authenticating with Google", Toast.LENGTH_SHORT).show();
                Log.w("tagbc", "Google sign in failed", e);
            }
        }

    }



    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
       // Log.d("bctag3", "firebaseAuthWithGoogle:" + acct.getId());
      //  showProgressDialog();
        pd=new ProgressDialog(MainActivity.this,ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(false);
        pd.setMessage("Logging you in..");
        pd.setCancelable(false);
        pd.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("tagbc1", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            pd.dismiss();
                            if(user!=null){
                                Toast.makeText(MainActivity.this, "Logged in as "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
                                Intent contactsActivity=new Intent(MainActivity.this,ContactsHome.class);
                                startActivity(contactsActivity);
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            Log.w("tagbc2", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
// [END auth_with_google]

  /*  private void updateUI(FirebaseUser user) {
     //   hideProgressDialog();
        if (user != null) {
            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }*/


}
