package com.tehyt.videoConferenceToolBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class VideoConferenceToolBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoConferenceToolBeApplication.class, args);
	}

}
