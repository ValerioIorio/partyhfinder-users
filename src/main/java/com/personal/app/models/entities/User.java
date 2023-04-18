package com.personal.app.models.entities;



import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "pf_users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;
    @Email
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "gender")
    private Integer gender;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "address")
    private String address;



    @Column(name = "status")
    private String status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    @Column(name = "last_logout")
    private LocalDateTime lastLogout;
    @Column(name = "last_failed_login")
    private LocalDateTime lastFailedLogin;




/*
    @Column(name = "matched_users_ids")
    @Lob
    private String matchedUsersIds; // comma separated ids of matched users
    @Transient*/

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_friend_user_relationships",
            joinColumns = @JoinColumn(name = "this_user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_user_id")
    )
    private List<User> matchedUsers;





    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_chats_relationships",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    private List<Chat> chats;
}
