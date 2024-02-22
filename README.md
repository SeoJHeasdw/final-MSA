# AI로봇 렌탈사업

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/75ed751e-69ff-4b7c-a987-04c96bfe41f5)

임대형 AI로봇만을 취급하여 사용자의 주문, 주문취소, 일시정지에 대한 비즈니스 모델 구현

## 이벤트 스토밍 Model
www.msaez.io/#/storming/final-MSA

## 서버 실행전 kafka 띄우기
```
cd infra
docker-compose up
```
- Check the Kafka messages:
```
cd infra
docker-compose exec -it kafka /bin/bash
cd /bin
./kafka-console-consumer --bootstrap-server localhost:9092 --topic kafka.scaling --from-beginning
```

## 구현된 backend MSA Service

- order : 주문담당하는 팀으로 고객의 주문, 주문취소, 일시정지를 관리하며 다른 팀에게 주문의 형태를 연동한다.
- install : 배송을 진행하는 현장작업자어플(외부시스템)과의 연동을 하며 설치&회수 완료 시 해당 상태를 payment팀과 airobot팀에게 연동한다.
- payment : 금액을 담당하는 팀으로 주문상태에 따라 금액을 부과하거나 취소한다.
- airobot : 로봇을 관리하는 팀으로 로봇의 사용상태, 재고 등을 관리한다.

## Run API Gateway (Spring Gateway)
```
cd gateway
mvn spring-boot:run
```

## Test Service
- airobot(재고생성)
```
 (gateway)  http :8088/airobots airobotId=3 airobotName=CleaningRobot useStatus=N stock=1000
 (airobot)  http :8085/airobots airobotId=3 airobotName=CleaningRobot useStatus=N stock=1000

```
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/71ebc6b6-6ed8-4217-89fc-8aa2ef3ffa3b)

- order(주문)
```
 (gateway)  http :8088/orders airobotId=3 airobotName=CleaningRobot qty=3
 (order)    http :8082/orders airobotId=3 airobotName=CleaningRobot qty=3

```
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/8867061f-3899-470d-968e-712360d9484c)

- gateway에서 확인
```
  (gateway) http :8088/airobots
  (airobot) http :8085/airobots
```
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/320ce006-f174-473a-ab57-0c2903dee691)

## 트랜잭션 처리과정
order -> install -> airobot 으로 전달되는 형태이기에
order가 발행되면 install을 거쳐 airobot으로 전달한다.

- order 소스 구조
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/4c52e26b-e929-4e0f-a185-f7d96c9aac4d)

- install 소스구조
 
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/4aaf2ba6-9c89-454b-83b6-cabb50b5fde8)

- airobot 소스구조
 
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/8a149f6a-ec70-4ad7-88a8-57a120b9b4e2)


# 서비스 운영
나는 AWS를 이용하였고(보안이슈로 사진첨부X)
1. AWS WEB Services에서 콘솔 로그인
2. IAM 서비스에서 사용자 액세스 키 만들기를 통해 Access key ID, Secret acess key 발급)

이후 console창에서
명령어 : aws configure 
를 통해 로그인 한다.

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/6d1e5abd-1086-42db-a2c3-9a7e6a7d833f)

```
Access key ID 	  : 비공개
Secret access key : 비공개
region name 	  : 비공개
output format     : json
```
접속이 잘 되었는지 
명령어 : aws iam list-account-aliases
로 계정정보가 잘 나오는지 확인한다.

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/d81966d9-252b-4ab6-b3f8-40bbf28fcb36)

그 후 클러스터 생성 명령어인 eksctl create cluster를 통해 클러스터를 만든다
(--name 즉 ClusterId user10-eks 로 내가 지정하여 생성하였다.)

eksctl create cluster --name user10-eks --version 1.27 --with-oidc --managed --node-type t3.medium --nodes 3 --nodes-min 1 eksctl create cluster
--node-volume-type gp3 --nodes-max 3 --asg-access --full-ecr-access

클러스터 생성이 완료되면 클러스터 사용을 위한 설정을 다운로드한다.
aws eks update-kubeconfig --name user10-eks

명령어 : kubectl get nodes
를 입력하여 3개의 노드가 보이면 클러스터 설정이 잘 적용되었다.
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/ba72d9bf-ee87-4cdd-a72e-120e399ff3bd)


docker hug를 이용할 예정이기에 
docker에 접속한다
명령어 : docker login

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/d4833dd9-1f7d-4ee5-8003-c7723a206e8e)

## docker 이미지 생성
각각의 service폴더에 들어가 
Maven 프로젝트를 빌드하고 
docker 이미지를 생성&푸쉬한다.

```
mvn package -B
docker build -t seojaeho/order/latest .
docker push seojaeho/order:latest

mvn package -B
docker build -t seojaeho/install/latest .
docker push seojaeho/install:latest

mvn package -B
docker build -t seojaeho/airobot/latest .
docker push seojaeho/airobot:latest

mvn package -B
docker build -t seojaeho/payment/latest .
docker push seojaeho/payment:latest
```

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/4d35ae75-f256-4a50-a230-4c3e066139f5)



## 내가 만든 서비스 띄우기
kubectl create deploy order --image=seojaeho/order:v1

띄운 서비스의 상태는 
명령어 : kubectl get po
통해 확인할 수 있다.

NAME                     READY   STATUS    RESTARTS   AGE
order-7d66c76dcd-j4nbq   1/1     Running   0          18s
내가 만든 내 이미지 배포해 보기
kubectl create deploy myhome --image=DockerHub-Id/welcome:v1
kubectl expose deploy myhome --type=LoadBalancer --port=80
웹브라우저에서 서비스 접속하기
kubectl get service
# 조회되는 EXTERNAL-IP 복사 후, 브라우저 주소창에 붙여넣기



## Required Utilities

- httpie (alternative for curl / POSTMAN) and network utils
```
sudo apt-get update
sudo apt-get install net-tools
sudo apt install iputils-ping
pip install httpie
```

- kubernetes utilities (kubectl)
```
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

- aws cli (aws)
```
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```

- eksctl 
```
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
```

