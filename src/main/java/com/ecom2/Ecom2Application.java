package com.ecom2;

import com.ecom2.role.ERole;
import com.ecom2.role.Role;
import com.ecom2.role.RoleRepository;
import com.ecom2.user.User;
import com.ecom2.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class Ecom2Application{

	public static void main(String[] args) {
		SpringApplication.run(Ecom2Application.class, args);
	}

}
