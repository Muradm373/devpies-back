package com.devpies.devpiesback.auth.application.domain.model;

import com.sun.istack.NotNull;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @ManyToMany(mappedBy = "privileges", fetch = FetchType.EAGER)
    private List<Role> roles;

    public Privilege(String name){
        this.name = name;
    }

    public Privilege() {
    }
}
