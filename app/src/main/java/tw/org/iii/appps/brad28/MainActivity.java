package tw.org.iii.appps.brad28;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private ContentResolver cr;
    private Uri uriSettings = Settings.System.CONTENT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CALL_LOG},
                        123);

        } else {
            // Permission has already been granted
        }

        cr = getContentResolver();

        // content://database/table
        // content://ContactsContract 聯絡人
        // content://CallLog 通話紀錄
        // content://MediaStore 媒體資料
        // content://Settings 設定資料

    }

    public void test1(View view) {
        // select * from Settings
        Cursor c = cr.query(uriSettings,
                null,null,null,null);

        int colCount = c.getColumnCount();
        while (c.moveToNext()){
            for (int i=0; i<colCount; i++){
                Log.v("brad", c.getColumnName(i) + " => " + c.getString(i));
            }
        }
        c.close();

    }

    public void test2(View view) {
//        Cursor c = cr.query(uriSettings,
//                null,
//                "name = ?",new String[]{Settings.System.SCREEN_BRIGHTNESS},null);
//        c.moveToNext();
//        String value = c.getString(c.getColumnIndexOrThrow("value"));
//        Log.v("brad", "v = " + value);
//
//        try {
//            int v2 = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
//            Log.v("brad", "v2 = " + v2);
//        }catch (Exception e){
//            Log.v("brad", e.toString());
//        }
        Log.v("brad", getSystemSetting(Settings.System.ANDROID_ID));

    }

    private String getSystemSetting(String settingName){
        String ret = null;
        Cursor c = cr.query(uriSettings,
                null,
                "name = ?",new String[]{settingName},null);
        try {
            ret =  Settings.System.getString(cr, settingName);
        }catch (Exception e){
            Log.v("brad", e.toString());
            //return null;
        }
        c.close();
        return ret;
    }

    public void test3(View view) {
        //ContactsContract.Contacts.CONTENT_URI 所有聯絡人
        //
        //ContactsContract.CommonDataKinds.Phone
        //ContactsContract.CommonDataKinds.Email
        //ContactsContract.CommonDataKinds.Photo

        // ContactsContract.Contacts._ID => key => phone
        Log.v("brad", getPhoneNumber("36483"));
    }

    private String getPhoneNumber(String id){
        // where ContactsContract.CommonDataKinds.Photo.CONTACT_ID+"=?"
        String ret = null;
        Cursor c = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Photo.CONTACT_ID+"=?",
                new String[]{id},null
        );
        int count = c.getCount();
        if (count>0) {
            c.moveToNext();
            int col = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
            ret =  c.getString(col);
        }

        c.close();

        return ret;
    }

    public void test4(View view){
        // CallLog.Calls.CONTENT_URI
        // CallLog.Calls.CACHED_NAME
        // CallLog.Calls.NUMBER
        // CallLog.Calls.TYPE => CallLog.Calls.INCOMING_TYPE, OUTGOING_TYPE, MISSED_TYPE
        // CallLog.Calls.DATE
        // CallLog.Calls.DURATION (second)

        Cursor c = cr.query(CallLog.Calls.CONTENT_URI,
                null,null,null,null);

        while (c.moveToNext()){
            String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String number = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
            Log.v("brad", name + ":" + number);

        }
        c.close();



    }


    public void test5(View view){
        // MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        // MediaStore.Images.Media.DATA
    }

}
