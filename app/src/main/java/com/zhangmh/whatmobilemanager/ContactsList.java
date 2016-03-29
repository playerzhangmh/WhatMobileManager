package com.zhangmh.whatmobilemanager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangmh.Utils.ContactUtils;
import com.zhangmh.bean.ContactItem;

import java.util.ArrayList;
import java.util.List;

public class ContactsList extends ActionBarActivity {
    private List<ContactItem> contactItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        contactItems=new ArrayList<>();
        contactItems= ContactUtils.getContactItemList(this);
        if(contactItems.size()!=0){
            ListView lv_Contactslist_allitems= (ListView) findViewById(R.id.lv_Contactslist_allitems);
            lv_Contactslist_allitems.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return contactItems.size();
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    ContactItem contactItem = contactItems.get(position);
                    LinearLayout view=null;
                    Holder holder=new Holder();
                    if(convertView!=null){
                        view= (LinearLayout) convertView;
                        holder= (Holder) view.getTag();
                    }else {
                        view= (LinearLayout) View.inflate(ContactsList.this,R.layout.item_contacts_list,null);
                        holder.tv_itemContact_name= (TextView) view.findViewById(R.id.tv_itemContact_name);
                        holder.tv_itemContact_teleNum= (TextView) view.findViewById(R.id.tv_itemContact_teleNum);
                    }
                    holder.tv_itemContact_name.setText(contactItem.getName());
                    holder.tv_itemContact_teleNum.setText(contactItem.getTeleNum());
                    return view;
                }
            });
            lv_Contactslist_allitems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ContactsList.this, GuardatSetup3.class);
                    intent.putExtra("num", contactItems.get(position).getTeleNum());
                    setResult(1000);
                    startActivity(intent);
                    finish();
                }
            });
        }else {
            Toast.makeText(this,"你的通讯录为空，请手动添加",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,GuardatSetup3.class));
            finish();
        }

    }

    class Holder{
        TextView tv_itemContact_name;
        TextView tv_itemContact_teleNum;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,GuardatSetup3.class));
        finish();
    }
}
