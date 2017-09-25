package com.example.oshift.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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
		List<String> eapHomeList = new ArrayList<>();
		String jbossHome = System.getenv("JBOSS_HOME");
		File eapHome = new File(jbossHome);
		model.addAttribute("jbossHome",jbossHome);
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

	@RequestMapping(value = "/prev-file", method = RequestMethod.POST)
	public String prevFile(Locale locale, HttpServletResponse response, Model model,
			@RequestParam("filePath") String filePath) throws Exception {
		// prevText
		File file = new File(filePath);
		if (file.exists()) {
			model.addAttribute("prevText", "ファイルがありません");
		} else if (file.isDirectory()) {
			model.addAttribute("prevText", "[DIR]\n"+String.join("\n", file.list()));

		} else {
			model.addAttribute("prevText", FileUtils.readFileToString(file, "UTF-8"));
		}
		return "testPage";
	}

	@Resource
	private ServletContext servletContext;

	@RequestMapping(value = "/exec-run", method = RequestMethod.POST)
	public String execRun(Locale locale, HttpServletResponse response, Model model,
			@RequestParam("command") String command) throws Exception {

		System.out.println("start");
		List<String> commands = new ArrayList<>();
		if (command.contains(" ")) {
			commands.addAll(Arrays.asList(command.split(" ")));
		} else {
			commands.add(command);
		}

		ProcessBuilder pb = new ProcessBuilder();
		pb.redirectErrorStream(true);
		pb.command(commands);
		Process process = pb.start();

		Future<List<String>> future = Executors.newSingleThreadExecutor().submit(() -> {
			System.out.println("callable start.");
			List<String> resultList = new ArrayList<>();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				for (;;) {
					String line = br.readLine();
					if (line == null)
						break;
					System.out.println("line=" + line);
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

	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public void download(Locale locale, HttpServletResponse response, Model model,
			@RequestParam("filePath") String filePath) {
		File downloadFile = new File(filePath);
		response.setContentType(servletContext.getMimeType(FilenameUtils.getExtension(downloadFile.getName())));
		response.setHeader("Content-Disposition", "attachment; filename=" + downloadFile.getName());

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
