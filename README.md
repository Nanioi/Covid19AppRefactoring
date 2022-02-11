# Covid-19 App Refactoring

#### Covid-19 App Project 기능추가 및 리팩토링

##### [ 22.02.11 ~ ]

< 목표 >
- 기존 코드 리팩토링 
- 누적확진 정보 제공 X -> 없애기 
- 누적, 신규확진현황 -> 국내현황 변경 
- 내 주변 선별진료소 찾기 기능 추가
- 주변 잔여백신 찾기 기능 추가 

< 진행상황 >
1. LinearLayout -> ConstraintLayout
2. BottomNavigation 추가
3. 각 메뉴 Activity 이동 -> Fragment 이동
4. 나열식 코드 -> 기능별 메서드 추출
5. Summary, Precaution 메뉴 완료

- 개요 ( Summary ) 메뉴 
<img src = "https://user-images.githubusercontent.com/52556870/153616641-b88791b1-53ec-46cd-bfc8-ca79f360cdb8.jpg" width="200">
- 예방수칙 ( Precaution ) 메뉴
<img src = "https://user-images.githubusercontent.com/52556870/153616667-b744ce7d-2313-499a-8af6-6bfaf8626c2c.jpg" width="200">

  
