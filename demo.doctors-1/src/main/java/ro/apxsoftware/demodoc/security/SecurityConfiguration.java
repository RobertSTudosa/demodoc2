package ro.apxsoftware.demodoc.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ro.apxsoftware.demodoc.dao.UserRepository;
import ro.apxsoftware.demodoc.service.UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	UserService userServ;
	
	
	@Autowired(required=true)
	UserRepository userAccountRepo;

	@Autowired
	BCryptPasswordEncoder bCryptEncoder;
	

	
	@Bean
	public AuthenticationSuccessHandler successHandler() {
	    SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
	    handler.setUseReferer(true);
	 
	    return handler;
	}
	


	//--> set for custom authenticationProvider
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) {
//		auth.authenticationProvider(authenticationProvider());
//	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth
//		.authenticationProvider(authenticationProvider) // <---- this is how to call a custom auth provider
			.jdbcAuthentication()	
			.dataSource(dataSource)
			.usersByUsernameQuery("select username, password, enable " + 
									" from user_accounts where username = ? ")
			.authoritiesByUsernameQuery("select  user_accounts.username, userrole.permission from userrole " + 
					" join  user_role on user_role.role_id= userrole.role_id " + 
					" join user_accounts on user_accounts.user_id = user_role.user_id "
					+ " where username = ? ")
			.passwordEncoder(bCryptEncoder);//used to decrypt/decode the password
	}
	
	//collate latin1_general_cs
	
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.authorizeRequests()
            .antMatchers("/home").hasAuthority("USER")
            .antMatchers("/","/**", "/css/**","/js/**","/assets/**","/static/**","/vendor/**","/pacient/assests/**").permitAll()
            .and()
            .formLogin()
            .loginPage("/login")
            .failureUrl("/login-error")
//            .usernameParameter("email")
            .successHandler(successHandler())
            .permitAll().defaultSuccessUrl("/", true)
            .and()
            .rememberMe().tokenValiditySeconds(2925000)
        	.and()
            .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
            .invalidateHttpSession(true).deleteCookies("JSESSIONID");         
    }
	
	@Bean
	@Override 
	public AuthenticationManager authenticationManagerBean() 
			throws Exception { 		
		return super.authenticationManagerBean(); 	
		}
	
//	@Bean
//	DaoAuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//		daoAuthenticationProvider.setPasswordEncoder(bCryptEncoder);
//		daoAuthenticationProvider.setUserDetailsService(userServ);
//		return daoAuthenticationProvider;
//	}
	

}
