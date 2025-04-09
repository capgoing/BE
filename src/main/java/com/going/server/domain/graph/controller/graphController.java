package com.going.server.domain.graph.controller;

import com.going.server.domain.graph.dto.graphDto;
import com.going.server.domain.graph.dto.graphListDto;
import com.going.server.domain.graph.service.graphService;
import com.going.server.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/graph")
@RequiredArgsConstructor
public class graphController {
    private final graphService graphService;
    @GetMapping()
    public SuccessResponse<graphListDto> getGraphList() {
        graphListDto result = graphService.getGraphList();
        return SuccessResponse.of(result);
    }

}
