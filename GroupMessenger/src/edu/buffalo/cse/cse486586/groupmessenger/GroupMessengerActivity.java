package edu.buffalo.cse.cse486586.groupmessenger;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


import edu.buffalo.cse.cse486586.groupmessenger.message.Message;
import edu.buffalo.cse.cse486586.groupmessenger.multicast_handler.MessageReceiver;
import edu.buffalo.cse.cse486586.groupmessenger.multicast_handler.SendToSequencer;
import edu.buffalo.cse.cse486586.groupmessenger.multicast_handler.Sequencer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

/**
 * GroupMessengerActivity is the main Activity for the assignment.
 * 
 *
 */
public class GroupMessengerActivity extends Activity {
	static final String []REMOTE_PORT={"11108","11112","11116","11120","11124"};
    static final int SERVER_PORT = 10000;
    static final String TAG = GroupMessengerActivity.class.getSimpleName();
    public int Sequenceno=-1;
    public boolean isSequencer;
    private int avd_no=0;
    public ArrayList<Integer> current_state;
    public Message message = null;
    public MessageReceiver msgrcv; 
      
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messenger);
        
        TelephonyManager tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        String myPort = String.valueOf((Integer.parseInt(portStr) * 2));
        isSequencer=myPort.equals("11108");    
                             
        msgrcv=new MessageReceiver(this.getContentResolver());
        
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());
        
        TextView txtvw=(TextView) findViewById(R.id.editText1);
        /*
         * Registers OnPTestClickListener for "button1" in the layout, which is the "PTest" button.
         * OnPTestClickListener demonstrates how to access a ContentProvider.
         */
        
        findViewById(R.id.button1).setOnClickListener(
                new OnPTestClickListener(tv, getContentResolver()));
               
        
        /*
         * Registers SendToSequencer for "button4" in the layout, which is the "Send" button.
         *  
         */
        Button senddata=(Button) findViewById(R.id.button4);
        senddata.setClickable(true);
        senddata.setOnClickListener(
        		new SendToSequencer(myPort,txtvw,tv,message,REMOTE_PORT[0],avd_no));
       
                
        try {
        	ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
        } catch (IOException e) 
        {
        	Log.e(TAG, "Can't create a ServerSocket");
            return;
        }
        
        
                       
    }
        
  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_group_messenger, menu);
        return true;
    }

    //Server receiver
    private class ServerTask extends AsyncTask<ServerSocket, Message, Void> {

        @Override
        
        protected Void doInBackground(ServerSocket... sockets) {
            ServerSocket serverSocket = sockets[0];
    while(true){
            try{
            	Socket clientSocket=serverSocket.accept();
        	    ObjectInputStream inSocket=new ObjectInputStream(clientSocket.getInputStream());
                try {
					message = (Message)inSocket.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}	    
                if (message!=null)
                publishProgress(message);
                clientSocket.close();
                    	
            }
            catch (IOException e){Log.e(TAG, "Socket write failed");}
            
           /*
             * TODO: Fill in your server code that receives messages and passes them
             * to onProgressUpdate().
             */
    }
        }

        protected void onProgressUpdate(Message...messages) {
            /*
             * The following code displays what is received in doInBackground().
             */
            if ((!messages[0].isOrder()) && (isSequencer))
            {
            	Thread thread=new Thread(new Sequencer(messages[0],Sequenceno,REMOTE_PORT));
            	Log.v(TAG, messages[0].getMessage());
            	thread.start();
            	
            }
            else
            {
              Sequenceno=msgrcv.checkvalidity(message,current_state,Sequenceno);            	
              Log.v("Messsage","buffered");
              String strReceived = messages[0].getMessage().trim();
              TextView localTextView = (TextView) findViewById(R.id.textView1);
              localTextView.append(strReceived + "\t\n");
              localTextView.append("\n");
              	
            }
        	
        	           
        }
    }












}


