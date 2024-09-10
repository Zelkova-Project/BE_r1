#### DTO와 domain이 따로 있는 이유
#### domain은 DB에 있는 데이터를 그대로 가져오는 객체
#### DTO는 domain의 순수성(영속성)을 유지시키기 위해서 사용하는 객체
#### ** 서비스 계층에서 DTO를 가지고 데이터 가공해야함

<hr/>

#### @GenerationType.IDENTITY
#### -> PK생성 방식을 데이터베이스로 이양

<hr/>

#### 테스트 환경에서도 롬복을 사용하기 위한 gradle 셋팅
#### -> 	testCompileOnly 'org.projectlombok:lombok'
#### -> 	testAnnotationProcessor 'org.projectlombok.lombok'

<hr/>

#### DB 한글에러 났을 때 
#### -> 	#ALTER TABLE BOARD CONVERT TO character SET utf8; 
#### -> 	#ALTER TABLE BOARD CONVERT TO character SET utf8;

<hr/>

#### @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
#### JSON 형식으로 넘기기 위해 String 타입으로 pattern에 맞춰서 포맷팅 해주는 어노테이션

<hr/>

