
# Number Generator Application

### Instructions to Build & Run:

Steps to run the application from IDE:
1. Clone the GIT Repository
2. Import project into any IDE like Intellij using pom.xml
3. Run App from Intellij Configuration straight away
4. (Or) Go to AruNumGeneratorApplication File -> Right Click -> Run

From command line:

1. Build the project using pom.xml - > mvn clean install
2. Run the application -> java -jar target/aru-num-generator-0.0.1-SNAPSHOT.jar

Note: Application runs on port 8085 by default.

###API Endpoints:

| Command | HTTP Method | Description
| --- | --- | --- 
| `/api/generate` | POST | Create/submit single task and returns task(taskId)
| `/api/tasks/{uuid}/status` | GET | Fetches the status of a taskid
| `/api/tasks/{uuid}?action=get_numlist` | GET | Fetches generated numbers list or string
| `/api/bulkGenerate` |  POST | Create/submit multiple tasks and returns task(taskId)
| `/api/tasks/{uuid}` |  POST | Create/submit multiple tasks and returns task(taskId)

### Sample Single task Request: 

```json
{
"goal":"10",
"step": "2"

}
```
### Sample multiple tasks Request: 

```json
[
	{
	"goal":100,
	"step":12
},
{
	"goal":200,
	"step":10
},
{
	"goal":10,
	"step":1
}
]
```

### Sample Response for task submission:
```json
{
    "task": "c15ffa02-4eb6-4fa9-9265-ff5cbc270075"
}
```

### Sample Response for fetching result:
```json
{
    "result": "10,8,6,4,2,0"
}
```
### Sample Response for fetching results:
```json
{"results":["100,88,76,64,52,40,28,16,4","200,190,180,170,160,150,140,130,120,110,100,90,80,70,60,50,40,30,20,10,0","10,9,8,7,6,5,4,3,2,1,0"]}
```
## Assumptions

1. Not handling any error scenarios
2. Storing data into Non bloking queue. Created a scheudled task
  to pick submitted task from queue and saving results to static map to hold results. 
    
3.  We can use persistent messaging system/db to process on 
multiple nodes/clusters.
        
4. Considering goal max value as 100000


5. Exception will be thrown for boundy conditions

6. Query parameter is ignored.

7. Accepting only 1000 in bulkgenerate request


## Google Cloud k8s Deployment Steps



1. Create K8s Cluster from google cloud UI.
	* Kubernetes Engine > Clusters > Create Cluster
	* Enter name ex: docker-k8s-num-generator-service
	* Click on "Create" buttondocker-k8s-num-generator.yaml	* This process takes few minutes.
2. Once Cluster is created follow below steps to upload
	* Select cluster and click on "connect" button
	* "Connect to the cluster" popup will apprer
	* Clikc on "Run in cloud shell" button
	* Cloud shell termincal will appear.
	* below similar command(sample) will appear by default
	
	* gcloud container clusters get-credentials cluster-1 --zone us-central1-c --project reflec
	ted-coder-300911
	* Hit Enter 
	* Process will fetch clustor endpoint and auth data
	* Once above step is completed, upload docker-k8s-num-generator.yaml file
	* Once file is uploaded, excute commad from terminal (kubectl apply -f docker-k8s-num-generator.yaml)
	* once above step is finished, expose the app and add loadbalancer(external ip).
	
3. Delete the cluster once testing is completed to avoid charges.
