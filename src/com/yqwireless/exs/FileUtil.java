package com.yqwireless.exs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.Resources;
import android.util.Log;

public class FileUtil {
	
	public static void loadDBFile(int resourceID, Resources resources, File dbFile) throws IOException {
		InputStream inputStream = resources.openRawResource(resourceID);
		try {
			FileOutputStream out = new FileOutputStream(dbFile);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = inputStream.read(temp)) != -1) {
				out.write(temp);
			}
			inputStream.close();
			out.flush();
			out.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("db_error", "open the db file on databases folder error ");
		}

	}
}
