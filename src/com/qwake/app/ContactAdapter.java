package com.qwake.app;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;

public class ContactAdapter {
	public static ArrayList<String> phoneValueArr = new ArrayList<String>();
    public static ArrayList<String> nameValueArr = new ArrayList<String>();
    public ArrayList<String> readContactData(Context context) {
        ArrayList<String> adapter = new ArrayList<String>();
        try {
             
            /*********** Reading Contacts Name And Number **********/
             
            String phoneNumber = "";
            ContentResolver cr = context
                    .getContentResolver();
             
            //Query to get contact name
             
            Cursor cur = cr
                    .query(ContactsContract.Contacts.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
             
            // If data data found in contacts 
            if (cur.getCount() > 0) {
                 
                Log.i("AutocompleteContacts", "Reading   contacts........");
                 
                int k=0;
                String name = "";
                 
                while (cur.moveToNext()) 
                {
                     
                    String id = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts._ID));
                    name = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                     
                    //Check contact have phone number
                    if (Integer
                            .parseInt(cur
                                    .getString(cur
                                        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) 
                    {
                             
                        //Create query to get phone number by contact id
                        Cursor pCur = cr
                                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                            null,
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                    + " = ?",
                                            new String[] { id },
                                            null);
                            int j=0;
                             
                            while (pCur
                                    .moveToNext()) 
                            {
                                // Sometimes get multiple data 
                                if(j==0)
                                {
                                    // Get Phone number
                                    phoneNumber =""+pCur.getString(pCur
                                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                     
                                    // Add contacts names to adapter
                                    adapter.add(name);
                                     
                                    // Add ArrayList names to adapter
                                    phoneValueArr.add(phoneNumber.toString());
                                    nameValueArr.add(name.toString());
                                     
                                    j++;
                                    k++;
                                }
                            }  // End while loop
                            pCur.close();
                        } // End if
                     
                }  // End while loop
 
            } // End Cursor value check
            cur.close();
                     
          
        } catch (Exception e) {
             Log.i("AutocompleteContacts","Exception : "+ e);
        }
                     
     return adapter;
   }
 public int getIndex(String name)
 {
	 return  nameValueArr.indexOf(name);
	 
	 
 }
 public String getPhoneNumber(int index)
 {
	 return phoneValueArr.get(index);
	 
 }
}
