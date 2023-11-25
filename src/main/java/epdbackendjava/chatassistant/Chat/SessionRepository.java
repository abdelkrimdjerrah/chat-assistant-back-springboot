package epdbackendjava.chatassistant.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SessionRepository extends MongoRepository<Session, String> {
    @Query("{'userId': ?0}")
    List<Session> findAllSessionsByUserId(String user);

    @Query("{'sessionId': ?0}")
    Session findBySessionId(String sessionId);




}
