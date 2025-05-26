<div align="center"> <img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&text=생각잇기%20BE&fontSize=30&fontAlign=50" alt="header"/> </div>

## 🧠 딥러닝 기반 지식 그래프 생성 시스템
> 최근 생성형 AI 기술은 텍스트 생성, 문서 요약, 번역 등 다양한 영역에서 활용되며 큰 주목을 받고 있습니다. 그러나 사실과 무관한 내용을 그럴듯하게 만들어내는 한계가 존재합니다.
> 이는 특히 **정보성 자료에서 정보의 신뢰성을 해치는 심각한 요인**이 될 수 있습니다.
> 이러한 문제를 해결하기 위해, 생각잇기 시스템은 생성형 AI의 기반 데이터를 **구조화된 지식그래프 형태로 가공**하여 AI의 **의미 기반 응답 정확도**를 높이고자 합니다.


## ✨ 주요기능
1. 텍스트가 담긴 PDF 파일을 업로드합니다.
2. PDF에서 텍스트를 추출한 뒤, 직접 학습시킨 지식그래프 생성 모델을 통해 주요 개념과 관계를 시각적인 지식그래프로 구성합니다.
3. 그래프의 노드를 더블 클릭하면 해당 문장이 포함된 문맥과 관련 이미지를 확인할 수 있으며, TTS 기술로 음성 출력도 가능합니다.
4. 생성된 그래프를 바탕으로 RAG 기반 챗봇을 통해 정확하고 맥락 있는 질의응답이 가능합니다.
5. 지식그래프 내용을 기반으로 3가지 형태의 퀴즈를 통해 자기 점검이 가능합니다.

## 🛠️ 주요 기술
- **개발언어**: Java, JavaScript, Cypher Query, Python
- **개발환경**: Spring Boot, React, Neo4j Aura, Docker, Docker-Compose
- **개발도구**: IntelliJ IDEA, Visual Studio Code 
- **주요기술**: 
  - koBERT : 관계 분류를 위한 사전 학습 모델 활용 및 파인튜닝
  - Stanza : 한국어 형태로 분석 및 의존 구문 분석
  - KMeans, Sentence Transformers : 문장 임베딩 생성, 의미 기반 클러스터링에 활용
  - NetworkX : 지식 그래프 구조 생성, BFS 기반 계층화
  - Git Actions + DockerHub : 코드 push 시 테스트, Docker 이미지 빌드 및 배포 자동화
  - Clova OCR : PDF 텍스트 추출
  - ReactFlow+D3-force : 지식 그래프 시각화

## ⚙️ 시스템 아키텍쳐
![image 41](https://github.com/user-attachments/assets/ccfa5e4f-8a61-43be-bdda-40008af743bc)


## 👩🏻‍💻 Developers

| BE | BE | BE |
| --- | --- | --- |
| <img style="width: 200px;" src="https://avatars.githubusercontent.com/u/113489721?v=4" /> | <img style="width: 200px;" src="https://avatars.githubusercontent.com/u/147326233?v=4"/> | <img style="width: 200px;" src="https://avatars.githubusercontent.com/u/104489022?v=4"/> |
| 한성대학교 | 한성대학교 | 한성대학교 | 한성대학교 |
| 강다현 | 김혜진 | 이주연 |
| [@hyeonda02](https://github.com/hyeonda02) | [@khyaejin](https://github.com/khyaejin) | [@Juye0nLee](https://github.com/Juye0nLee) | 
