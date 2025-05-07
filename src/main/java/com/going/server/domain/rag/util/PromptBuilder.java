package com.going.server.domain.rag.util;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromptBuilder {

    public String buildPrompt(List<String> chunks, String question) {
        StringBuilder sb = new StringBuilder();

        sb.append("다음 정보를 참고하여 질문에 답해주세요.\n\n");
        sb.append("[관련 정보]\n");

        for (String chunk : chunks) {
            sb.append("- ").append(chunk.trim()).append("\n");
        }

        sb.append("\n[질문]\n").append(question.trim()).append("\n\n");
        sb.append("[답변]\n");

        return sb.toString();
    }
}