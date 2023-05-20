package ro.apxsoftware.demodoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import ro.apxsoftware.demodoc.dao.UserRepository;
import ro.apxsoftware.demodoc.service.UserService;

@EnableAsync
@SpringBootApplication
//@ComponentScan({"ro.apxsoftware"})
public class Application {

	@Autowired
	UserService userServ;
	
	@Autowired(required=true)
	UserRepository userAccountRepo;
	
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
