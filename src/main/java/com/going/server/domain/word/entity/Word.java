package com.going.server.domain.word.entity;

import com.going.server.domain.cluster.entity.Cluster;
import com.going.server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="word")
public class Word extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="word_id")
    private Long wordId;

    @Column(name="compose_word")
    private String composeWord;

    @ManyToOne
    @JoinColumn(name="cluster_id")
    private Cluster cluster;

    public static Word toEntity(String composeWord, Cluster cluster) {
        return Word.builder().composeWord(composeWord).cluster(cluster).build();
    }
}
