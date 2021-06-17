# FLO-App
FLO Music Streaming Application - Android
플로앱 안드로이드
# ✌ Topic 
: 프로그래머스 과제 - FLO Application 클론 코딩

# 😊 화면 구성 요소
- 스플래시 스크린
#### - 음악 재생 화면
 - 재생 중인 음악 정보(제목, 가수, 앨범 커버 이미지, 앨범명)
 - 현재 재생 중인 부분의 가사 하이라이팅
 - Seekbar
 - Play/Stop 버튼
#### - 전체 가사 보기 화면
 - 특정 가사로 이동할 수 있는 토글 버튼
 - 전체 가사 화면 닫기 버튼
 - Seekbar
 - Play/Stop 버튼
 
 # 🤩 화면 소개
 ![ezgif com-gif-maker](https://user-images.githubusercontent.com/59405161/122177537-b0315d80-cec0-11eb-8dea-3a316b61eaa9.gif)
 
 # 👊 기능 요구 사항
 #### - 스플래시 스크린
   제공되는 이미지를 2초간 노출 후 음악 재생 화면으로 전환시킵니다.
 #### - 음악 재생 화면
  - 주어진 노래의 재생 화면이 노출됩니다.
  - 앨범 커버 이미지, 앨범명, 아티스트명, 곡명이 함께 보여야 합니다.
  - 재생 버튼을 누르면 음악이 재생됩니다. (1개의 음악 파일을 제공할 예정)
  - 재생 시 현재 재생되고 있는 구간대의 가사가 실시간으로 표시됩니다.
  - 정지 버튼을 누르면 재생 중이던 음악이 멈춥니다.
  - seekbar를 조작하여 재생 시작 시점을 이동시킬 수 있습니다.
 #### - 전체 가사 보기 화면
  - 전체 가사가 띄워진 화면이 있으며, 특정 가사 부분으로 이동할 수 있는 토글 버튼이 존재합니다.
  - 토글 버튼 on: 특정 가사 터치 시 해당 구간부터 재생
  - 토글 버튼 off: 특정 가사 터치 시 전체 가사 화면 닫기
  - 전체 가사 화면 닫기 버튼이 있습니다.
  - 현재 재생 중인 부분의 가사가 하이라이팅 됩니다.
  
 # 😋 디자인 패턴 및 라이브러리 
 #### MVVM 아키텍처 적용
 #### Retrofit 2 : 서버 통신에 사용
 #### Koin : DI를 위해 사용
 #### LiveData : 데이터 Observe를 위해 사용
 #### ViewModel : 긴 수명주기에 데이터를 담기 위해 사용
 #### Coroutine : 비동기 작업을 위해 사용
 #### Glide : 이미지 업로드를 위해 사용
 #### CardView : 앨범 corner를 위해 사용
 
 ## MVVM 패턴을 공부하시는 분들께 좋은 예시가 될 것이라고 예상합니다. 
 ## 추후 블로그 작성 후 readme 업데이트 하도록 하겠습니다.
 
 ## 🐔  Languages & IDE & Environment
- App : Kotlin & Android Studio
-  Operating System : Window10 64bit
-  CPU : I5-8265U
-  RAM : 8GB


 
 
