package com.example.oshift.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestpageController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/testPage", method = RequestMethod.GET)
	public String testPage(Locale locale, HttpServletRequest request, Model model) {
		System.out.println("open");
		return "testPage";
	}

	@Resource
	private ServletContext servletContext;

	@RequestMapping(value = "/exec-run", method = RequestMethod.POST)
	public String downloadConfig(Locale locale, HttpServletResponse response, Model model,
			@RequestParam("command") String command) throws Exception {

		System.out.println("start");
		List<String> commands = new ArrayList<>();
		if(command.contains(" ")) {
			commands.addAll(Arrays.asList(command.split(" ")));
		}else {
			commands.add(command);
		}

		ProcessBuilder pb = new ProcessBuilder();
		pb.redirectErrorStream(true);
		pb.command(commands);
		Process process = pb.start();

		Future<List<String>> future =  Executors.newSingleThreadExecutor().submit(()->{
			System.out.println("callable start.");
			List<String> resultList = new ArrayList<>();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				for (;;) {
					String line = br.readLine();
					if (line == null)
						break;
					System.out.println("line="+line);
					resultList.add(line);
				}
			}
			System.out.println("callable end.");
			return resultList;
		});
		process.waitFor();

		model.addAttribute("resultList", future.get());
		System.out.println("end");
		return "testPage";
	}
}
