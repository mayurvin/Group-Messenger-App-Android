package edu.buffalo.cse.cse486586.groupmessenger.ContentProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

public class WriteToContentProvider 

{
	protected Uri mUri;
	
	
	private Uri buildUri(String content, String authority) {
    	Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(content);
        return uriBuilder.build();
    	}
    
	
	    public void loadValues(String msg,Integer msg_id,ContentResolver cr) {
	    	mUri = buildUri("content", "edu.buffalo.cse.cse486586.groupmessenger.provider");//Set Content Provider URI
	    	ContentValues cv = new ContentValues();
	  	     cv.put("key",msg_id.toString());
	  	     cv.put("value",msg);
	  	     cr.insert(mUri, cv);
	  	     Log.v("Inserted key",msg_id.toString());
	}

}
