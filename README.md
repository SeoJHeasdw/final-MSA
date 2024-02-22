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

## 보상처리
order -> install -> airobot 으로 전달되는 형태이기에
order가 발행되면 install을 거쳐 airobot으로 전달한다.

- order 소스 구조
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/4c52e26b-e929-4e0f-a185-f7d96c9aac4d)

- install 소스구조
- 
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/4aaf2ba6-9c89-454b-83b6-cabb50b5fde8)

- airobot 소스구조
- 
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/8a149f6a-ec70-4ad7-88a8-57a120b9b4e2)


## Test by UI
Open a browser to localhost:8088

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

