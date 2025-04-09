package com.going.server.domain.graph.service;

import com.going.server.domain.graph.dto.graphDto;
import com.going.server.domain.graph.dto.graphListDto;
import org.springframework.stereotype.Service;

@Service
public class graphServiceImpl implements graphService {

    @Override
    public graphListDto getGraphList() {
        //TODO : DB에서 값 받아오는 코드 작성
        return graphListDto.of(null);
    }
}
