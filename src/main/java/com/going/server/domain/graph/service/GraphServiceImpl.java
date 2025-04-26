package com.going.server.domain.graph.service;

import com.going.server.domain.graph.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GraphServiceImpl implements GraphService {

    @Override
    public GraphListDto getGraphList() {
        //TODO : DB에서 값 받아오는 코드 작성

        return GraphListDto.of(null);
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
