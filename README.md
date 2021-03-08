# M1 Secure Development : Mobile applications Francois Grime IOS2

## Exercice

Using Android Studio, I had to create a mobile application.  
  
The goal is to create a secure application to see our bank accounts.   

### Requirements of our app
- This application must be available offline :
The application is available offline, i used framework Room to have a database that i can access offline. 

- A refresh button allows the user to update its accounts :
A refresh button is available and actualise the textview and the database at the same time.

- Access to the application is restricted :
To access to our application you have to put a password, by default it is "superpassword". This password is encode in a .cpp so it cannot be find with reverse engineering. 
The password is really weak, so to reduce the chance to force brute the app, i put 5 seconds between each logins and i strongly recommand to change the password.
The input for the password has been sanitized in order to stop SQL injection.

- Exchanges with API must be secure ( with TLS) :
For the network i choose to use retrofit2 and OkHttp for TLS use.

### README.md content

- Explain how you ensure user is the right one starting the app :
The password is hidden and could have been hashed in order to stay secret. The aim of the exercice wasn't to create a strong IAM so i decided to use a simple password.

- How do you securely save user's data on your phone ? :
Due to a database that i encrypt with AES256 masterkey that is hidden in a .cpp ile.

- How did you hide the API url ? :
I hide it in a .cpp file.

- Screenshots of your application 

![image](https://user-images.githubusercontent.com/65899606/110267381-f7ee4200-7fbf-11eb-9a23-907503240ed2.png)
![image](https://user-images.githubusercontent.com/65899606/110267445-18b69780-7fc0-11eb-8984-9764f94b9d60.png)

# Default password is "superpassword"
