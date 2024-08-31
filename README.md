# pubg-stat-bot

## main의 resources의 application.properties파일을 만들어 다음의 내용을 채워주세요.
### 채워야 할 부분은 {...}로 명시되어있습니다.

spring.application.name=pubg-stat
api.key={your_api_key}
api.url=https://api.pubg.com

#Discord
discord.bot.token={your_discord_bot_token}
discord.application.id={your_discord_bot_id}
discord.public.key={your_discord_bot_public_key}
discord.oauth.client.id={your_discord_bot_oauth_id}


spring.datasource.url=jdbc:h2:tcp://localhost/~/hellopubg
spring.datasource.username=sa
spring.datasource.password=

spring.profiles.active=local

#JPA log
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

#Mybatis
mybatis.type-aliases-package=hello.itemservice.domain
mybatis.configuration.map-underscore-to-camel-case=true
logging.level.hello.itemservice.repository.mybatis=trace

# 기능
* 팀 멤버 등록 가능 최대 9명(현재 비즈니스 API를 사용하지 않는다고 가정)
* 스텟 저장 스케줄러 작동
