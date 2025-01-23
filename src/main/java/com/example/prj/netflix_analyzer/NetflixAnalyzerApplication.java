package com.example.prj.netflix_analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@SpringBootApplication
@Controller
public class NetflixAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetflixAnalyzerApplication.class, args);
	}

	@GetMapping
	public String getHome() {
		return "home";
	}
}
