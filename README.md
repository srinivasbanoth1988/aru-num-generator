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



