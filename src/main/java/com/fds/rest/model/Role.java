package com.fds.rest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String userName;

    @ManyToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<User> users;
}
