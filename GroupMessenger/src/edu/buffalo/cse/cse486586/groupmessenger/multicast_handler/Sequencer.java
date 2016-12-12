package edu.buffalo.cse.cse486586.groupmessenger.multicast_handler;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;
import edu.buffalo.cse.cse486586.groupmessenger.message.Message;

public class Sequencer implements Runnable{

	private Message message;
	private int Seqno;
	private String[] remotePort;

	public Sequencer(Message message,int Seqno, String[] remotePort){
		this.message=message;
		this.Seqno=Seqno;
		this.remotePort=remotePort;
	}
	
	@Override
	public void run() {
	    message.setSeqno(Seqno+1);
		message.setOrder(true);
		for (int i=0;i<5;i++){
			try {
				Socket socket=new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
				        Integer.parseInt(remotePort[i]));
				ObjectOutputStream outSocket=new ObjectOutputStream(socket.getOutputStream());
	            outSocket.writeObject(message);
	            outSocket.flush();
	            outSocket.close();                
	            socket.close();
	            Log.v("Sequencer","Message sent(B-multicast)");
	            
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	return;	 
	}

}
