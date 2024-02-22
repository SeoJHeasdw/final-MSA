# AI로봇 렌탈사업

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/75ed751e-69ff-4b7c-a987-04c96bfe41f5)

임대형 AI로봇만을 취급하여 사용자의 주문, 주문취소, 일시정지에 대한 비즈니스 모델 구현

### 이벤트 스토밍 Model
www.msaez.io/#/storming/final-MSA

### 서버 실행전 kafka 띄우기
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
apiVersion: apps/v1
kind: Deployment
metadata:
  name: order
  labels:
    app: order
spec:
  replicas: 1
  selector:
    matchLabels:
      app: order
  template:
    metadata:
      labels:
        app: order
    spec:
      containers:
        - name: order
          image: jinyoung/monolith-order:v20210602
          ports:
            - containerPort: 8080
          resources:
            requests:
              cpu: "200m"            
          readinessProbe:
            httpGet:
              path: '/actuator/health'
              port: 8080
            initialDelaySeconds: 10
            timeoutSeconds: 2
            periodSeconds: 5
            failureThreshold: 10
          livenessProbe:
            httpGet:
              path: '/actuator/health'
              port: 8080
            initialDelaySeconds: 120
            timeoutSeconds: 2
            periodSeconds: 5
            failureThreshold: 5
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

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/47fe51e9-ac47-449a-a0a9-78de2a7511fd)

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

```
kubectl apply -f - <<EOF
apiVersion: "apps/v1"
kind: "Deployment"
metadata: 
  name: order
  labels: 
    app: "order"
spec: 
  selector: 
    matchLabels: 
      app: "order"
  replicas: 1
  template: 
    metadata: 
      labels: 
        app: "order"
    spec: 
      containers: 
      - name: "order"
        image: "ghcr.io/acmexii/order-liveness:latest"
        ports: 
          - containerPort: 80
        volumeMounts:
          - mountPath: "/mnt/data"
            name: volume
EOF       claimName: nbs-pvc
kubectl apply -f - <<EOF
apiVersion: "apps/v1"
kind: "Deployment"
metadata: 
  name: order
  labels: 
    app: "order"
spec: 
  selector: 
    matchLabels: 
      app: "order"
  replicas: 1
  template: 
    metadata: 
      labels: 
        app: "order"
    spec: 
      containers: 
      - name: "order"
        image: "ghcr.io/acmexii/order-liveness:latest"
        ports: 
          - containerPort: 80
        volumeMounts:
          - mountPath: "/mnt/data"
            name: volume
      volumes:
      - name: volume
        persistentVolumeClaim:
          claimName: nbs-pvc  
EOF
```

### 확인
order에 접속하여 test.txt 파일을 만든다.

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/a63e0610-e8e1-4c48-91f1-0a38f3af44ad)

order를 scale 명령어로 2개로 로드밸런싱한 후 접속 해보면 해당 파일이 있는것을 확인할 수 있다.

![image](https://github.com/SeoJHeasdw/final-MSA/assets/43021038/1d176ddf-4468-469b-83cb-5ae9b7f61184)



