package co.com.elkin.apps.taskmanagerapi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import co.com.elkin.apps.taskmanagerapi.security.JwtTokenAuthorizationOncePerRequestFilter;
import co.com.elkin.apps.taskmanagerapi.security.JwtUnAuthorizedResponseAuthenticationEntryPoint;
import co.com.elkin.apps.taskmanagerapi.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${jwt.get.token.uri}")
	private String TOKEN_URI;

	@Autowired
	private CustomUserDetailsService userDetailsService;

//	@Autowired
//	private UserDetailsService jwtInMemoryUserDetailsService;
//	@Autowired
//	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenAuthorizationOncePerRequestFilter jwtAuthenticationTokenFilter;
	@Autowired
	private JwtUnAuthorizedResponseAuthenticationEntryPoint jwtUnAuthorizedResponseAuthenticationEntryPoint;

//	@Override
//	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userDetailsService);
//	}

	@Autowired
	public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
//		return new BCryptPasswordEncoder();
//		return new Pbkdf2PasswordEncoder();
//		return new SCryptPasswordEncoder();
//		return new DelegatingPasswordEncoder(TOKEN_URI, null);
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(final HttpSecurity httpSecurity) throws Exception {

//		httpSecurity.ignoring().antMatchers(TOKEN_URI);

//		httpSecurity.csrf().disable().authorizeRequests().antMatchers(TOKEN_URI).permitAll().anyRequest()
//				.authenticated();

//		httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
//		httpSecurity.authorizeRequests().antMatchers("/").permitAll().and()
//        .authorizeRequests().antMatchers(TOKEN_URI).permitAll();
//		httpSecurity.csrf().disable(); 
//		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		httpSecurity.csrf().disable().exceptionHandling()
				.authenticationEntryPoint(jwtUnAuthorizedResponseAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests().anyRequest()
				.authenticated();
		httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(final WebSecurity webSecurity) throws Exception {
		webSecurity.ignoring().antMatchers(HttpMethod.POST, TOKEN_URI).antMatchers(HttpMethod.OPTIONS, "/**").and()
				.ignoring().antMatchers(HttpMethod.GET, "/" // Other Stuff You want to Ignore
				).and().ignoring().antMatchers("/h2-console/**/**");// Should not be in Production!
	}

}
