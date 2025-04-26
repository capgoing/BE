package com.going.server.domain.graph.service;

import com.going.server.domain.graph.dto.*;
import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphEdge;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.graph.repository.GraphNodeRepository;
import com.going.server.domain.graph.repository.GraphRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GraphServiceImpl implements GraphService {

    private final GraphRepository graphRepository;
    private final GraphNodeRepository graphNodeRepository;

    public GraphServiceImpl(GraphRepository graphRepository, GraphNodeRepository graphNodeRepository) {
        this.graphRepository = graphRepository;
        this.graphNodeRepository = graphNodeRepository;
    }

    @Override
    public GraphListDto getGraphList() {
        List<Graph> graphs = graphRepository.findAll();
        List<GraphDto> graphDtos = new ArrayList<>();
        for (Graph graph : graphs) {
            GraphDto graphDto = GraphDto.of(graph,null,false,false);
            graphDtos.add(graphDto);
        }
        return GraphListDto.of(graphDtos);
    }

    @Override
    public void deleteGraph(Long graphId) {
        Graph graph = graphRepository.getByGraph(graphId);
        graphRepository.deleteById(graph.getId());
    }

    @Override
    public KnowledgeGraphDto getGraph(Long graphId) {
        Graph graph = graphRepository.getByGraph(graphId);

        List<NodeDto> nodeDtoList = new ArrayList<>();
        List<EdgeDto> edgeDtoList = new ArrayList<>();

        for (GraphNode node : graph.getNodes()) {
            NodeDto nodeDto = NodeDto.from(node, null);
            nodeDtoList.add(nodeDto);

            if (node.getEdges() != null) {
                for (GraphEdge edge : node.getEdges()) {
                    EdgeDto edgeDto = EdgeDto.from(edge.getSource(),edge.getTarget().getNodeId().toString(),edge.getLabel());
                    edgeDtoList.add(edgeDto);
                }
            }
        }

        return KnowledgeGraphDto.of(nodeDtoList, edgeDtoList);
    }

    @Override
    public NodeDto getNode(Long graphId, Long nodeId) {
        graphRepository.getByGraph(graphId);
        GraphNode node = graphNodeRepository.geyByNode(nodeId);
        return NodeDto.from(node,null);
    }

    @Override
    public KnowledgeGraphDto addNode(Long graphId, String group, String label) {
        //TODO : graphId로 그래프 찾기
        //TODO : group 추가하는 코드 작성
        //TODO : label 추가하는 코드 작성

        //TODO : nodeDto에 값 매핑하는 코드 작성
        List<NodeDto> nodeDto = new ArrayList<>();

        //TODO : edgeDto에 값 매핑하는 코드 작성
        List<EdgeDto> edgeDto = new ArrayList<>();

        return KnowledgeGraphDto.of(nodeDto,edgeDto);
    }
}
