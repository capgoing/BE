package com.going.server.domain.sentence.entity;

import com.going.server.domain.word.entity.Word;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="sentence")
public class Sentence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sentence_id")
    private Long sentenceId;

    @Column(name="sentence")
    private String sentence;

    @ManyToOne
    @JoinColumn(name="word_id")
    private Word word;

    public static Sentence toEntity(String sentence,Word word) {
        return Sentence.builder().word(word).sentence(sentence).build();
    }
}
