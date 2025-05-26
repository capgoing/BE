package com.going.server.domain.openai.service;

import com.going.server.domain.openai.dto.ChatCompletionRequestDto;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 요약본 생성
@Service
@RequiredArgsConstructor
public class TextSummaryCreateService {
    private final OpenAiService openAiService;

    private static final String SUMMARY_PROMPT = """
        You are an **educational summary assistant** for learners ranging from elementary to high school level.
        
        Please read the following text and generate a **structured summary** following these instructions:
        
        ---
        
        ### 📑 Summary Guidelines
        
        - Include **full content coverage** (summarize without omitting key ideas).
        - Structure your summary into the following sections:  
          📍 **Key Concepts**  
          📍 **Core Principles / How It Works**  
          📍 **Reasons / Evidence / Explanations**  
          📍 **Cause-Effect Relationships**  
          📍 **Examples / Case Studies**  
          📍 **Related Concepts / Comparisons**
        - Use Markdown formatting actively:
          - Use `###` for headings, `**bold**` for emphasis, `-`, `1.` etc. for sublists
          - Use `---` for section separation
          - Use `<details><summary>💡 Notes</summary>...</details>` to collapse additional info
        - Only use the following emojis:  
          📍🖇💡⚙🔧🔍📑✔💭✨
        - **Respond only in Korean**, and **output the summary only**, without any extra commentary.
        
        ---
        
        ### 📑 [요약할 원문 텍스트는 아래와 같습니다]
        """;



    public String summarize(String originalText) {
        List<ChatMessage> messages = List.of(
                new ChatMessage("system", SUMMARY_PROMPT),
                new ChatMessage("user", originalText)
        );

        ChatCompletionRequestDto request = ChatCompletionRequestDto.builder()
                .model("gpt-4o")
                .temperature(0.5)
                .maxTokens(500)
                .messages(messages)
                .build();

        return openAiService.createChatCompletion(request.toRequest())
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}
