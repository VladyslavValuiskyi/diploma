package com.proarea.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authorities")
public class AuthoritiesEntity {

    @Id
    @Column(name = "unique_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uniqueId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "authority")
    private String authority;

    public AuthoritiesEntity(Long userId, String authority) {
        this.userId = userId;
        this.authority = authority;
    }
}
