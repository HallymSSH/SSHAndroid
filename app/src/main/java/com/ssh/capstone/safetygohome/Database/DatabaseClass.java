package com.ssh.capstone.safetygohome.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ssh.capstone.safetygohome.ContactData;

import java.util.ArrayList;

public class DatabaseClass {

    private com.ssh.capstone.safetygohome.Database.TableData tableData;
    private Context m_ctx;

    public DatabaseClass(Context ctx) {
        m_ctx = ctx;
    }


    public int GetUserNum() {
        int num = 0;

        try {
            tableData = new TableData(m_ctx);

            SQLiteDatabase db = tableData.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT user_name from user_info", null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                num = cursor.getCount();
            }
            cursor.close();
            db.close();
        } catch (StringIndexOutOfBoundsException e) {
            Log.w("StrIdxOutOfBoundsExcept", e.getMessage());
            return -1;
        } finally {
            tableData.close();
        }

        return num;
    }

    //listview 로 불러올 데이테
    public boolean GetUser(ArrayList<String> username, ArrayList<String> usernum) {

        try {
            tableData = new TableData(m_ctx);

            username.clear();
            usernum.clear();

            String getname = "";
            String getnum = "";

            SQLiteDatabase db = tableData.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT user_name, user_num from user_info", null);

            if (cursor.getCount() > 0) {
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
        } catch (StringIndexOutOfBoundsException e) {
            Log.w("StrIdxOutOfBoundsExcept", e.getMessage());
            return false;
        } finally {
            tableData.close();
        }

        return true;
    }

    //데이터를 새로 추가할때 사용하는 함수
    public boolean SaveUser(String name, String num) {
        String query = null;

        try {
            tableData = new TableData(m_ctx);
            SQLiteDatabase db = tableData.getReadableDatabase();

            db.execSQL("insert into user_info (user_name, user_num) values ('" + name + "', '" + num + "')");
            db.close();
        } catch (Exception e) {
            Log.w("Except", e.getMessage());
            return false;
        } finally {
            tableData.close();
        }

        return true;
    }


    //검색때 사용하는 함수
    public boolean SearchUser(ArrayList<String> username, ArrayList<String> usernum, String name) {
        try {
            tableData = new TableData(m_ctx);

            username.clear();
            usernum.clear();

            String getname = "";
            String getnum = "";

            SQLiteDatabase db = tableData.getReadableDatabase();

            //Cursor cursor = db.rawQuery("SELECT user_name, user_num from user_info WHERE user_name = '"+ name +"'",null);
            Cursor cursor = db.rawQuery("SELECT user_name, user_num from user_info WHERE user_name LIKE '%" + name + "%'", null);

            if (cursor.getCount() > 0) {
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
        } catch (StringIndexOutOfBoundsException e) {
            Log.w("StrIdxOutOfBoundsExcept", e.getMessage());
            return false;
        } finally {
            tableData.close();
        }

        return true;
    }

    public boolean DeleteUser(ArrayList<ContactData> items, ArrayList<Boolean> item_tag) {
        try {
            tableData = new TableData(m_ctx);

            SQLiteDatabase db = tableData.getReadableDatabase();

            for (int i = 0; i < items.size(); i++) {
                if (item_tag.get(i) == true) {
                    db.execSQL("DELETE FROM user_info WHERE user_name = '" + items.get(i).getName() + "'" +
                            " AND user_num = '" + items.get(i).getNum() + "'");
                }
            }
            db.close();
        } catch (StringIndexOutOfBoundsException e) {
            Log.w("StrIdxOutOfBoundsExcept", e.getMessage());
            return false;
        } finally {
            tableData.close();
        }

        return true;
    }

    public boolean ModifyUser(ArrayList<String> user_send) {
        try {
            ArrayList<String> push = user_send;
            tableData = new TableData(m_ctx);

            SQLiteDatabase db = tableData.getReadableDatabase();

            db.execSQL("UPDATE user_info SET user_name = '" + push.get(0) + "' , user_num = '" + push.get(1) + "' " +
                    "WHERE user_name = '" + push.get(2) + "' AND user_num = '" + push.get(3) + "'");
            db.close();
        } catch (StringIndexOutOfBoundsException e) {
            Log.w("StrIdxOutOfBoundsExcept", e.getMessage());
            return false;
        } finally {
            tableData.close();
        }

        return true;
    }

    public boolean searchCCTV(ArrayList<Double[]> positionList, double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        try {
            tableData = new TableData(m_ctx);

            positionList.clear();

            SQLiteDatabase db = tableData.getReadableDatabase();

            // between 순서 위해 값 크면 순서 바꿔주기
            if (startLatitude < endLatitude) {
                double temp = startLatitude;
                startLatitude = endLatitude;
                endLatitude = temp;
            }

            if (startLongitude < endLongitude) {
                double temp = startLongitude;
                startLongitude = endLongitude;
                endLongitude = temp;
            }

            // 위도 먼저 필터링하고 경도 찾기
            Cursor cursor = db.rawQuery("SELECT max(roadAddress) as roadAddress, max(branchAddress) as branchAddress, latitude, longitude, count(latitude) as cnt FROM cctv WHERE latitude BETWEEN " + startLatitude + " AND " + endLatitude + " and longitude BETWEEN " + startLongitude + " AND " + endLongitude + " group by latitude, longitude order by count(latitude) DESC", null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                // PositionList ArrayList에 2차원배열로 위도, 경도 add
                do {
                    positionList.add(new Double[]{cursor.getDouble(0), cursor.getDouble(1)});

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (StringIndexOutOfBoundsException e) {
            Log.w("StrIdxOutOfBoundsExcept", e.getMessage());
            return false;
        } finally {
            tableData.close();
        }

        return true;
    }
}
