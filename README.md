## 2025 캡스톤 디자인 Capgoing Backend Repository
### 깃 컨벤션

<aside>
💡 Title :  아이콘 + [커밋태그] + 내용

---

ex) ✨[Feat] 깃허브 로그인/회원가입 API 구현

</aside>

→ Issue, Pr 템플릿 만들어놓음. 참고해주세요!

<aside>
💡 Git Rule

---

1. 구현하고자 하는 것을 `issue` 로 만든다.
2. `Github-Project` 에서 해당 `issue` 를 `In-progress` 로 변경한다.
3. 이슈 번호와 수행 목표에 맞게 branch 생성
4. `issue` 에 체크리스트를 채우며 개발을 진행한다. `commit은 자주!` 
5. 개발이 끝났으면 `Pull&Rquest` 에서 PR을 진행 → PR Template에서 꼭!! 이슈 닫기
6. PR이 끝났으면 작업한 **branch 삭제 (Local & Remote 모두!!)**
7. 새로운 Branch 분기 전  `main branch` `pull` 받기!


> Confilct를 줄이기 위해 가능한! 작업 영역이 안겹치게!
`겹치게 된다면 서로 알려주기..!!`
> 
</aside>

<aside>
💡 Git Command

---

1. branch 관련 명령어
    
    > **git branch** : branch 조회 및 현재 branch를 알 수 있다.
    **git branch <만들 branch> : branch 생성**
    **git checkout <이동할 branch>** : branch 이동 
    **git branch -d <삭제할 branch>** : Local에서 branch 삭제
    > 
2. Push 관련 명령어
    
    > **git add . 
    gitmoji -c
    git push**

</aside>

## **🎯 Commit Convention**

- feat : 새로운 기능 추가
- fix : 버그 수정
- docs : 문서 수정
- style : 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
- refactor: 코드 리펙토링
- test: 테스트 코드, 리펙토링 테스트 코드 추가
- chore : 빌드 업무 수정, 패키지 매니저 수정

## **💡 PR Convetion**

| 아이콘 | 코드 | 설명 |
| --- | --- | --- |
| 🎨 | :art | 코드의 구조/형태 개선 |
| ⚡️ | :zap | 성능 개선 |
| 🔥 | :fire | 코드/파일 삭제 |
| 🐛 | :bug | 버그 수정 |
| 🚑 | :ambulance | 긴급 수정 |
| ✨ | :sparkles | 새 기능 |
| 💄 | :lipstick | UI/스타일 파일 추가/수정 |
| ⏪ | :rewind | 변경 내용 되돌리기 |
| 🔀 | :twisted_rightwards_arrows | 브랜치 합병 |
| 💡 | :bulb | 주석 추가/수정 |
| 🗃 | :card_file_box | 데이버베이스 관련 수정 |

