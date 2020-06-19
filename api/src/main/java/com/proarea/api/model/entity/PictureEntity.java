package com.proarea.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pictures")
public class PictureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "base_64", columnDefinition = "TEXT")
    private String base64;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    public byte[] getDecodedBase() {
        String imageDataBytes = base64.substring(base64.indexOf(",") + 1);
        return Base64.getDecoder().decode(imageDataBytes);
    }

}
