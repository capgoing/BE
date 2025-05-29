package com.going.server.domain.rag.service;

import com.going.server.domain.openai.service.OpenAIService;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CypherQueryGenerator {
    private final OpenAIService openAIService;

    public String generate(String userQuestion) {
        String prompt = """
        당신은 Neo4j 그래프 데이터베이스에서 정보를 추출하는 Cypher 쿼리를 생성하는 AI입니다.
        
        - 사용자 질문에 포함된 복합 명사(예: "지구형 행성")는 의미 단위로 분리해서,  
          해당 단어 각각이 포함된 노드도 함께 탐색할 수 있도록 쿼리를 작성하세요.
        - 예를 들어 "지구형 행성과 관련된 개념"이라면,
          "지구형", "행성", "지구형 행성" 모두를 쿼리 조건에 포함해야 합니다.
        - 관계(triple)는 (시작 노드)-[관계]->(도착 노드) 형식으로 추출하세요.
        - 설명 문장도 함께 조회하세요. (r.sentence → a.includeSentence → b.includeSentence)
        - 반환 항목:
          sourceLabel, relationLabel, targetLabel, sentence, nodeLabel
        - 코드는 Cypher 한 줄만 출력하며, 설명이나 코드블록 없이 작성하세요.
        - LIMIT은 5로 설정하세요.
        
        예시 질문: 지구형 행성과 관련된 개념들을 알려줘  
        →  
        MATCH (a:GraphNode)-[r:RELATED]-(b:GraphNode)  
        WHERE
          toLower(a.label) =~ '.*지구형.*' OR toLower(b.label) =~ '.*지구형.*' OR
          toLower(a.label) =~ '.*행성.*' OR toLower(b.label) =~ '.*행성.*' OR
          toLower(a.label) =~ '.*지구형 행성.*' OR toLower(b.label) =~ '.*지구형 행성.*'  
        RETURN  
          a.label AS sourceLabel,  
          r.label AS relationLabel,  
          b.label AS targetLabel,  
          COALESCE(r.sentence, a.includeSentence, b.includeSentence, "") AS sentence,  
          a.label AS nodeLabel  
        LIMIT 5
        
        질문: "%s"
        →
        """.formatted(userQuestion);


        return openAIService.getCompletionResponse(
                List.of(new ChatMessage("user", prompt)),
                "gpt-4o", 0.2, 500
        );
    }
}
