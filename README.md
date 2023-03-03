# Drones-Application

## # Instructions:
Clone the app from the repository.

[https://github.com/vacastro/drones.git](https://github.com/vacastro/drones.git)

This application has an in-memory database. To view the database, run the application, and then enter through the browser:

[http://localhost:8080/h2-console/](http://localhost:8080/h2-console/ "http://localhost:8080/h2-console/")

Driver class: org.h2.Driver

JDBC URL: jdbc:h2:mem:drones

Username: drones

Password: (not required)


### Clarifications: 

No dummy data was added to run the app.
In order to carry out the proposed exercise, it was necessary to create an additional class that manages the application, in this case we call it Shipping.
 A shipping has:
 
- 	id user,
- 	invoice number;
- An address which the shipment will be sent by drone;
- 	A date;
- 	The total weight (which may not exceed 500 grams),
- A dron (which will be the transport to the shipping).
- 	A medication list.
- 	An itinerary list: which will have

	o	A date

	o	A status: which could be- 

				ORDERED: when the shipment request is confirmed,
				 ON PROCESS: In this execution, an available drone is assigned to carry out the shipment. If there are no drones available, the request cannot be made. This case enables the drone to go from IDLE to LOADING state. In turn of , this option will enable medications to be added to the shipment. Medicines cannot be added if the drone is not in LOADING status, and the shipment is in ON PROCESS status. In this practical exercise, a table was added to the database that manages the medicines added to a shipment, the table is called medicine_dispensed.
				PACKED: Through this option, the shipment of medicines is finished. It will not be possible to proceed with its execution if the shipment is empty. The status of the drone will change from LOADING to LOADED.
				IN TRANSIT: shipment is authorized here. The drone changes from LOADED to DELIVERING state. When the drone's status is DELIVERING, its battery begins to discharge 1% for every minute elapsed..
				DELIVERED: the drone has reached its destination. Change the status of the drone from DELIVERING to DELIVERED. In this case we assume that the drone is landed, for this reason the discharge of its battery stops. Once the transport cargo is unloaded, the return of the drone can be confirmed. To carry out this exercise, a service was developed that allows the drone to return to its origin. This service allows the drone to go from DELIVERED to RETURNING status. Again the battery will begin to discharge by 1% for each minute elapsed. In turn of, another service was developed that confirms the receipt of the drone and allows its status to go from RETURNING to IDLE, and in this way it can start with a new request..
				CANCEL: This status allows the user to cancel his order if he does not wish to continue with its processing. Only orders that are in the ORDERED, ON PROCESS, PACKED status can be cancelled. It will not be possible to cancel an order when its status is ON TRANSIT, DELIVERING, or DELIVERED. When this action is executed, the drone will change its state to IDLE again.
			
Clarifications about itineraries: this application only allows actions to be carried out in order, so that if the shipping status is IN TRANSIT, for example, a previous action cannot be executed (PACKED, ON PROCESS, ORDERED). Nor does it allow you to perform the execution of the same action again. You should always proceed with the next action (with the exception of the CANCEL action that can be executed in the manner mentioned in the previous paragraph). The states of the itineraries will always go to the next state:
ORDERED -> ON PROCESS -> PACKED ->IN TRANSIT -> DELIVERED
If you try to perform an action in the wrong order, the application will show you the error message (Shipping Exception).

In addition to this exercise, the execution of a periodic task was requested to control the state of the drone batteries. In order to perform this task, a table was created in the database called Risk History, which will record the battery level of the drones every 3 minutes. If there are no drones in the database, the task will not give any results. This action checks the battery level and assigns it a risk value:
RISK STATUS:
- 	OK: when the battery level is greater than 50%.
- 	WARNING: when the battery level is between 25% and 50%.
- 	CRITICAL: when the battery level is less than 25%.

For this requirement, a REST API service was not requested. The records will only be accessible from the H2 database, executing the query:
Select * from risk_history;


## Rest Api Services: locally they run on port 8080

localhost:8080

### 1)	POST :   /DRON-APP/drone-register
This resource generates a request to create a new Drone type object. 
- 	It has serial number validations: it cannot be an empty value, it cannot exceed 100 characters, it cannot register 2 devices with the same serial number.
- 	It has model validations: it can only be the models mentioned in the requirement (LIGHTWEIGHT, MIDDLEWEIGHT, CRUISERWEIGHT, HEAVYWEIGHT), it cannot be an empty value.
- 	For this requirement it was mentioned that the fleet is made up of 10 devices, therefore no more than 10 may be registered.

Example:


```json
{
    "serialNumber":"oasdjbds",
    "dronModel":"LIGHTWEIGHT"
}

```

### 2)	GET:	/DRON-APP/all-drones
This resource generates a request to get all Drone objects. If there are no records, the error is reported.

### 3)	GET:	/DRON-APP/available-drones

Through this resource, a request is generated to obtain all Drone objects whose status is IDLE. If there are no matches, the error is reported.

### 4)	GET:	/DRON-APP/battery-status/{idDrone}
This resource generates a request to obtain the battery status of a Drone object. If there are no matches, the error is reported.

Path Variable: idDrone, type number.

### 5)	GET:	/DRON-APP/search-dron/{idDrone}
Through this resource, a request is generated to obtain the drone object whose id is the one reported in the path variable. If there are no matches, the error is reported..
Path Variable: idDrone, type number

### 6)	POST :   /DRON-APP/medication-register

This resource generates a request to create a Medication object.
- 	It has name validations: it cannot be an empty value, only letters, numbers, '-', '_' are allowed.
- 	It has code validations: the value cannot be empty, there cannot be more than one medication with the same code. It only accepts letters, '-', '_'. If lowercase characters are entered, they are converted to uppercase.
- 	It has weight validations: it cannot be a negative number, it cannot be a number greater than 500. If it is, it cannot be sent by this transport, since its value exceeds the allowed limi
- 	It has image validations: for this practical exercise, we did not work with a database with images, we only worked with strings, so that the image value is a string that must contain the values '.jpg' or ' in its extension. png'.

Example 

```json
{
    "name":"ibuprofeno",
    "weight":55,
    "code":"njsdhb",
    "image":"hbsc.jpg"
}
```
 
### 7)	GET:	/DRON-APP/all-medication

This resource generates a request to get all Medication objects. If there are no records, the error is reported.

### 8)	GET:	/DRON-APP/search-medication/{idMedication}

Through this resource, a request is generated to obtain the medication object whose id is the one reported in the path variable. If there are no matches, the error is reported.
Path Variable: idMedication, type number

### 9)	POST :   /DRON-APP/shipping-register

This resource generates a request to create a Shipping object. 
- 	It has invoice number validations, there cannot be more than one invoice with the same number.

Example

```json
{
    "idUser": 1,
    "invoice": 1885,
    "adress":"rivadavia 5350"
}
```

### 10)	GET:	/DRON-APP/DRON-APP/all-shippings

Through this resource, a request is made to obtain a list of all Shipping objects. If it does not exist, the error is reported.

### 11)	GET:	/DRON-APP/DRON-APP /find-shipping/{idShipping}

Through this resource, a request is made to obtain a Shipping object whose id is equal to that of the path variable. If there are no matches, the error is reported.

Path Variable: idShipping, type number

### 12)	GET:	/DRON-APP/DRON-APP /find-shipping-by-invoice/{invoice}

Through this resource, a request is made to obtain a Shipping object whose invoice is equal to that of the path variable. If there are no matches, the error is reported.

Path Variable: invoice, type number

### 13)	GET:	/DRON-APP/DRON-APP /all-itineraries

Through this resource, a request is made to obtain a list of Itinerary objects. If there are no matches, the error is reported.

### 14)	GET:	/DRON-APP/DRON-APP /shipping-by-user/{idUser}/{invoice}

Through this resource, a request is made to obtain a Shipping object whose idUser and invoice are the same as those of the path variable. If there are no matches, the error is reported.

Path Variable: idUser, type number / 
Path Variable: invoice, type number

### 15)	GET:	/DRON-APP/DRON-APP /shippings-by-user/{idUser}

Through this resource, a request is made to obtain a list of Shipping objects whose idUser is equal to that of the path variable. If there are no matches, the error is reported.

Path Variable: idUser, type number

### 16)	POST :   /DRON-APP/loading-shipping

Through this resource, a request is created to change the status of the shipment. If the shipping does not exist, the error is reported. A drone is assigned to shipping. If there are no available drones with a battery greater than 25%, the error is reported. The drone changes its status to LOADING. It is verified that the shipping is in the ORDERED state, if it is not, the error is reported. It is verified that the shipment is not cancelled, if it is, the error is reported.

Example

```json
{
        "idShipping": 1
}

```
### 17)	POST :   /DRON-APP/add-medication

Through this resource, a request to add medicines to a shipment is created. If the shipping does not exist, the error is reported. If the medication does not exist, the error is reported. If the shipping is not in the ON PROCESS state, the action cannot be executed, the error is reported.

Example

```json
{
    "shipping":{
        "idShipping":1
    },
    "medication":{
        "idMedication":1
    }
}
```

### 18)	POST :   /DRON-APP/loaded-shipping

Through this resource, a request is created to change the status of the shipment. If the shipping does not exist, the error is reported. It is verified that the shipment is in the ON PROCESS state, if it is not, the error is reported. It is verified that the shipment has loaded medicines since an empty drone cannot be sent. If not, the error is reported. The drone status is changed to LOADED. It is verified that the shipment is not cancelled, if it is, the error is reported.

Example

```json
{
        "idShipping": 1
}

```

### 19)	POST :   /DRON-APP/delivering-shipping

Through this resource, a request is created to change the status of the shipment. If the shipping does not exist, the error is reported. It is verified that the shipment is in the PACKED state, if it is not, the error is reported. The drone status is changed to DELIVERING. It is verified that the shipping status is not cancelled, if it is, the error is reported.

Example

```json
{
        "idShipping": 1
}

```

### 20)	POST :   /DRON-APP/delivered-shipping

Through this resource, a request is created to change the status of the shipment. If the shipping does not exist, the error is reported. It is verified that the shipment is in IN TRANSIT status, if it is not, the error is reported. The drone status is changed to DELIVERED.

Example

```json
{
        "idShipping": 1
}
```

### 21)	POST :   /DRON-APP/returning-dron

Through this resource, a request is created to change the status of the shipment. If the shipping does not exist, the error is reported. It is verified that the shipment is in the DELIVERED state, if it is not, the error is reported. The drone status is changed to RETURNING.

Example

```json
{
        "idShipping": 1
}

```

### 22)	POST :   /DRON-APP/cancel-shipping

Through this resource, a request is created to change the status of the shipment. If the shipping does not exist, the error is reported. It is verified that the shipment is in the ORDERED, ON PROCESS, PACKED state, if it is not, the error is reported. Drone status is changed to IDLE.

Example

```json
{
        "idShipping": 1
}
```


### 23)	POST :   /DRON-APP/confirm-returning

Through this resource, a request is created to change the state of the drone. If the drone does not exist, the error is reported. It is verified that the drone is in the RETURNING state, if it is not, the error is reported. Drone status is changed to IDLE.

Example

```json
{
    "idDron":1
}

```

### 24)	POST :   /DRON-APP/recharge-battery

Through this resource, a request is created to change the state of the drone's battery. If the drone does not exist, the error is reported. 

Example

```json
{
    "idDron":1
}

```

