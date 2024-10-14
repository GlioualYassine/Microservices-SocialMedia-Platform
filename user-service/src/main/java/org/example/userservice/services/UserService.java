package org.example.userservice.services;

import lombok.RequiredArgsConstructor;
import org.example.userservice.kafka.consumer.UserFriendDTO;
import org.example.userservice.kafka.producer.UserDeleteDto;
import org.example.userservice.kafka.producer.UserUpdateDto;
import org.example.userservice.kafka.producer.UserProducer;
import org.example.userservice.models.User;
import org.example.userservice.openFeign.FeignClient;
import org.example.userservice.openFeign.UserDTO;
import org.example.userservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserProducer userProducer;
    private final FeignClient userClient;

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }
    public List<User> getAllUsersExceptUserAndFriend(UUID id) {
        List<User> users = new ArrayList<>();
        UserDTO userDTO = userClient.getUserById(id);
        Set<UUID> userFriendRequestsSent = userDTO.friendRequestsSent();
        Set<UUID> userFriendRequestsReceived = userDTO.friendRequestsReceived();



        User user = userRepository.findById(id).orElse(null);
        if(user != null){
            List<UUID> friends = user.getFriends();
            userRepository.findAll().forEach(u -> {
                if(!u.getId().equals(id) && !friends.contains(u.getId())&& !userFriendRequestsSent.contains(u.getId()) && !userFriendRequestsReceived.contains(u.getId())){
                    users.add(u);
                }
            });
        }
        return users;
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public UserFriendsResponseList getConnectedUserFriendships(UUID userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        List<User> users = userRepository.findAll();
        List<UUID> friendsIds = user.getFriends();
        UserFriendsResponseList userFriendsResponseList = new UserFriendsResponseList();
        userFriendsResponseList.setId(user.getId());
        userFriendsResponseList.setUsername(user.getUsername());
        for (User u : users) {
            if (friendsIds.contains(u.getId())) {
                FriendModel friendModel = new FriendModel();
                friendModel.setId(u.getId());
                friendModel.setUsername(u.getUsername());
                friendModel.setEmail(u.getEmail());
                userFriendsResponseList.getFriends().add(friendModel);
            }
        }
        return userFriendsResponseList;
    }


    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        if (existingUser == null) {
            return null;
        }
        User us =  userRepository.save(user);
        UserUpdateDto userUpdateDto = UserUpdateDto
                .builder()
                .id(us.getId())
                .email(us.getEmail())
                .firstName(us.getFirstName())
                .lastName(us.getLastName())
                .build();
        userProducer.sendUserUpdate(userUpdateDto);
        return us;
    }

    public User deleteUser(UUID id) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            userRepository.deleteById(id);
            userProducer.sendUserDelete(
                    UserDeleteDto.builder().id(id).build()
            );
        }
        return existingUser;
    }
}
