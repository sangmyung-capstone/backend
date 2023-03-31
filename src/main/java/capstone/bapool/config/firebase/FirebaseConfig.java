package capstone.bapool.config.firebase;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Configuration
public class FirebaseConfig {
    @Value("${firebase-realtime-database.database-url}")
    private String url;

    @PostConstruct
    public void init() throws Exception{
        FileInputStream serviceAccount =
                new FileInputStream("src/main/resources/google-services.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(url)
                .build();

        FirebaseApp.initializeApp(options);
    }
}
