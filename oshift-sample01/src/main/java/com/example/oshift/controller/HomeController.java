package com.example.oshift.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.oshift.config.ConfigBean;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	ConfigBean configBean;

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
		Map<String, String> systemProps = new HashMap<>();
		for (String key : System.getProperties().stringPropertyNames()) {
			systemProps.put(key, System.getProperty(key));
		}
		model.addAttribute("systemProps", systemProps);

		// アドレス
		List<InetAddress> addressList = new ArrayList<>();
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface network = interfaces.nextElement();
				Enumeration<InetAddress> addresses = network.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = addresses.nextElement();
					addressList.add(address);
				}

			}
		} catch (SocketException e) {
			// 無視
		}
		model.addAttribute("addressList", addressList);

		HttpSession session = request.getSession(true);
		model.addAttribute("sessionId", session.getId());

		AtomicInteger counter = (AtomicInteger) session.getAttribute("counter");
		if (counter == null) {
			counter = new AtomicInteger(0);
			session.setAttribute("counter", counter);
		}

		model.addAttribute("counter", counter.incrementAndGet());
		model.addAttribute("title", configBean.getTitle());

		return "home";
	}

	/// clearCount
	@RequestMapping(value = "/clearCount", method = RequestMethod.POST)
	public String clearCount(Locale locale, HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		session.invalidate();
		return "redirect:/";
	}

	@RequestMapping(value = "/testPage", method = RequestMethod.GET)
	public String testPage(Locale locale, HttpServletRequest request, Model model) {
		List<String> eapHomeList = new ArrayList<>();
		File eapHome = new File(System.getenv("JBOSS_HOME"));
		for (File file : eapHome.listFiles()) {
			eapHomeList.add(file.getName());
		}
		model.addAttribute("eapHomeList", eapHomeList);

		List<String> configurationList = new ArrayList<>();
		File configuration = new File(eapHome, "standalone/configuration");
		for (File file : configuration.listFiles()) {
			configurationList.add(file.getName());
		}
		model.addAttribute("configurationList", configurationList);

		return "testPage";
	}
	@Resource
	private ServletContext servletContext;
	@RequestMapping(value = "/dl-config", method = RequestMethod.POST)
	public void downloadConfig(Locale locale, HttpServletResponse response, Model model, @RequestParam("fileName") String fileName) {
		File eapHome = new File(System.getenv("JBOSS_HOME"));
		File configuration = new File(eapHome, "standalone/configuration");
		File downloadFile = new File(configuration,fileName);

		response.setContentType(servletContext.getMimeType(FilenameUtils.getExtension(fileName) ));
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

		try {
			OutputStream out = response.getOutputStream();
			FileUtils.copyFile(downloadFile, out);
			out.flush();
		} catch (IOException e) {
			logger.warn("入出力エラー", e);
			return;
		}
	}

}
