package org.september.colorful.qrcode.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
public class ApplicationStartupListener implements CommandLineRunner {


	@Autowired
	protected WebApplicationContext applicationContext;


	@Override
	public void run(String... args) {
	}
}
