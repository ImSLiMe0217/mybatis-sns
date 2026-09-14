# 이미지 빌드 및 태그 지정 (latest 및 v1.0.0)
docker build -t imslime0217/mybatis-sns:latest .
docker tag imslime0217/mybatis-sns:latest imslime0217/mybatis-sns:v1.0.0

# 빌드된 이미지 확인
docker images | grep mybatis-sns

# 1) 특정 태그별 개별 푸시 (권장 방식)
docker push imslime0217/mybatis-sns:latest
docker push imslime0217/mybatis-sns:v1.0.0

# 2) 동일 이미지의 모든 태그를 한 번에 푸시할 경우
# docker push --all-tags imslime0217/mybatis-sns