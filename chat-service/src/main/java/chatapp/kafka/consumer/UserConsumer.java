package chatapp.kafka.consumer;


import chatapp.entity.User;
import chatapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {

    private final UserService userService;

//    @KafkaListener(topics = "user-topic")
//    public void consumeUser(UserDTO userDTO) {
//        log.info("Consumed user: {}", userDTO);
//        User user = User.builder()
//                .userId(userDTO.id())
//                .firstName(userDTO.firstName())
//                .lastName(userDTO.lastName())
//                .email(userDTO.email())
//                .build();
//        userService.saveUser(user);
//    }

    @KafkaListener(topics = "user-events")
    public void consumeUserOnlineOfflineEvent(UserEvent event){
        log.info("Consumed user event: {}", event);
        if(event.status().equals("created")) {
            User user = User.builder()
                    .userId(event.userId())
                    .firstName(event.firstName())
                    .lastName(event.lastName())
                    .email(event.email())
                    .build();
            userService.saveUser(user);
        } else if (event.status().equals("online")) {
            userService.setUserStatusOnline(event.userId());
        } else if (event.status().equals("offline")) {
            userService.setUserStatusOffline(event.userId());

        }


    }



}
