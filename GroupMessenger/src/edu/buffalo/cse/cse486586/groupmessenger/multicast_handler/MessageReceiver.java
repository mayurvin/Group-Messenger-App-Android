package edu.buffalo.cse.cse486586.groupmessenger.multicast_handler;

import java.util.ArrayList;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.util.Log;
import edu.buffalo.cse.cse486586.groupmessenger.ContentProvider.WriteToContentProvider;
import edu.buffalo.cse.cse486586.groupmessenger.message.Message;

public class MessageReceiver {

	private HashMap<Integer,Message> buffer;
	private int k;
	private final ContentResolver cr;
	
	
	@SuppressLint("UseSparseArrays")
	public MessageReceiver(ContentResolver cr) {
	  buffer=new HashMap<Integer,Message>();
	  this.cr=cr;
	}
    
	public int checkvalidity(Message message,ArrayList<Integer>current_state,int Sequenceno){
		Log.v("Checking Validity","Please Wait");
		buffer.put((Integer)message.getSeqno(), message);
		if (k<message.getSeqno())k=message.getSeqno();
		if (message.getSeqno()==(Sequenceno+1)){
     	int j=Sequenceno+1;
		while(j!=k+1){
			if (!buffer.containsKey(j))break;
			WriteToContentProvider write=new WriteToContentProvider();
			write.loadValues(message.getMessage(), j, cr);
			Log.v("Row",message.getMessage());
			j++;
		}
	Sequenceno=j-1;
	}
	return Sequenceno;
	}


}
