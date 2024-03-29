# AI로봇 렌탈사업

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/75ed751e-69ff-4b7c-a987-04c96bfe41f5)

임대형 AI로봇만을 취급하여 사용자의 주문, 주문취소, 일시정지에 대한 비즈니스 모델 구현

### 이벤트 스토밍 Model
www.msaez.io/#/storming/final-MSA
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/3005f031-443a-4459-835f-0a00c1c519c7)

## 서비스 시나리오
1. 고객이 주문을 한다
2. 주문팀은 설치팀에게 사용자의정보와 주문내역을 제공한다.
3. 설치팀은 해당 주문을 보고 외부시스템을 통해 현장작업자가 설치를 시작한다.
4. 또한 로봇팀에게 주문정보를 보내며 단말관리를 하라고 지시하고, 설치가 시작됨을 고객에게 알린다.
5. 외부시스템을 통해 설치가 완료되면 설치팀이 연락을 받고 설치가 완료되었고 비용을 부과하라고 비용팀에게 전달한다.
6. 설치가 완료되면 월정기요금이 발생하며 로봇팀에게 설치가 완료되어 로봇이 사용상태가 되었다고 알리고, 사용자에게 월 정기요금이 발생하기 시작했다고 알림을 보낸다.

## FLOW
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/ce713126-ef58-47f7-9feb-94ab05e8aa9d)



### 서버 실행전 kafka 띄우기
```
cd infra
docker-compose up
```
- kafka 이벤트 발생 확인 방법
```
cd infra
docker-compose exec -it kafka /bin/bash
cd /bin
./kafka-console-consumer --bootstrap-server localhost:9092 --topic kafka.scaling --from-beginning
```

### 구현된 backend MSA Service

- order : 주문담당하는 팀으로 고객의 주문, 주문취소, 일시정지를 관리하며 다른 팀에게 주문의 형태를 연동한다.
- install : 배송을 진행하는 현장작업자어플(외부시스템)과의 연동을 하며 설치&회수 완료 시 해당 상태를 payment팀과 airobot팀에게 연동한다.
- payment : 금액을 담당하는 팀으로 주문상태에 따라 금액을 부과하거나 취소한다.
- airobot : 로봇을 관리하는 팀으로 로봇의 사용상태, 재고 등을 관리한다.

### Run API Gateway (Spring Gateway)
```
cd gateway
mvn spring-boot:run
```

### Test Service
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

### 트랜잭션 처리과정
order -> install -> airobot 으로 전달되는 형태이기에
order가 발행되면 install을 거쳐 airobot으로 전달한다.

- order 소스 구조
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/4c52e26b-e929-4e0f-a185-f7d96c9aac4d)

- install 소스구조
 
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/4aaf2ba6-9c89-454b-83b6-cabb50b5fde8)

- airobot 소스구조
 
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/8a149f6a-ec70-4ad7-88a8-57a120b9b4e2)


# 클라우드 네이티브 운영
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

그 후 클러스터 생성 명령어를 통해 클러스터를 만든다
(--name 즉 ClusterId user10-eks 로 내가 지정하여 생성하였다.)

eksctl create cluster --name user10-eks --version 1.27 --with-oidc --managed --node-type t3.medium --nodes 3 --nodes-min 1 eksctl create cluster
--node-volume-type gp3 --nodes-max 3 --asg-access --full-ecr-access


클러스터 생성이 완료되면 클러스터 사용을 위한 설정을 다운로드한다.
```
aws eks update-kubeconfig --name user10-eks
```

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



### docker image deploy
```
kubectl create deploy order --image=seojaeho/order:latest
(버전정보 잘 확인할 것)
```
띄운 서비스의 상태는 
명령어 : kubectl get po
통해 확인할 수 있다.

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/c7514219-20c6-477e-9091-fdafe42e40b6)




### 컨테이너 자동 확장

단순하게는 scale 명령어를 통해서 늘릴 수 있지만(3개로 확장시킴)
```
kubectl scale deploy order --replicas=3
```

자동 확장을 위한 명령으로는 scale이 아닌 autoscale로 
cpu점유율, 최소값, 최대값 을 지정할 수 있고

```
kubectl autoscale deployment order --cpu-percent=50 --min=1 --max=3
```

yaml 파일을 만들어 관리할수도 있다

