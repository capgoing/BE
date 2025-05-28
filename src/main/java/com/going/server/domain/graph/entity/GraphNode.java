package com.going.server.domain.graph.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Node("GraphNode")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor  // Neo4j가 생성자 주입 대신 setter 기반으로 생성할 수 있도록 해줌
public class GraphNode {
    @Id
    @GeneratedValue
    private Long id;

    @Property("node_id")
    private Long nodeId;

    private String label;
    private String group;
    private Long level;
    private String includeSentence; //해당 노드(단어)가 포함된 문장
    private String image;

//    @Transient  // Neo4j가 매핑하지 않음
    @ToString.Exclude
    @JsonIgnore
    @Relationship(type = "RELATED", direction = Relationship.Direction.OUTGOING)
    private Set<GraphEdge> edges;
}