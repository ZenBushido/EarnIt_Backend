package com.mobiledi.earnitapi;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.Parent;
import com.mobiledi.earnitapi.repository.AccountRepository;
import com.mobiledi.earnitapi.util.AppConstants;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class EarnitApiApplication extends SpringBootServletInitializer {

	static {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	private static Log logger = LogFactory.getLog(EarnitApiApplication.class);

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
				logger.info("ServletContext initialized");
			}

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				logger.info("ServletContext destroyed");
			}

		};
	}

}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
	private static Log logger = LogFactory.getLog(WebSecurityConfiguration.class);

	@Autowired
	AccountRepository accountRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		logger.info("WebSecurityConfiguration initialized..");
		auth.userDetailsService(userDetailsService());
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				Parent account = accountRepository.findParentByEmail(username);
				if (account != null) {
					logger.info("Found User with email: " + username);

					return new User(account.getEmail(), account.getPassword(), true, true, true, true,
							AuthorityUtils.createAuthorityList(AppConstants.USER_PARENT));
				} else {
					Children child = accountRepository.findChildByEmail(username);
					if (child != null)
						return new User(child.getEmail(), child.getPassword(), true, true, true, true,
								AuthorityUtils.createAuthorityList(AppConstants.USER_CHILD));
					else
						throw new UsernameNotFoundException("could not find the user '" + username + "'");
				}
			}

		};
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static Log logger = LogFactory.getLog(WebSecurityConfig.class);

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.info("Configuring WebSecurity");
		// http.authorizeRequests().antMatchers("/login").permitAll().anyRequest().fullyAuthenticated().and().httpBasic()
		// .and().csrf().disable();
		http.authorizeRequests().antMatchers("/login", "/signup/parent", "/hello", "/sendPush/*", "/passwordReminder").permitAll()
				.antMatchers("/childrens").hasAuthority(AppConstants.USER_PARENT).anyRequest().fullyAuthenticated()
				.and().httpBasic().and().csrf().disable();

	}

}