package com.proarea.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "plants_requests")
public class PlantRequestEntity {


    @Id
    @Column(name = "plant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "lat")
    private String lat;

    @Column(name = "lng")
    private String lng;

    //created, applied, declined,
    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

}
