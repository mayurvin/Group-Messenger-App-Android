package edu.buffalo.cse.cse486586.groupmessenger;


import edu.buffalo.cse.cse486586.groupmessenger.ContentProvider.MessageStore;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.database.sqlite.*;

/*
 * GroupMessengerProvider is a key-value table. Once again, please note that we do not implement
 * full support for SQL as a usual ContentProvider does. We re-purpose ContentProvider's interface
 * to use it as a key-value table.
 * 
 * Please read:
 * 
 * http://developer.android.com/guide/topics/providers/content-providers.html
 * http://developer.android.com/reference/android/content/ContentProvider.html
 * 
 * before you start to get yourself familiarized with ContentProvider.
 * 
 * There are two methods you need to implement---insert() and query(). Others are optional and
 * will not be tested.
 *
 *
 */

public class GroupMessengerProvider extends ContentProvider {
	public static MessageStore messagesdb;
    public static SQLiteDatabase db;
    int algo=SQLiteDatabase.CONFLICT_REPLACE;
	@Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // You do not need to implement this.
    	
    	return null;
        
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db.insertWithOnConflict("Messages", null, values,algo);
        Log.v("insert", values.toString());
        return null;
    	}
    	

    @Override
    public boolean onCreate() {
        // If you need to perform any one-time initialization task, please do it here.
       messagesdb=new MessageStore(this.getContext());
       if (messagesdb!=null){
    	   db=messagesdb.getWritableDatabase();
    	   Log.v("create","Database created");
    	   return true;
       }
    	return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
    	String select="Select key,value From Messages Where key="+"'"+selection+"'";
    	Cursor queryCursor=db.rawQuery(select, selectionArgs);
        if(queryCursor==null)Log.v("query","Databases Query");
        return queryCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }
}
