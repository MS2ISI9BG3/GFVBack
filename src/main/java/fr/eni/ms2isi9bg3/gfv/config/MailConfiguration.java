package fr.eni.ms2isi9bg3.gfv.config;

import io.github.jhipster.config.JHipsterConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class MailConfiguration {

	@Value("${spring.mail.default-encoding}")
	String encoding;

	@Value("${spring.mail.host}")
	String mailHost;

	@Value("${spring.mail.port}")
	int port;

	@Value("${spring.mail.username}")
	String username;

	@Value("${spring.mail.password}")
	String password;

	@Value("${spring.mail.protocol}")
	String protocol;

	@Value("${spring.mail.properties.mail.smtp.auth}")
	String auth;

	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	String startTLS;

	@Bean
	@Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
	public JavaMailSenderImpl javaMailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(mailHost);
		javaMailSender.setPort(port);

		javaMailSender.setDefaultEncoding(encoding);

		if (auth.equalsIgnoreCase("true")) {
			javaMailSender.setUsername(username);
			javaMailSender.setPassword(password);
		}

		Properties props = javaMailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", protocol);
		props.put("mail.smtp.auth", auth);
		props.put("mail.smtp.starttls.enable", startTLS);
		props.put("mail.smtp.ssl.trust", mailHost);

		//javaMailSender.setJavaMailProperties(props);

		return javaMailSender;
	}
}
