package com.ssh.capstone.safetygohome.Database;

import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PreParingDB {

    public static final void initDB(Resources resources, boolean bForce) throws IOException {
        // DB파일 패키지 설치 폴더에 복사
        File outfile = new File("/data/data/com.ssh.capstone.safetygohome/databases/ssh.db");

        File folder = new File("/data/data/com.ssh.capstone.safetygohome/databases");
        if(!folder.exists())
            folder.mkdir();

        AssetManager assetManager = resources.getAssets();
        InputStream is = assetManager.open(
                "database/ssh.db",
                AssetManager.ACCESS_BUFFER);
        long filesize = is.available();

        if(outfile.length() < filesize || bForce)
        {
            byte[] tempdata = new byte[(int) filesize];
            is.read(tempdata);
            is.close();
            outfile.createNewFile();
            FileOutputStream fo = new FileOutputStream(outfile);
            fo.write(tempdata);
            fo.close();
        }
    }
}
