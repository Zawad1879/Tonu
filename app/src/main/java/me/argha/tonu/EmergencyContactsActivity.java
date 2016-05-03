package me.argha.tonu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmergencyContactsActivity extends AppCompatActivity {

    @Bind(R.id.forumMainList)
    ListView forumListView;

    ArrayList<ContactItem> contactItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_layout);
        ButterKnife.bind(this);

        contactItemArrayList=new ArrayList<>();
        contactItemArrayList.add(new ContactItem("Turzo","01719858641"));
        contactItemArrayList.add(new ContactItem("Mehedi","01914596128"));
        contactItemArrayList.add(new ContactItem("Argha","01911190527"));
        contactItemArrayList.add(new ContactItem("Junayed", "01723461326"));

        forumListView.setAdapter(new ContactListAdapter());
    }

    @OnClick(R.id.fab)
    public void showContactAddDialog(){
        EditText editText=new EditText(this);
        editText.setHint("Mobile Number");
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Add New Contact")
                .setView(editText)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        Dialog dialog=builder.create();
        dialog.show();
    }

    class ContactListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return contactItemArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row=getLayoutInflater().inflate(R.layout.single_emergency_item_row,null,false);
            TextView nameTv= (TextView) row.findViewById(R.id.emerNameTv);
            TextView numberTv= (TextView) row.findViewById(R.id.emerContactNumber);

            nameTv.setText(contactItemArrayList.get(position).getName());
            numberTv.setText(contactItemArrayList.get(position).getMobile());
            return row;
        }
    }

    class ContactItem{
        private String name;
        private String mobile;

        public ContactItem(String name, String mobile) {
            this.name = name;
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public String getMobile() {
            return mobile;
        }
    }
}
