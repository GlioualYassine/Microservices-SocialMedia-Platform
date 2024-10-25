package org.example.userservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.dto.UserResponse;
import org.example.userservice.dto.UserUpdateDTO;
import org.example.userservice.file.FileStorageService;
import org.example.userservice.file.FileUtils;
import org.example.userservice.kafka.consumer.UserFriendDTO;
import org.example.userservice.kafka.producer.UserDeleteDto;
import org.example.userservice.kafka.producer.UserUpdateDto;
import org.example.userservice.kafka.producer.UserProducer;
import org.example.userservice.models.User;
import org.example.userservice.openFeign.FeignClient;
import org.example.userservice.openFeign.UserDTO;
import org.example.userservice.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserProducer userProducer;
    private final FeignClient userClient;
    private final FileStorageService fileStorageService;

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }
    public List<UserResponse> getAllUsersExceptUserAndFriend(UUID id) {
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
        List<UserResponse> userResponses = new ArrayList<>();
         users.forEach(u -> {
             UserResponse userResponse = UserResponse.builder()
                     .id(u.getId())
                     .firstName(u.getFirstName())
                     .lastName(u.getLastName())
                     .username(u.getUsername())
                     .email(u.getEmail())
                     .bio(u.getBio())
                     .birthDate(u.getBirthDate())
                     .imageUrl(FileUtils.readFileFromLocation(u.getImageUrl()))
                     .createdAt(u.getCreatedAt())
                     .updatedAt(u.getUpdatedAt())
                     .friends(u.getFriends())
                     .friendsRequestSent(u.getFriendsRequestSent())
                     .friendsRequestReceived(u.getFriendsRequestReceived())
                     .build();
             userResponses.add(userResponse);
         });
         return userResponses;
    }

    public UserResponse getUserById(UUID id) {
        User user =  userRepository.findById(id).orElse(null);
        log.info("User : {}", user);
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .birthDate(user.getBirthDate())
                .imageUrl(FileUtils.readFileFromLocation(user.getImageUrl()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .friends(user.getFriends())
                .friendsRequestSent(user.getFriendsRequestSent())
                .friendsRequestReceived(user.getFriendsRequestReceived())
                .build();
        return userResponse;
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


    public void uploadProfileImage(UUID userId, MultipartFile file) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null)
            return ;
        var profilePicture = fileStorageService.saveFile(file,user.getId());
        user.setImageUrl(profilePicture);
        userRepository.save(user);
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
                friendModel.setImageUrl(FileUtils.readFileFromLocation(u.getImageUrl()));
                userFriendsResponseList.getFriends().add(friendModel);
            }
        }
        return userFriendsResponseList;
    }


    public User updateUser(UserUpdateDTO user, MultipartFile file) {
        log.info("user : {}", user);
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        if (existingUser == null) {
            log.info("User not found");
            return null; // L'utilisateur n'existe pas
        }

        log.info("existing user : {}", existingUser);

        // Mettre à jour les informations de l'utilisateur
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setBio(user.getBio());
        existingUser.setBirthDate(user.getBirthDate());

        // Vérifier si un fichier a été fourni
        if (file != null && !file.isEmpty()) {
            String profilePicture = fileStorageService.saveFile(file, user.getId());
            existingUser.setImageUrl(profilePicture); // Mettre à jour l'image de profil
        }

        // Enregistrer l'utilisateur mis à jour
        User updatedUser = userRepository.save(existingUser);

        log.info("updated user : {}", updatedUser);
        // Envoyer une mise à jour à l'event bus ou au producteur
        UserUpdateDto userUpdateDto = UserUpdateDto
                .builder()
                .id(updatedUser.getId())
                .email(updatedUser.getEmail())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .build();
        userProducer.sendUserUpdate(userUpdateDto);

        return updatedUser; // Retourner l'utilisateur mis à jour
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
