# androidProject_6

## ⚠ 평가표의 각 항목에 대한 테스트 방법이나 주의 사항 ⚠


<h4>• 데이터베이스의 구조</h4>

```
/컬렉션
    |_uid
        |_“필드”

<물품>
/items
    |_물품uid
        |_“name", "price", "owner", "status", "content"

<메세지>
/message
    |_송신자uid
        |_"message", "receiver", "sender"

```

<h4>• Authentication 계정 생성가능</h4>
 
 ```
 (id/password)
-android@hansung.ac.kr/hansungandroid
-a@a.com/aaaaaa
 ```
 

<h4>• galaxy, galaxy watch, galaxy tab, buds, budspro는 
android@hansung.ac.kr 계정의 물품이고,
iphone, airpods 는 a@a.com 계정의 물품입니다.</h4>
<br>
<h4>• 판매 목록📋의 toolbar의 첫번째 항목을 통해 다른 사용자로부터 받은 모든 메세지💬를 볼 수 있고, 두번째 항목을 통해 로그아웃이 가능합니다.</h4>
<br>
<h4>• 물품 목록의 imageview는 모두 기본 이미지🤖로 저장되어 있습니다.</h4>
<br>
<h4>• 글 추가는 오른쪽 하단의 floatingButton으로 가능합니다.</h4>
<br>
<h4>• 필터링은 switch🎛 버튼을 사용해 거래가능한 물품만을 볼 수 있습니다.</h4>
<br>
<h4>• 메세지💬 보내기는 다이얼로그 화면으로 구성했고, 판매 글 보기 화면에서 보낼 수 있습니다.</h4>
<br>
<h4>• 채팅 목록📋에는 송신사의 이메일 계정과 메세지💬 내용을 볼 수 있습니다.</h4>
