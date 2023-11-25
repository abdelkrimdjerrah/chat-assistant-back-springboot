package epdbackendjava.chatassistant;

import epdbackendjava.chatassistant.Chat.Message;
import epdbackendjava.chatassistant.Chat.Session;
import epdbackendjava.chatassistant.Chat.SessionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Date;

@SpringBootApplication
public class ChatAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatAssistantApplication.class, args);
	}

//	@Bean
//	CommandLineRunner runner(SessionRepository repository){
//		return args -> {
//			Message userMessage = new Message("Hello, how are youiuuuuuu?", true, new Date());
//			Message assistantMessage = new Message("Hi thereeeeeee! I'm doing well, thank you.", false, new Date());
//			// Create a sample session with messages
//			Session session = new Session("john_doe", Arrays.asList(userMessage, assistantMessage));
//
//			repository.insert(session);
//		};
//	}

}
