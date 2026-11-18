package org.pdsr;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailSenderConfig {

	@Bean
//	public JavaMailSender getJavaMailSender() {
	JavaMailSender getJavaMailSender() {
		   JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		    mailSender.setHost("smtp.gmail.com");
		    mailSender.setPort(587);
		    
		    mailSender.setUsername("urgesafe142@gmail.com");
		    mailSender.setPassword("jkseasjaprmageum");
		    
		    Properties props = mailSender.getJavaMailProperties();
		    props.put("mail.transport.protocol", "smtp");
		    props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true");
		   
		    props.put("mail.smtp.connectiontimeout", "10000"); // 5 seconds
		    props.put("mail.smtp.timeout", "10000");           // 5 seconds
		    props.put("mail.smtp.writetimeout", "10000");
		    props.put("mail.debug", "true");
		    
		    
		    return mailSender;
	}
		 
}
