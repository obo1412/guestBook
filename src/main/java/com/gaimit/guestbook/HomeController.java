package com.gaimit.guestbook;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gaimit.guestbook.model.GuestBook;
import com.gaimit.guestbook.service.GuestBookService;
import com.gaimit.helper.WebHelper;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	WebHelper web;
	
	@Autowired
	GuestBookService guestBookService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value= {"/", "/index.do"})
	public ModelAndView doRun(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		web.init();
		
		GuestBook data = new GuestBook();
		String data2 = String.valueOf(data);
		
		logger.debug(data2);
		
		List<GuestBook> eventList = null;
		try {
			guestBookService.insertEvent(data);
			eventList = guestBookService.selectEventList(data);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		model.addAttribute("eventList", eventList);
		
		// "/WEB-INF/views/index.jsp"파일을 View로 사용한다.
		return new ModelAndView("index");
	}
	
}
