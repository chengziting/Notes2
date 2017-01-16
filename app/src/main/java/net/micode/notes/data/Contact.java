package net.micode.notes.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by 10191042 on 1/6/2017.
 */
public class Contact {
    private static HashMap<String,String> s_contactCache;
    private static final String TAG = "Contact";

    private static final String CALLER_ID_SELECTION = "PHONE_NUMBERS_EQUAL(" + ContactsContract.CommonDataKinds.Phone.NUMBER
            + ",?) AND " + ContactsContract.RawContacts.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'"
            + " AND " + ContactsContract.Data.RAW_CONTACT_ID + " IN "
            + "(SELECT raw_contact_id "
            + " FROM phone_lookup"
            + " WHERE min_match = '+')";

    @Nullable
    public static String getContact(Context context, String phoneNum){
        if(s_contactCache == null){
            s_contactCache = new HashMap<String,String>();
        }

        if(s_contactCache.containsKey(phoneNum)){
            return s_contactCache.get(phoneNum);
        }

        String selection = CALLER_ID_SELECTION.replace("+", PhoneNumberUtils.toCallerIDMinMatch(phoneNum));
        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                selection,
                new String[]{phoneNum},
                null);

        if(cursor != null && cursor.moveToFirst()){
            try {
                String name = cursor.getString(0);
                s_contactCache.put(phoneNum,name);
                return name;

            }catch (IndexOutOfBoundsException e){
                Log.e(TAG,e.getMessage());
                return null;
            }
            finally {
                cursor.close();
            }
        }
        else {
            Log.d(TAG,"No contact matched with phonenum:"+phoneNum);
            return null;
        }

    }
}
