package com.pccw;

import java.io.Serializable;
import java.net.InetAddress;


public class UserInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer userid;
	private String usermac;
	private Integer teamid;
	private String username;
	private InetAddress userip;
	private Integer sex;
	private String personsign;
	private int portrait;

	public UserInfo() {

	}

	public UserInfo(String usermac) {
		this.usermac = usermac;
	}

	public UserInfo(Integer userid, String usermac, Integer teamid,
			String username,InetAddress userip, Integer sex, String personsign,int portrait) {
		this.userid = userid;
		this.usermac = usermac;
		this.teamid = teamid;
		this.username = username;
		this.userip = userip;
		this.sex = sex;
		this.personsign = personsign;
		this.portrait = portrait;
	}
	

	public InetAddress getUserip() {
		return userip;
	}

	public void setUserip(InetAddress userip) {
		this.userip = userip;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getPersonsign() {
		return personsign;
	}

	public void setPersonsign(String personsign) {
		this.personsign = personsign;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getUsermac() {
		return usermac;
	}

	public void setUsermac(String usermac) {
		this.usermac = usermac;
	}

	public Integer getTeamid() {
		return teamid;
	}

	public void setTeamid(Integer teamid) {
		this.teamid = teamid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	
	public int getPortrait() {
		return portrait;
	}

	public void setPortrait(int portrait) {
		this.portrait = portrait;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((usermac == null) ? 0 : usermac.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserInfo other = (UserInfo) obj;
		if (usermac == null) {
			if (other.usermac != null)
				return false;
		} else if (!usermac.equals(other.usermac))
			return false;
		return true;
	}
	
	
	
}
