package edu.buffalo.cse.cse486586.groupmessenger.message;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{

	private static final long serialVersionUID = 1L;
private String message;
private ArrayList<Integer>process_send_state;
private final int avd_no;
private boolean order;
private int seqno;

public Message (String message,ArrayList <Integer>current_state,int avd_no){
	this.avd_no=avd_no;
	this.message=message;
	this.process_send_state=current_state;
} 

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}

public int getavd_no() {
	return avd_no;
}

public ArrayList<Integer>getsender_state(){
	return process_send_state;
}

public boolean isOrder() {
	return order;
}

public void setOrder(boolean order) {
	this.order = order;
}

public int getSeqno() {
	return seqno;
}

public void setSeqno(int seqno) {
	this.seqno = seqno;
}



}
