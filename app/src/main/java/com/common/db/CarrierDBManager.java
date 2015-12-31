package com.common.db;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.common.model.AgentCarrier;
import com.gt.officeagent.R;
 
public class CarrierDBManager {
    private final int BUFFER_SIZE = 1024;
    public static final String DB_NAME = "carriers2.db";
    public static final String PACKAGE_NAME = "com.gt.officeagent";
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"+ PACKAGE_NAME;
    private SQLiteDatabase database;
    private Context context;
    private File file=null;
    
    public static CarrierDBManager dbmanager = null;

	public static CarrierDBManager getDBManager(Context context) {
		if (dbmanager == null) {
			dbmanager = new CarrierDBManager(context);
		}
		return dbmanager;
	}
    public CarrierDBManager(Context context) {
        this.context = context;
    }
 
    public void openDatabase() {
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }
    public SQLiteDatabase getDatabase(){
    	return this.database;
    }
 
    private SQLiteDatabase openDatabase(String dbfile) {
        try {
        	file = new File(dbfile);
            if (!file.exists()) {
            	InputStream is = context.getAssets().open("carriers2.db");
            	FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count =is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                	fos.flush();
                }
                fos.close();
                is.close();
            }
            database = SQLiteDatabase.openOrCreateDatabase(dbfile,null);
            return database;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
        }
        return null;
    }
    public void closeDatabase() {
    	if(this.database!=null)
    		this.database.close();
    }
    
    public  List<AgentCarrier> getCarrierList() {
    	dbmanager.openDatabase();
		database = dbmanager.getDatabase();
		ArrayList<AgentCarrier> localArrayList = new ArrayList<AgentCarrier>();
		try {
			String str1 = "SELECT carrier_id,carrier_code,carrier_name,carrier_phone FROM gt_carrier ORDER BY carrier_id";
			Cursor c = database.rawQuery(str1, null);
			while (c.moveToNext()) {
				int id = c.getInt(c.getColumnIndex("carrier_id"));
				String code = c.getString(c.getColumnIndex("carrier_code"));
				String name = c.getString(c.getColumnIndex("carrier_name"));
				String phone = c.getString(c.getColumnIndex("carrier_phone"));
				AgentCarrier tmp = new AgentCarrier();
				tmp.setParentCarrierId((long) id);
				tmp.setParentCarrierName(name);
				tmp.setParentCarrierCode(code);
				tmp.setAgentCarrierPhone(phone);
				localArrayList.add(tmp);

			}
			dbmanager.closeDatabase();
			database.close();

		} catch (Exception localException) {
			return null;
		}
		return localArrayList;
	}
    public  AgentCarrier getCarrierById(String carrierid) {
    	dbmanager.openDatabase();
    	database = dbmanager.getDatabase();
    	AgentCarrier carrier = new AgentCarrier();
    	try {
    		String str1 = "SELECT carrier_id,carrier_code,carrier_name,carrier_phone FROM gt_carrier where carrier_id="+carrierid;
    		Cursor c = database.rawQuery(str1, null);
    		while (c.moveToNext()) {
    			int id = c.getInt(c.getColumnIndex("carrier_id"));
    			String code = c.getString(c.getColumnIndex("carrier_code"));
    			String name = c.getString(c.getColumnIndex("carrier_name"));
    			String phone = c.getString(c.getColumnIndex("carrier_phone"));
    			
    			carrier.setParentCarrierId((long) id);
    			carrier.setParentCarrierName(name);
    			carrier.setParentCarrierCode(code);
    			carrier.setAgentCarrierPhone(phone);
    			
    		}
    		dbmanager.closeDatabase();
    		database.close();
    		
    	} catch (Exception localException) {
    		return null;
    	}
    	return carrier;
    }
    public  AgentCarrier getCarrierByName(String carrierName) {
    	dbmanager.openDatabase();
    	database = dbmanager.getDatabase();
    	AgentCarrier carrier = new AgentCarrier();
    	try {
    		String str1 = "SELECT carrier_id,carrier_code,carrier_name,carrier_phone FROM gt_carrier where carrier_name like '%"+carrierName+"%'";
    		Cursor c = database.rawQuery(str1, null);
    		while (c.moveToNext()) {
    			int id = c.getInt(c.getColumnIndex("carrier_id"));
    			String code = c.getString(c.getColumnIndex("carrier_code"));
    			String name = c.getString(c.getColumnIndex("carrier_name"));
    			String phone = c.getString(c.getColumnIndex("carrier_phone"));
    			
    			carrier.setParentCarrierId((long) id);
    			carrier.setParentCarrierName(name);
    			carrier.setParentCarrierCode(code);
    			carrier.setAgentCarrierPhone(phone);
    			
    		}
    		dbmanager.closeDatabase();
    		database.close();
    		
    	} catch (Exception localException) {
    		return null;
    	}
    	return carrier;
    }
    public  String getCarrierName(int id) {
    	dbmanager.openDatabase();
    	database = dbmanager.getDatabase();
    	String name="";
    	try {
    		String str1 = "SELECT carrier_name FROM gt_carriers where carrier_id="+id;
    		Cursor c = database.rawQuery(str1, null);
    		while (c.moveToNext()) {
    			name = c.getString(c.getColumnIndex("carrier_name"));
    		}
    		dbmanager.closeDatabase();
    		database.close();
    		
    	} catch (Exception localException) {
    		return null;
    	}
    	return name;
    }
    public  String getCarrierCode(int id) {
    	dbmanager.openDatabase();
    	database = dbmanager.getDatabase();
    	String name="";
    	try {
    		String str1 = "SELECT carrier_code FROM gt_carriers where carrier_id="+id;
    		Cursor c = database.rawQuery(str1, null);
    		while (c.moveToNext()) {
    			name = c.getString(c.getColumnIndex("carrier_code"));
    		}
    		dbmanager.closeDatabase();
    		database.close();
    		
    	} catch (Exception localException) {
    		return null;
    	}
    	return name;
    }
}