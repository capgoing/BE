package com.going.server.domain.graph.service;

import com.going.server.domain.graph.dto.graphDto;
import com.going.server.domain.graph.dto.graphListDto;
import com.going.server.domain.graph.dto.knowledgeGraphDto;
import com.going.server.domain.graph.dto.nodeDetailDto;

public interface graphService {
    graphListDto getGraphList();
    void deleteGraph(Long graphId);

    knowledgeGraphDto getGraph(Long graphId);

    nodeDetailDto getNode(Long graphId, Long nodeId);
}
