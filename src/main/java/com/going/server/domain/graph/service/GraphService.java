package com.going.server.domain.graph.service;

import com.going.server.domain.graph.dto.*;

public interface GraphService {
    GraphListDto getGraphList();
    void deleteGraph(Long graphId);

    KnowledgeGraphDto getGraph(Long graphId);

    NodeDto getNode(Long graphId, Long nodeId);

    void addNode(Long graphId, NodeAddDto dto);

    void deleteNode(Long graphId, Long nodeId);

    void modifyNode(Long graphId, Long nodeId, NodeModifyDto dto);
}
