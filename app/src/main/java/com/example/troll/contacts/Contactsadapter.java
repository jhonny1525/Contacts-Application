package com.example.troll.contacts;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

/**
 * Created by troll on 23-03-2018.
 */

public class Contactsadapter extends BaseAdapter {

    private Context mContext;
    private List<DocumentSnapshot> mCursor;
    TextView  contactNameView;
    nContact temp;
    ImageView message;
    View view;

    public Contactsadapter(Context context, List<DocumentSnapshot> cursor) {
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public int getCount() {
        return mCursor.size();
    }

    @Override
    public Object getItem(int position) {
        return mCursor.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {




        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listviewitem, null);
        }
        else {
            view = convertView;
        }

        // Set contact name and number
         contactNameView = (TextView) view.findViewById(R.id.fullname);
        TextView contactNumberView = (TextView) view.findViewById(R.id.number);
        ImageView call=view.findViewById(R.id.call);
        message=view.findViewById(R.id.message);
        temp =(nContact)mCursor.get(position).toObject(nContact.class);
        contactNameView.setTag(temp);
        contactNameView.setTag(R.string.tag1,mCursor.get(position).getId());
        call.setTag(temp);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                nContact temp=(nContact) view.getTag();
                intent.setData(Uri.parse("tel:"+temp.contactNumbers.get(0).getCountryCode()+temp.contactNumbers.get(0).getNumber()));
                mContext.startActivity(intent);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.putExtra("sms_body", "");
                Uri data = Uri.parse("sms:"+temp.contactNumbers.get(0).getCountryCode()+temp.contactNumbers.get(0).getNumber());
                intent.setData(data);
                mContext.startActivity(intent);
            }
        });
        String name = temp.firstName+" "+temp.lastName;
        contactNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewc=new Intent(mContext,ViewContact.class);
                nContact temp=(nContact) view.getTag();
                viewc.putExtra("contact",temp);
                viewc.putExtra("contactid",view.getTag(R.string.tag1).toString());
                mContext.startActivity(viewc);
            }
        });
        if(!temp.contactNumbers.isEmpty())
        contactNumberView.setText(temp.contactNumbers.get(0).getCountryCode()+temp.contactNumbers.get(0).getNumber());
        else
            contactNumberView.setText("");
            contactNameView.setText( name );
        return view;
    }
}