```
apiVersion: autoscaling/v1
kind: HorizontalPodAutoscaler
metadata:
  name: order-autoscaler
  # namespace: airobotrental   # 만약 네임스페이스를 명시했다면 여기에 추가
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: order   # autoscaling을 적용할 배포의 이름
  minReplicas: 1   # 최소 파드 수
  maxReplicas: 10  # 최대 파드 수
  targetCPUUtilizationPercentage: 50  # 대상 CPU 사용률(0-100%)
```

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/64a05667-1431-4b45-b5f0-65c3fea14762)

### 기능테스트

다른 서비스들과 동일한 기능이기에 order만 테스트를 수행한다.
먼저 expose 명령어를 통해 pod를 외부로 노출시킨다
```
kubectl expose deploy order --port=80 --target-port=8080 --type=LoadBalancer
kubectl get svc
```

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/92cecf6c-c746-488d-96ea-609bfd8725d5)

siega 를 통해 부하를 주면 order가 추가로 발생하는 것을 확인할 수 있다.

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/5de97bbf-ed08-4391-b18c-a663c637ff85)

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/a000d55e-68bf-4be0-a898-bdab55c099d6)


## Container 스토리지 관리
EBS CSI 설정

사용자 환경 변수를 설정하고 진행한다.
export REGION=ca-central-1
export CLUSTER_NAME=user10-eks
export ROOT_ACCOUNT_UID=879772956301

Kubernetes 서비스 계정과 IAM 역할 연결
1.23 이상의 EKS Cluster가 설치되어 있어야 한다.
Cluster가 AWS EBS CSI Driver를 사용하도록 IAM계정을 생성하고 IAM Policy를 Role과 함께 EKS에 설정한다.
```
eksctl create iamserviceaccount \
  --override-existing-serviceaccounts \
  --region $REGION \
  --name ebs-csi-controller-sa \
  --namespace kube-system \
  --cluster $CLUSTER_NAME \
  --attach-policy-arn arn:aws:iam::aws:policy/service-role/AmazonEBSCSIDriverPolicy \
  --approve \
  --role-only \
  --role-name AmazonEKS_EBS_CSI_DriverRole_$CLUSTER_NAME
```

## EBS Storage 백업을 위한 Snapshot Components 생성
### Customresourcedefinition 생성
```
kubectl apply -f https://raw.githubusercontent.com/kubernetes-csi/external-snapshotter/master/client/config/crd/snapshot.storage.k8s.io_volumesnapshotclasses.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes-csi/external-snapshotter/master/client/config/crd/snapshot.storage.k8s.io_volumesnapshotcontents.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes-csi/external-snapshotter/master/client/config/crd/snapshot.storage.k8s.io_volumesnapshots.yaml
```
### Controller 생성
```
kubectl apply -f https://raw.githubusercontent.com/kubernetes-csi/external-snapshotter/master/deploy/kubernetes/snapshot-controller/rbac-snapshot-controller.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes-csi/external-snapshotter/master/deploy/kubernetes/snapshot-controller/setup-snapshot-controller.yaml
```
### CSI-Driver add-on 설치
```
eksctl create addon --region $REGION --name aws-ebs-csi-driver --cluster $CLUSTER_NAME --service-account-role-arn arn:aws:iam::$ROOT_ACCOUNT_UID:role/AmazonEKS_EBS_CSI_DriverRole_$CLUSTER_NAME --force
```
### EBS CSI Driver 기반 gp3 StorageClass 등록

```
kubectl apply -f - <<EOF
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: ebs-sc
  annotations:
    storageclass.kubernetes.io/is-default-class: "true"
provisioner: ebs.csi.aws.com
volumeBindingMode: WaitForFirstConsumer
EOF
```

기존 gp2기반 Storage Class를 default 해제

```
kubectl patch storageclass gp2 -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"false"}}}'
```

Storage Class 확인
명령어 : kubectl get storageclass

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/86fe00e3-931b-40a8-98a7-ecb25a5be841)



## EFS 설정
### 아마존웹서비스에서 EFS 검색 후 파일시스템 생성, ID를 저장해둔다.
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/0df5d044-5c45-464b-9907-f0c817c1d1dc)

정책 설정
```
# Download the IAM policy document (Cloud Administrator Only)
curl -S https://raw.githubusercontent.com/kubernetes-sigs/aws-efs-csi-driver/master/docs/iam-policy-example.json -o iam-policy.json

# Create an IAM policy (Cloud Administrator Only)
aws iam create-policy \
  --policy-name EFSCSIControllerIAMPolicy \
  --policy-document file://iam-policy.json 
```


쿠버네티스 서비스 계정과 IAM역할 연결

