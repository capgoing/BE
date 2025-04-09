package com.going.server.domain.graph.service;

import com.going.server.domain.graph.dto.graphDto;
import com.going.server.domain.graph.dto.graphListDto;

public interface graphService {
    graphListDto getGraphList();
    void deleteGraph(Long graphId);
}
