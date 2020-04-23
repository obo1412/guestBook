package com.gaimit.guestbook.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gaimit.guestbook.model.GuestBook;
import com.gaimit.guestbook.service.GuestBookService;

//--> import org.springframework.stereotype.Service; 
@Service
public class GuestBookServiceImpl implements GuestBookService {

	/** ó�� ����� ����� Log4J ��ü ���� */
	// --> import org.slf4j.Logger;
	// --> import org.slf4j.LoggerFactory;
	private static Logger logger = LoggerFactory.getLogger(GuestBookServiceImpl.class);

	/** MyBatis */
	// --> import org.springframework.beans.factory.annotation.Autowired;
	// --> import org.apache.ibatis.session.SqlSession
	@Autowired
	SqlSession sqlSession;

	@Override
	public void insertEvent(GuestBook guestBook) throws Exception {
		try {
			int result = sqlSession.insert("GuestBookMapper.insertEvent", guestBook);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("����� ��� ������ �����ϴ�.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("��� ���� ���忡 �����߽��ϴ�.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public List<GuestBook> selectEventList(GuestBook guestBook) throws Exception {
		List<GuestBook> result = null;
		try {
			result = sqlSession.selectList("GuestBookMapper.selectEventList", guestBook);
		} catch (Exception e) {
			throw new Exception("������ ��ȸ�� �����߽��ϴ�.");
		}
		return result;
	}

}
