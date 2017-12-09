TCSS 450: Mobile Application Programming, Autumn 2017

Programming Project: Deliverable 2

Group 3: Farm Fresh
Name: Alisher Baimenov
      Christian Boettcher
      Doseon Kim

Notes:
https://docs.google.com/document/d/1bEI2MWY_DPKpuJ4Bm-O2y6ho3lotgZ9uys179zqD-VY/edit

Overview:
	
	For the second stage of our Mobile Apps project,
our group has implemented 90% of our apps features in 
our use cases. This included the requirements for Phase1
user registration, login and back-end webservice. We 
also needed to implement the app's main functionality 
and use SQL-Lite to store data locally on a mobile 
device. we use this to store the user's login 
information with a "Remember Me" feature. We chose to 
use SQL-Lite in the case that our application needed to 
store a large amount of data. In addition to storing 
information locally, we also needed to make a back end
web service to store these users in a database and made 
it so our app uses USDA's Api to find near by farmers 
markets. With the USDA's Api, we were able to access 
information regarding farmers markets through JSon files 
and search for markets through their given zip codes. We
also implemented a filter feature to sort through 
markets which have a certain product available (for now 
only one product at a time). to make our project more 
our own, we finished our project by giving it a logo
and an icon. 

	This app is tested with a Nexus 5: android 
	ver. 4.0.3(Google API) emulator 

	*Account for testing purposes*
	E-mail: farmfresh5000@gmail.com
	Password: password123 

Known issues:
	
	-filter doesn't apply until user hits search
	 again
	-No other issues we can think of at this time.
	 (move ahead to usability testing?)

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

	6. Provides a filter feature to find market by 
	   product (located in drawer menu).

	7. Provides a log off feature to... um... 
	   log off (also located in drawer menu).

	8. provides user with information about a
	   particular farmers market when they select 
           one form the list of farmers markets.

	9. Provides a link to a Google map on information
	   display page.

	10. Automaically logs users into the application
	    if user selects "Remember Me" before logging
	    in.

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

		-the user backs out before entering a pin
		-> the pin screen is placed on top of the 		   
		   backstack, so users can get back to it

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

		-the user backs out before entering a pin
		-> the pin screen is placed on top of the
 		   backstack, so users can get back to it 
 
	-"Remember Me"
		the user selects the "Remember Me"
		checkbox in order to store log in 
		information on the mobile device. This
		automatically log our users in next time
		the application is started. 

	-"Remember Me": Alternative Cases
		the user leaves the "Remember Me"
		checkbox unselected. The mobile device
		does not store the login information on
		the mobile device.


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

	-Filter product
		open the drawer menu and select the
                option marked "Filter". Once selected, a
		edit text field will be made available to
		enter a desired product. hit the search
		button to apply the filter. 

	-Filter product: Alternative Cases
		-user leaves the TextEdit field blank		
		-> the list is populated with ALL
		    possible farmers markets as if no
 		    filter was applied

		-user enters an invalid item in the
		 TextEdit field.		
		-> the list is populated with NONE of
  		   the farmers markets as no markets
		   carry that item (as far as our app is 
		   concerned).

	-Log off
		open the drawer menu and select the
                option marked "Log off". Once selected, 
		the user will be taken back to the login
		screen. This removes the "remember me" 
		condition so users will have to re-enter
		their information to log back in. 

	-Log off: Alternative Cases
		-user closes the application 		
		-> the user is NOT taken to the login 
		   page, the application still logs the
		   user automatically if "Remember Me"
		   was selected.

	-Display market information
		from a populated farmers market list, the
		user selects one of the markets by
		tapping on one in the list. When
		selected, the user will be taken to a
		screen which displays the information 

	-Display market information: Alternative Cases
		the user does not know they can select a
		market and becomes frustrated that all
		their friends can access information on
		our farmers market app.
		-> the user asks a friend for help :)
		-> the user uninstalls our app :(

	-Display map directions
		from market information screen, the
		user selects the hyperlink labled
		"Directions". The user will be taken to
		a Google Map screen with the given
		farmers market selected  

	-Display map directions: Alternative Cases
		the user does not know they can select
		the directions hyperlink and becomes
		frustrated that all their friends can
		recognise a hyperlink in an application
		-> the user asks a friend for help :)
		-> the user doesn't care for directions 
		   and misses some of our app's 
		   functionality :|
		-> the user uninstalls our app :(