```
ksctl create iamserviceaccount \
  --override-existing-serviceaccounts \
  --region $REGION \
  --name efs-csi-controller-sa \
  --namespace kube-system \
  --cluster $CLUSTER_NAME \
  --attach-policy-arn arn:aws:iam::$AWS_ROOT_UID:policy/EFSCSIControllerIAMPolicy \
  --approve 
```


## EFS CSI 드라이버 설치
helm upgrade -i aws-efs-csi-driver aws-efs-csi-driver/aws-efs-csi-driver \
  --namespace kube-system \
  --set image.repository=602401143452.dkr.ecr.ca-central-1.amazonaws.com/eks/aws-efs-csi-driver \
  --set controller.serviceAccount.create=false \
  --set controller.serviceAccount.name=efs-csi-controller-sa


### PVC생성

```
kubectl apply -f - <<EOF
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: nbs-pvc
  labels:
    app: test-pvc
spec:
  accessModes:
  - ReadWriteMany
  resources:
    requests:
      storage: 1Mi
  storageClassName: ebs-sc
EOF
```
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/1e651399-320d-409b-a403-3f0cb068e436)

### 쉐어볼룸 만들기

```
kubectl apply -f -<<EOF
apiVersion: v1
kind: Pod
metadata:
  name: shared-volumes
spec:
  containers:
  - image: redis
    name: redis
    volumeMounts:
    - name: shared-storage
      mountPath: /data/shared
  - image: nginx
    name: nginx
    volumeMounts:
    - name: shared-storage
      mountPath: /data/shared
  volumes:
  - name: shared-storage
    emptyDir: {}
EOF
```

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/0233c1d5-1eff-4bd0-89fd-7d0b78f874bc)


### EBS 볼륨을 가지는 주문마이크로서비스 배포

order-deploy.yaml 파일에 volumes 부분을 추가한다.

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/2101bed2-20d4-4010-9b2f-e061754442ef)


### 확인
order에 접속하여 test.txt 파일을 만든다.

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/a63e0610-e8e1-4c48-91f1-0a38f3af44ad)

order를 scale 명령어로 2개로 로드밸런싱한 후 접속 해보면 해당 파일이 있는것을 확인할 수 있다.

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/1d176ddf-4468-469b-83cb-5ae9b7f61184)


## 셀프 힐링 & 무정지배포

셀프힐링은 
order-deploy.yaml 파일에 livenessProbe 부분을 추가한다.

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/0769e3e4-fe95-4da6-b5ec-72f50fb914b8)

강제로 down을 주고 셀프 힐링이 되는지 확인한다. 

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/f3edbed7-2a40-492f-908e-5ec0dd365fae)


무정지 배포는
order-deploy.yaml 파일에 readinessProbe 부분을 추가한다.

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/db198049-496c-4520-acb4-ce15f8c43b41)

시그마로 부하를 주면서 배포를 해보면 에러가뜨지않고 배포되는것을 확인할 수 있다.

## Service Mesh
서비스 메시(Service Mesh)는 분산 시스템에서 마이크로서비스 간 통신을 관리하고 보안, 모니터링, 트래픽 제어 등의 기능을 제공하는 인프라스트럭처 계층이며
주요 목표는 개발자가 애플리케이션 코드에 집중할 수 있도록 하면서도, 마이크로서비스 간의 통신을 안전하고 효율적으로 관리하는 것

나는 쿠버네티스의 기본 서비스 매시인 istio 를 설치하였다.
랩을 활용하여 1.18.1ver을 이용하였다.

```
export ISTIO_VERSION=1.18.1
curl -L https://istio.io/downloadIstio | ISTIO_VERSION=$ISTIO_VERSION TARGET_ARCH=x86_64 sh -
```


![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/dc3ba31d-7668-44d0-8baa-4ed693379970)

### istio가 설치된 모습
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/11592014-a591-4925-aeb6-d3a1af64366c)

### istio Demo 설치
```
istioctl install --set profile=demo --set hub=gcr.io/istio-release
```
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/62f834ab-bb39-472b-a751-fbd18dfdcd40)

### istio DashBoard 설치
```
mv samples/addons/loki.yaml samples/addons/loki.yaml.old
curl -o samples/addons/loki.yaml https://raw.githubusercontent.com/msa-school/Lab-required-Materials/main/Ops/loki.yaml
kubectl apply -f samples/addons
```
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/e8b936e9-9a0e-4633-a29d-02c2cfc45407)

### 정상 설치 후, kiali의 ServiceType을 ClusterIP에서 LoadBalancer로 변경한다.

