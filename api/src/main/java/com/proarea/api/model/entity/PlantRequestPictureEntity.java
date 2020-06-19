package com.proarea.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "plant_request_pictures")
public class PlantRequestPictureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unique_id")
    private Long id;

    @Column(name = "plant_request_id")
    private Long plantRequestId;

    @Column(name = "picture_id")
    private Long pictureId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picture_id", insertable = false, updatable = false)
    private PictureEntity picture;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_request_id", insertable = false, updatable = false)
    private PlantRequestEntity plantRequest;

}
