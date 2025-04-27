package com.going.server.domain.quiz.generate;

import com.going.server.domain.graph.entity.Graph;
import com.going.server.domain.graph.entity.GraphNode;
import com.going.server.domain.quiz.dto.PictureQuizDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PictureQuizGenerator implements QuizGenerator<PictureQuizDto> {

    @Override
    public PictureQuizDto generate(Graph graph) {
        Random random = new Random();
        Set<String> shuffled = new HashSet<>(); //보기로 제공할 랜덤 문장들(중복 방지)
        int shuffledListSize = 3; // 총 보기 수 (정답 포함)

        List<GraphNode> graphNodes = graph.getNodes();
        int[] randomIndex = new int[shuffledListSize]; // 랜덤하게 선택할 노드

        // 랜덤 보기 개수만큼 node 뽑아내서 보기용 문장에 추가
        for (int i = 0; i < randomIndex.length; i++) {
            randomIndex[i] = random.nextInt(graphNodes.size());
            shuffled.add(graphNodes.get(randomIndex[i]).getIncludeSentence());
        }

        // 무작위로 정답 보기 설정
        int answerIndex = random.nextInt(shuffledListSize);

        // 정답보기를 답으로 설정
        GraphNode pictureNode = graphNodes.get(answerIndex);
        String answer = pictureNode.getIncludeSentence();

        // TODO : 정답 보기 이미지 생성

        // TODO : S3에 이미지 업로드

        // TODO : S3에 업로드한 이미지 url 받아오기
        String imageUrl = "https://s3-ap-northeast-2.amazonaws.com/~.png"; //예시 url

        return PictureQuizDto.builder()
                .imageUrl(imageUrl)
                .shuffled(shuffled)
                .answer(answer)
                .build();
    }
}
