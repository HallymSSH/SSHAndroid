# 2020년 1학기 한림대학교 소프트웨어융합대학<br>빅데이터 캡스톤디자인

## 프로젝트명
### [안심귀가길 도보안내 (회의 및 보고서 Repository)](https://github.com/HallymSSH/SSHReport)

![1](https://user-images.githubusercontent.com/50908525/85112890-549ea680-b251-11ea-8cb3-3dca2931fb4c.png)
![2](https://user-images.githubusercontent.com/50908525/85112896-55cfd380-b251-11ea-9373-ca2520a27442.png)
![3](https://user-images.githubusercontent.com/50908525/85112972-7c8e0a00-b251-11ea-9bb0-158f80925e33.png)

## 목표 및 기대효과
- 기존의 도보 안내는 최단 경로 또는 편한 길로 안내를 하고, 주변 CCTV와 경찰서 정보만 제공한다. 그러나, 안심 귀갓길 도보 안내 앱은 최단 경로가 아닌 CCTV와 가로 등이 밀집되어 있는 장소 위주로 경로를 제공해 안전한 귀가를 도와준다.
- 긴급상황 발생 시 특정 동작을 통해 근처 파출소로 연락을 할 수 있는 기능을 제공하고, 사용자뿐만이 아니라 경찰들에게 서비스를 제공함으로써 범죄에 빠르게 대응할 수 있도록 도와준다.

## SSH 팀 구성
- 지도교수 : **이원철 교수님**
- 팀장 : **[황찬우](https://github.com/HChanWoo)**
  - 팝업메뉴, 민원제기, 연락처 설정, DB 연동
- 팀원 : **[심창현](https://github.com/ChangHyun-S)**
  - Tmap API와 공공데이터 DB를 이용한 지도 및 경로 구현
- 팀원 : **[송시호](https://github.com/tlgh0623)**
  - 편의기능버튼, 사용자 설정

## 개발환경
- 개발 툴 : Android Studio
- 언어 : JAVA
- 데이터베이스 : sqlite
- API : Tmap API, Daum 우편번호 서비스, 공공데이터포털 전국 CCTV

## 회의록
- [Link](https://github.com/HallymSSH/SSHReport/tree/master/%ED%9A%8C%EC%9D%98%EB%A1%9D)

## 보고서 및 PPT
- [Link](https://github.com/HallymSSH/SSHReport/tree/master/Document)

## Process
### [PowerPoint Link](https://github.com/HallymSSH/SSHReport/blob/master/Document/SSH_%EC%95%88%EC%8B%AC%EA%B7%80%EA%B0%80%EA%B8%B8_%EC%98%81%EC%83%81%EC%A0%9C%EA%B1%B0.pptx)

![그림1](https://user-images.githubusercontent.com/50908525/85111574-7b5bdd80-b24f-11ea-8603-53860f85845d.png)

## 스크린샷
### [PowerPoint Link](https://github.com/HallymSSH/SSHReport/blob/master/Document/SSH_%EC%95%88%EC%8B%AC%EA%B7%80%EA%B0%80%EA%B8%B8_%EC%98%81%EC%83%81%EC%A0%9C%EA%B1%B0.pptx)

![스크린샷1](https://user-images.githubusercontent.com/50908525/85111575-7bf47400-b24f-11ea-9606-6ba957b7ef9c.png)
![스크린샷2](https://user-images.githubusercontent.com/50908525/85111558-77c85680-b24f-11ea-8191-995ebef59307.png)
![스크린샷3](https://user-images.githubusercontent.com/50908525/85111562-78f98380-b24f-11ea-9fa6-661eeb3f9ee9.png)
![스크린샷4](https://user-images.githubusercontent.com/50908525/85111566-7a2ab080-b24f-11ea-8b7f-e2e575845135.png)
![스크린샷5](https://user-images.githubusercontent.com/50908525/85111570-7ac34700-b24f-11ea-8575-acd6cacc1268.png)

## 기대효과
- 본 과제 결과물을 이용한 사업화 추진 전략은 기존 유사 앱들과의 차별성이다. 기존 유사 앱들은 한정된 지역만 정보를 제공하고, 대개 경로를 안내해주지 않지만, 안심귀가길 앱은 전국 어디서든 서비스가 가능하다.
- 범죄는 CCTV 사각지대에서 주로 발생하는데 안심귀가길 앱을 사용하면 안전한 경로로 안내해 주기 때문에 범죄를 예방할 수 있다. 만약 범죄가 발생하더라도 피해자의 위치를 파악하여 가족 또는 인근 경찰서에 정보를 제공함으로써 초기에 대처할 수 있다.
- CCTV와 같은 보안 시설물들이 미흡한 곳을 파악한 후 앱 사용자가 공공기관에 민원을 제기할 수 있도록 링크를 제공하여 공공기관은 보안 시설물이 부족한 곳을 파악하게 되어 보완함으로써 모든 사람이 안심하고 귀가할 수 있는 길을 만들 수 있을 것이다.

## References
- [Tmap API](http://tmapapi.sktelecom.com/index.html)
