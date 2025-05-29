package com.going.server.domain.rag.util;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromptBuilder {

    public String buildPrompt(List<String> contextChunks, List<String> triples, String userQuestion) {
        StringBuilder sb = new StringBuilder();

        sb.append("다음 정보를 참고하여 질문에 답해주세요.\n\n");

        if (!triples.isEmpty()) {
            sb.append("[관계 정보]\n");
            triples.forEach(triple -> sb.append("- ").append(triple).append("\n"));
            sb.append("\n");
        }
        if (!contextChunks.isEmpty()) {
            sb.append("[설명 문장]\n");
            for (int i = 0; i < contextChunks.size(); i++) {
                sb.append(i + 1).append(". ").append(contextChunks.get(i)).append("\n");
            }
            sb.append("\n");
        }

        sb.append("질문: ").append(userQuestion);
        sb.append("[답변]\n");

        return sb.toString();
    }
}