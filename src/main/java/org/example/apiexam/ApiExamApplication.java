package org.example.apiexam;

import org.example.apiexam.model.Bocata;
import org.example.apiexam.model.Bread;
import org.example.apiexam.model.User;
import org.example.apiexam.repository.BocataRepository;
import org.example.apiexam.repository.BreadRepository;
import org.example.apiexam.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ApiExamApplication {

	public static void main(String[] args) {

		ApplicationContext context  = SpringApplication.run(ApiExamApplication.class, args);

		BocataRepository bocataRepository = context.getBean(BocataRepository.class);
		BreadRepository breadRepository = context.getBean(BreadRepository.class);
		UserRepository userRepository = context.getBean(UserRepository.class);

		Bread cristian = breadRepository.save(new Bread(null,"Cristian",true));
		bocataRepository.save(new Bocata(null,"Cristian", 100,cristian));


		Bread mallorqui = breadRepository.save(new Bread(null,"Blanc Mallorquí", false));
		Bread moreno = breadRepository.save(new Bread(null,"Moreno Mallorquí", false));
		Bread baguete = breadRepository.save(new Bread(null,"Baguette", false));
		Bread blatsarrai = breadRepository.save(new Bread(null,"Blat sarraí", true));
		Bread arros = breadRepository.save(new Bread(null,"Arròs integral", true));
		Bread cereals = breadRepository.save(new Bread(null,"Baguette cereals", false));


		bocataRepository.save(new Bocata(null, "Pamboli formatge", 1.90, mallorqui));
		bocataRepository.save(new Bocata(null, "Pamboli formatge", 2, moreno));
		bocataRepository.save(new Bocata(null, "Pamboli sobrassada", 2.10, mallorqui));
		bocataRepository.save(new Bocata(null, "Pamboli sobrassada", 2.20, moreno));
		bocataRepository.save(new Bocata(null, "Baguette Formatge-york", 2.30, baguete));
		bocataRepository.save(new Bocata(null, "Bomba", 3.50, baguete));
		bocataRepository.save(new Bocata(null, "Vegetal tonyina", 3.10, blatsarrai));
		bocataRepository.save(new Bocata(null, "Fuagras", 1.70, blatsarrai));
		bocataRepository.save(new Bocata(null, "Fuagras", 1.80, moreno));
		bocataRepository.save(new Bocata(null, "Spicy Tuna", 4.10, arros));
		bocataRepository.save(new Bocata(null, "Chukrut!!!", 3.90, arros));
		bocataRepository.save(new Bocata(null, "Pizza boom", 3.55, baguete));
		bocataRepository.save(new Bocata(null, "Vegetal Salmó", 4, cereals));
		bocataRepository.save(new Bocata(null, "Salami picant", 3.85, cereals));

		List<String> userRoles = new ArrayList<>();
		userRoles.add("PROPIETARI");


		userRepository.save(User.builder()
				.name("Pere")
				.email("pere@pere.com")
				.password(BCrypt.hashpw("1234", BCrypt.gensalt()))
				.roles(userRoles)
				.build());

		userRoles = new ArrayList<>();
		userRoles.add("CUINER");

		userRepository.save(User.builder()
				.name("Paco")
				.email("paco@paco.com")
				.password(BCrypt.hashpw("1234", BCrypt.gensalt()))
				.roles(userRoles)
				.build());
	}

}
