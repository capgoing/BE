package com.going.server.domain.graph.service;

import com.going.server.domain.graph.dto.graphDto;
import com.going.server.domain.graph.dto.graphListDto;
import com.going.server.domain.graph.dto.knowledgeGraphDto;

public interface graphService {
    graphListDto getGraphList();
    void deleteGraph(Long graphId);

    knowledgeGraphDto getGraph(Long graphId);
}
