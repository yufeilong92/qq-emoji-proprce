package com.pccw;

import java.io.Serializable;


public class MessageInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String usermac;
	private String msg;
	private int protrait;
	
	public MessageInfo() {}
	
	public MessageInfo(String usermac, String msg,int protrait) {
		this.usermac = usermac;
		this.msg = msg;
		this.protrait = protrait;
	}

	
	
	public String getUsermac() {
		return usermac;
	}

	public void setUsermac(String usermac) {
		this.usermac = usermac;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getProtrait() {
		return protrait;
	}

	public void setProtrait(int protrait) {
		this.protrait = protrait;
	}
	
	
}
