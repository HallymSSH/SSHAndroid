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


    public int GetUserNum(){
        int num=0;

        try
        {
            tableData = new TableData(m_ctx);

            SQLiteDatabase db = tableData.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT user_name from user_info"  ,null);

            if( cursor.getCount() > 0 ) {
                cursor.moveToFirst();
                num = cursor.getCount();
            }
            cursor.close();
            db.close();
        }
        catch(StringIndexOutOfBoundsException e) {
            Log.w("StrIdxOutOfBoundsExcept", e.getMessage());
            return -1;
        }
        finally {
            tableData.close();
        }

        return num;
    }

    //listview 로 불러올 데이테
    public boolean GetUser(ArrayList<String> username, ArrayList<String> usernum){

        try
        {
            tableData = new TableData(m_ctx);

            username.clear();
            usernum.clear();

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

    //데이터를 새로 추가할때 사용하는 함수
    public boolean SaveUser(String name, String num){
        String query = null;

        try {
            tableData = new TableData(m_ctx);
            SQLiteDatabase db = tableData.getReadableDatabase();

            db.execSQL("insert into user_info (user_name, user_num) values ('"+name+"', '"+num+"')");
            db.close();
        }
        catch(Exception e) {
            Log.w("Except", e.getMessage());
            return false;
        }
        finally {
            tableData.close();
        }

        return true;
    }


    //검색때 사용하는 함수
    public boolean SearchUser(ArrayList<String> username, ArrayList<String> usernum, String name){
        try
        {
            tableData = new TableData(m_ctx);

            username.clear();
            usernum.clear();

            String getname = "";
            String getnum = "";

            SQLiteDatabase db = tableData.getReadableDatabase();

            //Cursor cursor = db.rawQuery("SELECT user_name, user_num from user_info WHERE user_name = '"+ name +"'",null);
            Cursor cursor = db.rawQuery("SELECT user_name, user_num from user_info WHERE user_name LIKE '%"+ name +"%'",null);

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

    // CCTV
    public boolean SearchCCTV(String org_latitude, String org_longitude, String dst_latitude, String dst_longitude){
        try
        {
            tableData = new TableData(m_ctx);

            String location1 = "";
            String location2 = "";
            String location3 = "";

            SQLiteDatabase db = tableData.getReadableDatabase();

            // 인덱스 테이블 만들어서 찾기
            // Cursor cursor = db.rawQuery("SELECT from WHERE ,null);

            // cursor.close();
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
