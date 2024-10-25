package org.example.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class UserUpdateDTO {
    private UUID id ;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String bio;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private String imageUrl;



}
