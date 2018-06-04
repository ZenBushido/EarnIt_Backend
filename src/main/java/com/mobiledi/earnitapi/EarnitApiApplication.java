package com.mobiledi.earnitapi;

import com.mobiledi.earnitapi.util.AppConstants;
import java.util.TimeZone;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class EarnitApiApplication extends SpringBootServletInitializer {

	static {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EarnitApiApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(EarnitApiApplication.class, args);

	}

    @Bean
    public TaskScheduler taskScheduler() {
        ConcurrentTaskScheduler taskScheduler = new ConcurrentTaskScheduler();
        return taskScheduler;
    }

	@Bean
	protected ServletContextListener listener() {
		return new ServletContextListener() {

			@Override
			public void contextInitialized(ServletContextEvent sce) {
				log.info("ServletContext initialized");
			}

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				log.info("ServletContext destroyed");
			}

		};
	}

}

@Slf4j
@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	ApplicationContext applicationContext;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		log.info("WebSecurityConfiguration initialized..");
		auth.authenticationProvider(applicationContext.getBean(DaoAuthenticationProvider.class));
	}

}

@Slf4j
@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("Configuring WebSecurity");
		// http.authorizeRequests().antMatchers("/login").permitAll().anyRequest().fullyAuthenticated().and().httpBasic()
		// .and().csrf().disable();
		http.authorizeRequests().antMatchers("/encryptpassword", "/login", "/signup/parent", "/hello", "/sendPush/*", "/passwordReminder").permitAll()
				.antMatchers("/childrens").hasAuthority(AppConstants.USER_PARENT).anyRequest().fullyAuthenticated()
				.and().httpBasic().and().csrf().disable();

	}

}