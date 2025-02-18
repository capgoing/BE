package com.going.server.domain.history.entity;

import com.going.server.domain.word.entity.Word;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="history_id")
    private Long historyId;

    @Column(name="before")
    private String before;

    @Column(name="after")
    private String after;

    @ManyToOne
    @JoinColumn(name="word_id")
    private Word word;

    public static History toEntity(String before, String after, Word word){
        return History.builder().before(before).after(after).word(word).build();
    }
}
