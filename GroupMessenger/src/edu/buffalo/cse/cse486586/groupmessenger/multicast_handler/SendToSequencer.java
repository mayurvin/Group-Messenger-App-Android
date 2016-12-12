package edu.buffalo.cse.cse486586.groupmessenger.multicast_handler;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import edu.buffalo.cse.cse486586.groupmessenger.message.*;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class SendToSequencer implements OnClickListener {
	
	private TextView EditTextView=null;
	private TextView DisplayView=null;
    private Message message;
	private String myport="";
    private int avd_no;
	protected String Remote_Port0;
	private ArrayList<Integer> process_state=new ArrayList<Integer>();
        
    static final String TAG = SendToSequencer.class.getSimpleName();   
    
     public SendToSequencer(String myPort,TextView EditTextView,TextView DisplayView,Message message,final String REMOTE_PORT,int avd_no)
 {
    	 this.EditTextView=EditTextView;
    	 this.myport=myPort;   
    	 this.message=message;
         this.DisplayView=DisplayView;
         this.Remote_Port0=REMOTE_PORT;
         this.avd_no=avd_no;
 }       


@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
    EditText editText = (EditText)EditTextView;
	String msg = editText.getText().toString() + "\n";
	editText.setText(""); // This is one way to reset the input box.
	message=new Message(msg,process_state,avd_no);
	Log.v("onClick", "ViewModify");
    
    TextView localTextView = (TextView)DisplayView;
    localTextView.append("\t" + msg); // This is one way to display a string.
    
    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, msg, myport);

}

private class ClientTask extends AsyncTask<String,Void, Void> {

    @Override
    protected Void doInBackground(String... msgs) {
        try {
        	
        	Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                    Integer.parseInt(Remote_Port0));
            
            ObjectOutputStream outSocket=new ObjectOutputStream(socket.getOutputStream());
            outSocket.writeObject(message);
            outSocket.flush();
            outSocket.close();                
            socket.close();
            }
            catch (UnknownHostException e) {
            Log.e(TAG, "ClientTask UnknownHostException");
        } catch (IOException e) {
            Log.e(TAG, "ClientTask socket IOException");
        }

        return null;
    }
}
}





