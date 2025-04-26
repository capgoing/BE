package com.going.server.domain.graph.service;

import com.going.server.domain.graph.dto.*;
import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.repository.GraphRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GraphServiceImpl implements GraphService {

    private final GraphRepository graphRepository;

    public GraphServiceImpl(GraphRepository graphRepository) {
        this.graphRepository = graphRepository;
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
        //TODO : graphId로 그래프 찾기
        //TODO : 그래프 삭제하는 코드 작성
    }

    @Override
    public KnowledgeGraphDto getGraph(Long graphId) {
        //TODO : graphId로 그래프 찾기

        //TODO : nodeDto에 값 매핑하는 코드 작성
        List<NodeDto> nodeDto = new ArrayList<>();

        //TODO : edgeDto에 값 매핑하는 코드 작성
        List<EdgeDto> edgeDto = new ArrayList<>();

        return KnowledgeGraphDto.of(nodeDto,edgeDto);
    }

    @Override
    public NodeDetailDto getNode(Long graphId, Long nodeId) {
        //TODO : graphId로 그래프 찾기
        //TODO : nodeId로 노드 찾기
        return NodeDetailDto.from(null,null,null,null,null);
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
