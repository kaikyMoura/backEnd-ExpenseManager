package com.lab.expenseManager.user.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

import com.lab.expenseManager.user.enums.Status;

import io.micrometer.common.lang.Nullable;

@ToString
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbusers")
public class User implements Serializable{

	private static final long serialVersionUID = 3432009339652921717L;

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(unique = true)
    private String email;

    private String name;

    private String lastName;
    
    @Nullable
    private String userImage;

    @NotNull
    private String password;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name="tbusers_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Role role;
    
    @Enumerated(EnumType.STRING)
    private Status status;
}
