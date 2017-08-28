package com.example.oshift.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, HttpServletRequest request, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		// Date date = new Date();
		// DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
		// DateFormat.LONG, locale);
		//
		// String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime",
				DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS").format(LocalDateTime.now()));
		HttpSession session = request.getSession(true);
		model.addAttribute("sessionId", session.getId());

		AtomicInteger counter = (AtomicInteger) session.getAttribute("counter");
		if (counter == null) {
			counter = new AtomicInteger(0);
			session.setAttribute("counter", counter);
		}

		model.addAttribute("counter", counter.incrementAndGet());

		return "home";
	}

	/// clearCount
	@RequestMapping(value = "/clearCount", method = RequestMethod.POST)
	public String clearCount(Locale locale, HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		session.invalidate();
		return "redirect:/";
	}

}
