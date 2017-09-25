package com.example.oshift.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Locale;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DsController {
	@RequestMapping(value = "/ds", method = RequestMethod.GET)
	public String home(Locale locale, HttpServletRequest request, Model model) {

		// prevText
		try {
			InitialContext context = new InitialContext();
			DataSource ds = (DataSource) context.lookup("java:jboss/datasources/orangeds_postgresql");
			try (Connection conn = ds.getConnection()) {
				try (Statement st = conn.createStatement()) {
					try (ResultSet rs = st.executeQuery("select now()")) {
						rs.next();
						Timestamp ts = rs.getTimestamp(1);
						model.addAttribute("prevText", "[SUCCESS]\n" + ts.toString());
					}
				}
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			model.addAttribute("prevText", "[ERROR\n]" + sw.toString());
		}

		return "dsview";
	}
}
