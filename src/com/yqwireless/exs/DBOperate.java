package com.yqwireless.exs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBOperate {

	private static final String TABLE_NAME = "company_info";
	private SQLiteDatabase database = null;

	public DBOperate(Context context) {
		database = new DBOpenHelper(context).getReadableDatabase();
	}

	public String getCompanyByName(String company_cn) {
		List<String> list = queryCompany_cn("company_cn like ?", company_cn, null);
		if (list.size() != 0) {
			return list.get(0);
		}
		return null;
	}

	public List<String> getCompanyByLetter(String letter) {

		return queryCompany_cn("letter = ?", letter, null);

	}

	public List<String> getAllCompany() {

		return queryCompany_cn(null, null, "letter");
	}

	public Map<String, String> getCompanyInfo(String company_cn) {
		Map<String, String> map = null;
		Cursor cursor = database.query(TABLE_NAME, new String[] { "company_en", "phone" },
				"company_cn = ?", new String[] { company_cn }, null, null, null);
		if (cursor.moveToFirst()) {
			map = new HashMap<String, String>(1);
			map.put("company_en", cursor.getString(cursor.getColumnIndex("company_en")));
			map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
			cursor.close();
		}
		return map;
	}

	private List<String> queryCompany_cn(String selection, String mapping, String orderBy) {
		List<String> list = new ArrayList<String>();
		Cursor cursor = null;
		if (mapping != null) {
			cursor = database.query(TABLE_NAME, new String[] { "company_cn" },
					selection, new String[] { mapping }, null, null, orderBy);
		} else {
			cursor = database.query(TABLE_NAME, new String[] { "company_cn" },
					null, null, null, null, orderBy);
		}
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				list.add(cursor.getString(0));
				cursor.moveToNext();
			}
		} else {
			// 查询结果为空
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}

	public void close() {
		if (database != null) {
			database.close();
		}
	}
}
