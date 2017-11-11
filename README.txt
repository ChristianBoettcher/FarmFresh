TCSS 450: Mobile Application Programming, Autumn 2017

Programming Project: Deliverable 1

Group 3: Farm Fresh
Name: Alisher Baimenov
      Christian Boettcher
      Doseon Kim

Overview:
	
	For the first stage of our Mobile Apps project,
our group has implemented all of the necessary reqs. for 
user registration and login. We've also make a back-end
web service to store these users in a database and made 
it so our app uses USDA's Api to find near by farmers 
markets.

	This app is tested with a Nexus 5: android 
	ver. 4.0.3(Google API) emulator 

	*Account for testing purposes*
	E-mail: farmfresh5000@gmail.com
	Password: password123 

Known issues:

	- E-mail login/registration is case sensitive
	- Samsung SCH-S738C (Android 4.0.4, API 15)
	  cut off some text when desplaying list of 
	  markets
	- 

Implemented Features:

	1. Registers users in a database, creating an
	   account
	2. Allows users to login to access the primary
	   functions of the app
	3. Allows users to change their password if 
	   they forgot it
	4. Provides a searching option to look up 
	   markets by five digit zip-code 
	5. Auto generates a list of near by markets
	   using USDA's API and zip-code

Use Case:
	
	First Activity
	-Login
		Enter a registered e-mail password on 
		the first screen and click "Login" to
		perceed to precede to the next activity.
	
	-Login: Alternative Cases
		-The user leaves e-mail line blank or
		 enters an invalid e-mail
		-> a setError pops up telling the user
		   has entered in a valid e-mail
		
		-The user leaves password blank or
		 enters an invalid password
		-> a setError pops up telling the user
		   to a password with eight or more 
		   characters
		
		-The user enters an incorrect e-mail or
		 password. 
		-> A toaster widget pops up telling the
		   user they have "entered in an e-mail 
		   not in our database" or "the wrong 
		   password has been entered".

	-Registeration
		click "Register" on the first screen to
		preceed to registration fragment. enter
		User Name, e-mail, password and confirm
		password to register. First activity 
		makes a call to our web service, stores 
		user information, and e-mails a 
		randomly generated pin used to confirm 
		registration (may take up to 5 min). 
		upon adding a successful user, the app 
		transitions to a "confirm pin" screen 
		where the user enters in the e-mailed 
		pin. once entered, the user returns to 
		the first screen.

	-Registeration: Alternative Cases
		-the user enters in an e-mail that 
		already exists in our database
		-> A setError tells the user that the 
		   email already exists

		-the user enters in two passwords that
		 don't match
		-> A setError tells the user that that
		   the passwords have to match 

		-the user enters in a password with 
		 less than eight characters
		-> A setError tells the user that that
		   the password has to be at least 
		   eight characters.

		-the user enters in a password with 
		 an invalid character
		-> A setError tells the user that that
		   the password has to be at least 
		   eight characters.

		-the user enters in the wrong pin
		-> a setError tells the user they have
		   entered the wrong pin

	-Forgot password
		click "Forgot password" on the first
		screen with a valid user e-mail. A call
		is made to our web service which 
		e-mails a pin to the user's e-mail 
		account while the app transitions to a
		"confirm pin" screen where the user 
		enters the emailed pin (e-mail may take
		up to 5 min). once entered, the user 
		transitions to a "new password" screen 
		where they enter a new password, a
		confirmation password, and confirms.
		When confirmed, a call to our web
		service updates the user password and 
		the user is returned to the first 
		screen.

	-Forgot password: Alternative Cases
		-The user leaves e-mail line blank or
		 enters an invalid e-mail
		-> a setError pops up telling the user
		   has entered in a non-existant e-mail

		-The user enters in the wrong pin
		-> a setError tells the user they have
		   entered the wrong pin

		-the user enters in two passwords that
		 don't match
		-> A setError tells the user that that
		   the passwords have to match 

		-the user enters in a password with 
		 less than eight characters
		-> A setError tells the user that that
		   the password has to be at least 
		   eight characters.

		-the user enters in a password with 
		 an invalid character
		-> A setError tells the user that that
		   the password has to be at least 
		   eight characters.  

	Second Activity
	-Search zip-code
		click on the edit text field and enter
		a zip code. Once entered, a list of   
		up to twenty of the nearest farmers 
		markets is generated and displayed by
		calling the USDA API.

	-Search zip: Alternative Cases
		-user enters a blank or an invalid zip-
		 code and hits search.
		-> nothing really happens as nothing is
		   populated in our list

		-user attempts to enter a zip-code with 
		 more than five digits.
		-> Only the five digits are kept by the
		   search field.