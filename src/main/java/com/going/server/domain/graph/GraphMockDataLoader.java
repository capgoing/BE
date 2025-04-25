/* 사이퍼 쿼리문 예시
import com.going.server.domain.graph.entity.GraphNode;

CREATE (g:KnowledgeGraph {
    title: "물의 상태 변화",
            listenUpPerfect: false,
            connectPerfect: false,
            picturePerfect: false
});

CREATE (n1:GraphNode { nodeId: 1, label: "기화", level: 1, description: "물이 기체로 바뀌는 것" }),
        (n2:GraphNode { nodeId: 2, label: "응결", level: 1, description: "기체가 다시 물이 되는 것" }),
        (n3:GraphNode { nodeId: 3, label: "응고", level: 1, description: "물이 얼어 고체가 되는 것" }),
        (n4:GraphNode { nodeId: 4, label: "물", level: 0, description: "상태 변화의 주체" });

MATCH (g:KnowledgeGraph { title: "물의 상태 변화" }),
        (n1:GraphNode { nodeId: 1 }),
        (n2:GraphNode { nodeId: 2 }),
        (n3:GraphNode { nodeId: 3 }),
        (n4:GraphNode { nodeId: 4 })
CREATE (g)-[:HAS_NODE]->(n1),
        (g)-[:HAS_NODE]->(n2),
        (g)-[:HAS_NODE]->(n3),
        (g)-[:HAS_NODE]->(n4);

MATCH (a:GraphNode { nodeId: 4 }), (b:GraphNode { nodeId: 1 })
CREATE (a)-[:RELATED { label: "기화" }]->(b);

MATCH (a:GraphNode { nodeId: 1 }), (b:GraphNode { nodeId: 2 })
CREATE (a)-[:RELATED { label: "응결" }]->(b);

MATCH (a:GraphNode { nodeId: 2 }), (b:GraphNode { nodeId: 3 })
CREATE (a)-[:RELATED { label: "응고" }]->(b);

MATCH (g:KnowledgeGraph { title: "물의 상태 변화" })-[:HAS_NODE]->(n:GraphNode)
OPTIONAL MATCH (n)-[r:RELATED]->(m:GraphNode)
        RETURN g, n, r, m;
*/



/*
package com.going.server.domain.graph;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphEdge;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.graph.repository.GraphRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class GraphMockDataLoader implements CommandLineRunner {

    private final GraphRepository graphRepository;

    @Override
    public void run(String... args) {
        Graph graph = new Graph();
        graph.setTitle("물의 상태 변화");
        graph.setListenUpPerfect(false);
        graph.setConnectPerfect(false);
        graph.setPicturePerfect(false);

        Map<String, GraphNode> nodeMap = new HashMap<>();
        String[][] nodes = {
                {"0", "물은", "2", "물은 변신해요!"},
                {"1", "그대로의", "2", "눈에 보이는 그대로의 모습만 있는 게 아니에요."},
                {"2", "모습이", "2", "물은 상황에 따라 모습이 바뀔 수 있어요."},
                {"3", "변화를", "2", "이런 변화를 상태 변화라고 해요."},
                {"4", "상태", "2", "이런 변화를 상태 변화라고 해요."},
                {"5", "변화에는", "2", "상태 변화에는 기화, 응결, 응고라는 세 가지가 있어요."},
                {"6", "기화", "2", "상태 변화에는 기화, 응결, 응고라는 세 가지가 있어요."},
                {"7", "응결", "2", "상태 변화에는 기화, 응결, 응고라는 세 가지가 있어요."},
                {"8", "가지가", "2", "상태 변화에는 기화, 응결, 응고라는 세 가지가 있어요."},
                {"9", "물을", "2", "먼저, 물을 끓이면 뜨거운 김이 올라오는 것을 본 적 있나요?"},
                {"10", "김이", "2", "김이 올라오는 것을 본 적 있나요?"},
                {"11", "것을", "1", "김이 올라오는 것을 본 적 있나요?"},
                {"12", "김은", "2", "그 김은 바로 수증기예요."},
                {"13", "물이", "0", "물이 뜨거워져서 기체가 되는 것을 기화라고 해요."},
                {"14", "기체가", "2", "물이 기체가 되는 것을 기화라고 해요."},
                {"15", "기화는", "2", "기화는 주로 뜨거운 열이 있을 때 일어나요."},
                {"16", "열이", "2", "뜨거운 열이 있을 때 일어나요."},
                {"17", "예를", "2", "예를 들어, 국을 끓이거나..."},
                {"18", "국을", "2", "국을 끓이거나..."},
                {"19", "햇볕이", "2", "햇볕이 강한 여름날..."}
        };

        for (String[] arr : nodes) {
            GraphNode node = new GraphNode();
            node.setId(Long.valueOf(arr[0]));
            node.setLabel(arr[1]);
            node.setLevel(Integer.parseInt(arr[2]));
            node.setDescription(arr[3]);
            node.setGraph(graph);
            nodeMap.put(arr[0], node);
        }

        graph.setNodes(new ArrayList<>(nodeMap.values()));

        List<GraphEdge> edges = new ArrayList<>();
        edges.add(createEdge(0L, "올라오는", nodeMap.get("11"), nodeMap.get("10")));
        edges.add(createEdge(1L, "되는", nodeMap.get("11"), nodeMap.get("14")));
        edges.add(createEdge(2L, "차가워질", nodeMap.get("22"), nodeMap.get("21")));
        edges.add(createEdge(3L, "서리는", nodeMap.get("25"), nodeMap.get("10")));
        edges.add(createEdge(4L, "되는", nodeMap.get("28"), nodeMap.get("13")));
        edges.add(createEdge(5L, "되는", nodeMap.get("28"), nodeMap.get("27")));
        edges.add(createEdge(6L, "바뀌는", nodeMap.get("11"), nodeMap.get("13")));
        edges.add(createEdge(7L, "연결", nodeMap.get("0"), nodeMap.get("11")));
        edges.add(createEdge(8L, "연결", nodeMap.get("1"), nodeMap.get("11")));
        edges.add(createEdge(9L, "연결", nodeMap.get("2"), nodeMap.get("11")));

        // Neo4j에서는 GraphEdge가 source → target 구조만 갖도록 하면 됨 (source 연결은 생략 가능)
        graphRepository.save(graph);
        System.out.println("지식그래프 목데이터 저장 완료");
    }

    private GraphEdge createEdge(Long edgeId, String label, GraphNode source, GraphNode target) {
        GraphEdge edge = new GraphEdge();
        edge.setEdgeId(edgeId);
        edge.setLabel(label);
        edge.setTarget(target);
        return edge;
    }
}*/

