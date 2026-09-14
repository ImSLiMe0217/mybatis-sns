# 인스턴스 원격콘솔 접속
ssh -i "mybatis-sns-key.pem" ec2-user@54.180.33.209

# 패키지 업데이트, Docker Engine 설치, Compose 플러그인 배치 및 사용자 권한 부여 일괄 실행
sudo yum update -y && \
sudo yum install -y docker && \
sudo systemctl enable --now docker && \
sudo mkdir -p /usr/local/lib/docker/cli-plugins && \
sudo curl -SL https://github.com/docker/compose/releases/latest/download/docker-compose-linux-$(uname -m) -o /usr/local/lib/docker/cli-plugins/docker-compose && \
sudo chmod +x /usr/local/lib/docker/cli-plugins/docker-compose && \
sudo usermod -aG docker $USER && \
newgrp docker

# 설치 버전 확인
docker --version
docker compose version


# SSH 접속 터미널이 아닌 로컬의 터미널에서 실행해야 함
# 인스턴스의 퍼블릭 IP가 12.34.56.78일 경우의 예시
scp -i "mybatis-sns-key.pem" .env ec2-user@35.72.183.18:/home/ec2-user/.env
scp -i "mybatis-sns-key.pem" docker-compose.yaml ec2-user@35.72.183.18:/home/ec2-user/docker-compose.yaml

# SSH 접속된 터미널에서 전송된 숨김 파일 목록 확인
ls -al


# Docker Hub로부터 최신 이미지 다운로드
docker compose pull

# App 및 DB 다중 컨테이너 백그라운드 기동
docker compose up -d

# EC2에서 실행 중인 컨테이너 상태 목록 확인 (STATUS 열에 Up 및 healthy 확인)
docker compose ps

# 스프링부트 application 구동 로그 확인
docker compose logs mybatis-sns-app

# curl 기반 서비스 응답 테스트
curl http://localhost/api/v1/posts