```
kubectl patch svc kiali -n istio-system -p '{"spec": {"type": "LoadBalancer"}}'
kubectl get service -n istio-system
```

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/f9d57fd6-e7dc-44f9-8b87-4b5efe478641)

서비스 메시 모니터(kiali) 접속 
a69167c468fb24432a54b8b1a5abc6d7-600541860.ca-central-1.elb.amazonaws.com:20001

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/56b10210-deb7-42cd-bc39-8732cc589877)

### 정상 설치 후, tracing의 ServiceType을 ClusterIP에서 LoadBalancer로 변경한다.

```
kubectl patch svc tracing -n istio-system -p '{"spec": {"type": "LoadBalancer"}}'
kubectl get service -n istio-system

```

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/870c4d6e-5338-4a77-818f-05ed80e4b91a)

분산추적 시스템(tracing) 접속
a2ee57e3458cb473493682409f85cec9-1286056546.ca-central-1.elb.amazonaws.com:80

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/385dd950-7904-4dde-9b19-6a8b71d7d2c6)

나는 namespace를 설정하지 않아 default로 설정해서 확인해본 모습이다.
![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/fc32b9d2-d134-4ac9-86ac-17c62dda9370)

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/6774b47b-304d-44a1-9a4c-65d323283b42)

### 변수(자연재해 등)로 인한 통신이 실패했을 때를 대비한 Retry 추가
- Retry 및 서킷브레이크는 istio에서 통제하기때문에 각 서비스에서 추가할 수 없다.

yml 파일을 만들고 kubectl apply -f 한다
(각 서비스 별로 (해당건은 order만)
```
apiVersion: networking.istio.io/v1alpha3
kind: DestinationRule
metadata:
  name: order
spec:
  host: order
  trafficPolicy:
    retries:
      attempts: 3
      perTryTimeout: 2s
      retryOn: 5xx,gateway-error,connect-failure
```

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/efeffa63-47b9-4234-a5fe-f43562ed87b5)


### 서킷 브레이크 기능 추가 (장애가 전파되는걸 방지하는 기능)

yml 파일을 만들고 kubectl apply -f 한다

```
apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
metadata:
  name: order
spec:
  hosts:
  - order
  http:
  - route:
    - destination:
        host: order
    weight: 100
---
apiVersion: networking.istio.io/v1alpha3
kind: DestinationRule
metadata:
  name: order
spec:
  host: order
  trafficPolicy:
    loadBalancer:
      simple: ROUND_ROBIN
    outlierDetection:
      interval: 10s
      consecutive5xxErrors: 1
      baseEjectionTime: 3m
      maxEjectionPercent: 100
```

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/1c6a277c-7c8b-4dd2-a5cc-df06397fd7f4)


## 모니터링

kubectl get service -n istio-system 할때 보였던 prometheus를 이용
tracing과 kiali와 마찬가지로 prometheus의 ServiceType을 ClusterIP에서 LoadBalancer로 변경한다.

```
kubectl patch svc prometheus -n istio-system -p '{"spec": {"type": "LoadBalancer"}}'
kubectl get service -n istio-system

```


### 만약 External-IP 개수 제약에 걸린다면 잠시 tracing과 kiali를 수정하자
(안걸리면 안해도 됌)
```
kubectl patch svc kiali -n istio-system -p '{"spec": {"type": "ClusterIP"}}'
kubectl patch svc tracing -n istio-system -p '{"spec": {"type": "ClusterIP"}}
```

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/eb0dd6a7-ef23-4892-8534-8192784613a4)

오픈소스 모니터링 서비스(prometheus) 접속 
acdf80ebb3a0640718007f9e1bb97dab-205573653.ca-central-1.elb.amazonaws.com:9090

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/ca7d0b31-3a51-49b6-a47e-35461382f4f4)


해당 GUI에서 그라파나와 연동할 수 있다.
grafana의 ServiceType을 ClusterIP에서 LoadBalancer로 변경한다.

```
kubectl patch svc grafana -n istio-system -p '{"spec": {"type": "LoadBalancer"}}'
kubectl get service -n istio-system

```

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/dab0a713-2772-49ee-920f-b7c4978da9e9)

오픈소스 모니터링 서비스(grafana) 접속
ab578c91a71c4464b9f37908292dc3fc-822424932.ca-central-1.elb.amazonaws.com:3000

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/244c5606-9e62-49b7-94a3-0befba623e72)


!! 프로메테우스는 상태 데이터를 수집하고, 그라파나는 프로메테우스로 수집한 데이터를 관리자가 보기 좋게 시각화
