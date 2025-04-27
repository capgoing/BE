//import jakarta.annotation.Resource;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import com.theokanning.openai.chat.ChatCompletionRequest;
//import com.theokanning.openai.service.OpenAiService;
//
//@Service
//@RequiredArgsConstructor
//public class ChatService {
//    @Resource(name = "getOpenAIService")
//    private final OpenAiService openAiService;
//
//    public String chat(List<Message> messages, String prompt) {
//        ChatCompletionRequest request = ChatCompletionRequest.builder()
//                .messages(messages) // 이전 대화 목록 리스트
//                .prompt(prompt)     // 현재 입력
//                .model("gpt-4o") // 사용 모델
//                .build();
//
//        return openAiService.createChatCompletion(request)
//                .getChoices()
//                .get(0)
//                .getMessage()
//                .getContent();
//    }
//}
