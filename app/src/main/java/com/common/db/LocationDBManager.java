package com.common.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.common.model.CityListItem;


/**
 * 选择省市区DBManager
 * @author zhangruntao
 */
public class LocationDBManager {
	private final int BUFFER_SIZE = 1024;
	public static final String DB_NAME = "GT_REGION.db";
	public static final String PACKAGE_NAME = "YourPackageName";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME;
	private SQLiteDatabase database;
	private Context context;
	private File file = null;

	public LocationDBManager(Context context) {
		this.context = context;
		openDatabase();
	}

	private void openDatabase() {
		this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
	}

	public SQLiteDatabase getDatabase() {
		return this.database;
	}

	private SQLiteDatabase openDatabase(String dbfile) {
		try {
			file = new File(dbfile);
			if (!file.exists()) {
				InputStream is = context.getResources().getAssets()
						.open("GT_REGION.db");
				FileOutputStream fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
					fos.flush();
				}
				fos.close();
				is.close();
			}
			database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
			return database;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}
		return null;
	}

	public void closeDatabase() {
		if (this.database != null)
			this.database.close();
	}

	public List<CityListItem> querySheng(){
		List<CityListItem> shengList = null;
		try {
			shengList = new ArrayList<CityListItem>();
			String sql = "SELECT REGION_ID,REGION_NAME,REGION_CODE,IS_PARENT FROM GT_REGION WHERE LENGTH(REGION_CODE) = 2 ORDER BY REGION_ID";
			Cursor cursor = getDatabase().rawQuery(sql, null);
			while (cursor.moveToNext()) {
				String code = cursor.getString(cursor
						.getColumnIndex("REGION_ID"));
				String name = cursor.getString(cursor
						.getColumnIndex("REGION_NAME"));
				CityListItem myListItem = new CityListItem();
				myListItem.setName(name);
				myListItem.setPcode(code);
				shengList.add(myListItem);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return shengList;
	}
	
	public List<CityListItem> queryShi(String pcode){
		List<CityListItem> shiList = null;
		try {
			shiList = new ArrayList<CityListItem>();
			String sql = "SELECT REGION_ID,REGION_NAME,REGION_CODE,IS_PARENT FROM GT_REGION WHERE REGION_ID LIKE '"
					+ pcode
					+ "%' AND LENGTH(REGION_CODE) = 4 ORDER BY REGION_ID";
			Cursor cursor = getDatabase().rawQuery(sql, null);
			while (cursor.moveToNext()) {
				String code = cursor.getString(cursor
						.getColumnIndex("REGION_ID"));
				String name = cursor.getString(cursor
						.getColumnIndex("REGION_NAME"));
				CityListItem myListItem = new CityListItem();
				myListItem.setName(name);
				myListItem.setPcode(code);
				shiList.add(myListItem);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return shiList;
	}
	
	public List<CityListItem> queryQu(String pcode){
		List<CityListItem> quList = null;
		try {
			quList = new ArrayList<CityListItem>();
			String sql = "SELECT REGION_ID,REGION_NAME,REGION_CODE,IS_PARENT FROM GT_REGION WHERE REGION_ID LIKE '"
					+ pcode
					+ "%'AND LENGTH(REGION_CODE) = 6 ORDER BY REGION_ID";
			Cursor cursor = getDatabase().rawQuery(sql, null);
			while (cursor.moveToNext()) {
				String code = cursor.getString(cursor
						.getColumnIndex("REGION_ID"));
				String name = cursor.getString(cursor
						.getColumnIndex("REGION_NAME"));
				CityListItem myListItem = new CityListItem();
				myListItem.setName(name);
				myListItem.setPcode(code);
				quList.add(myListItem);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return quList;
	}
}