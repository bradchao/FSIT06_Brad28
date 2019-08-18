package tw.org.iii.appps.brad28;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
        c.move(2);
        while (c.moveToNext()){
            for (int i=0; i<colCount; i++){
                Log.v("brad", c.getColumnName(i) + " => " + c.getString(i));
            }
        }

    }

    public void test2(View view) {
        Cursor c = cr.query(uriSettings,
                null,
                "name = ?",new String[]{Settings.System.SCREEN_BRIGHTNESS},null);
        c.moveToNext();
        String value = c.getString(c.getColumnIndexOrThrow("value"));
        Log.v("brad", "v = " + value);

        try {
            int v2 = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
            Log.v("brad", "v2 = " + v2);
        }catch (Exception e){
            Log.v("brad", e.toString());
        }
    }
}
