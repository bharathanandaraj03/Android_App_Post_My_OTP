# Android_App_Post_My_OTP
Android app to read the SMS and post OTP back to webserver for UI authentication automation

Authentication is always quite challenging activity in test automation where modern application uses two factor (2FA) to authenticate the applications. Please follow the below steps to automate the OTP transactions.

Note:
•	It is always recommended to disable 2FA authentications in test environment as it helps to prevent the robotic activities but still in few scenarios applications requires such testing or automation. 

Solution : 
•	Automated test suite which triggers application under test (AUT) in chrome browser and starts with login transaction
•	Web application send OTP SMS message to Mobile number which is linked with the account
•	Once SMS received to mobile “PMOTP” app reads the message and sends it back to webserver API call
•	Automated test suite then reads the OTP from file and enters into application for automation
