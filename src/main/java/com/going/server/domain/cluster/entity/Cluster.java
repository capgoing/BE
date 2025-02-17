package com.going.server.domain.cluster.entity;

import com.going.server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="cluster")
public class Cluster extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cluster_id")
    private Long clusterId;

    @Column(name="represent_word")
    private String representWord;

    @Column(name="result_img")
    private String resultImg;

    public static Cluster toEntity(String representWord, String resultImg) {
        return Cluster.builder().representWord(representWord).resultImg(resultImg).build();
    }
}
