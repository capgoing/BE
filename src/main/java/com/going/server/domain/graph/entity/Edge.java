package com.going.server.domain.graph.entity;


import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Edge")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Edge {
    @Id @GeneratedValue
    private Long id;
    private String source;
    private String target;
    private String label;
}
