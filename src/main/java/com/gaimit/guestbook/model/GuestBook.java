package com.gaimit.guestbook.model;

public class GuestBook {
	private int idEvent;
	private String nameEvent;
	private int idHost;
	private String nameHost;
	private int idGuest;
	private String nameGuest;
	private int celebrationGift;
	private String remarks;
	
	public int getIdEvent() {
		return idEvent;
	}
	public void setIdEvent(int idEvent) {
		this.idEvent = idEvent;
	}
	public String getNameEvent() {
		return nameEvent;
	}
	public void setNameEvent(String nameEvent) {
		this.nameEvent = nameEvent;
	}
	public int getIdHost() {
		return idHost;
	}
	public void setIdHost(int idHost) {
		this.idHost = idHost;
	}
	public String getNameHost() {
		return nameHost;
	}
	public void setNameHost(String nameHost) {
		this.nameHost = nameHost;
	}
	public int getIdGuest() {
		return idGuest;
	}
	public void setIdGuest(int idGuest) {
		this.idGuest = idGuest;
	}
	public String getNameGuest() {
		return nameGuest;
	}
	public void setNameGuest(String nameGuest) {
		this.nameGuest = nameGuest;
	}
	public int getCelebrationGift() {
		return celebrationGift;
	}
	public void setCelebrationGift(int celebrationGift) {
		this.celebrationGift = celebrationGift;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
