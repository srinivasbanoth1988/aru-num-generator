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

####Bulk Generate Test Case 1:

###### sample input

[
	{
	"goal":1000,
	"step":12
},
{
	"goal":1000,
	"step":10
},
{
	"goal":10,
	"step":1
}
]

###### sample response

{
    "task": "4411973f-235c-4f47-929e-727546d203a6"
}

## Google Cloud k8s Deployment Steps

Deployed on google cloud k8s.

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



