package com.proarea.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "plant_pictures")
public class PlantPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id")
    private Long id;

    @Column(name = "plant_id")
    private Long plantId;

    @Column(name = "picture_id")
    private Long pictureId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picture_id", insertable = false, updatable = false)
    private Picture picture;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", insertable = false, updatable = false)
    private PlantEntity plant;
}
