package com.tripmite.app;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	// Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "breach";
 
    // Contacts table name
    private static final String TABLE_LOCATION = "location";
 
    // Contacts Table Columns names
    private static final String ID = "id";
    private static final String NAME = "LocationName";
    private static final String LATITUDE = "Latitude";
    private static final String LONGITUDE = "Longitude";
    private static final String COMMENT = "comment";
	private static final String RANGE = "range"; 
    public DatabaseHandler(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION + "("
                + ID + " INTEGER PRIMARY KEY,"+ NAME + " TEXT,"+ COMMENT + " TEXT,"+ RANGE + " TEXT," + LATITUDE + " TEXT,"
                + LONGITUDE + " TEXT" + ")";
        db.execSQL(CREATE_LOCATION_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
		 
	        // Create tables again
	        onCreate(db);
		
	}
	public void addLocation(SavedLocation loc) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(NAME, loc.getName()); 
	    values.put(COMMENT, loc.getComment());
	    values.put(LATITUDE,loc.getLatitude());
	    values.put(LONGITUDE, loc.getLongitude());
	    values.put(RANGE,loc.getRange() );
	    
	    
	    // Inserting Row
	    db.insert(TABLE_LOCATION, null, values);
	    db.close(); // Closing database connection
	}
	public List<SavedLocation> getAllLocations() {
	    List<SavedLocation> locationList = new ArrayList<SavedLocation>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_LOCATION;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            SavedLocation location = new SavedLocation();
	            location.setName(cursor.getString(1));
	            location.setComment(cursor.getString(2));
	            location.setRange(cursor.getString(3));
	            location.setLatitude(cursor.getString(4));
	            location.setLongitude(cursor.getString(5));
	           
	            // Adding contact to list
	            locationList.add(location);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return locationList;
	}
public void DeleteRecord(String name)
{
    SQLiteDatabase db = this.getWritableDatabase();
	db.delete(TABLE_LOCATION, NAME + "=" +"'" + name + "'" , null);
}
}
