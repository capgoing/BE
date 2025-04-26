package com.going.server.domain.graph.service;

import com.going.server.domain.graph.dto.GraphListDto;
import com.going.server.domain.graph.dto.KnowledgeGraphDto;
import com.going.server.domain.graph.dto.NodeDetailDto;

public interface GraphService {
    GraphListDto getGraphList();
    void deleteGraph(Long graphId);

    KnowledgeGraphDto getGraph(Long graphId);

    NodeDetailDto getNode(Long graphId, Long nodeId);

    KnowledgeGraphDto addNode(Long graphId, String group, String label);
}
