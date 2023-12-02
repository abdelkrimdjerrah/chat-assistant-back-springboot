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


}
