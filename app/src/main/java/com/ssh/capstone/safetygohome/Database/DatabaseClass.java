package com.ssh.capstone.safetygohome.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseClass {

    private com.ssh.capstone.safetygohome.Database.TableData tableData;
    private Context m_ctx;

    public DatabaseClass(Context ctx) {
        m_ctx = ctx;
    }

    public boolean GetUserName(ArrayList<String> username, ArrayList<String> usernum){
        try
        {
            tableData = new TableData(m_ctx);

            String getname = "";
            String getnum = "";

            SQLiteDatabase db = tableData.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT user_name, user_num from user_info"  ,null);

            if( cursor.getCount() > 0 ) {
                cursor.moveToFirst();

                do {
                    getname = cursor.getString(0);
                    getnum = cursor.getString(1);

                    username.add(getname);
                    usernum.add(getnum);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch(StringIndexOutOfBoundsException e) {
            Log.w("StrIdxOutOfBoundsExcept", e.getMessage());
            return false;
        }
        finally {
            tableData.close();
        }

        return true;
    }
}
