package com.gaimit.guestbook.service;

import java.util.List;

import com.gaimit.guestbook.model.GuestBook;


public interface GuestBookService {
	public void insertEvent(GuestBook guestBook) throws Exception;
	
	public List<GuestBook> selectEventList(GuestBook guestBook) throws Exception;
}
