STEP BY STEP PROCEDURE HOW TO DO AN ANDROID APP WHICH USES THE CURRENT GPS LOCATION OF THE USER in ECLIPSE:
1) First in order to use Google Maps Api in your application you have to create an API KEY for your own application.
2)I used a release keystore instead of using a debug keystore.
3)In order to create a release key store right click your android and click on EXPORT
4)A window open and click on the android and click on EXPORT ANDROID APPLICATION
5)Then click on create a new keystore and follow the steps given by giving a password for your keystore(Save that key because you'll be using that key if you're planning to release your app)
6)Then open command prompt do the following steps
6.1) cd C:\Program Files\Java\jdk1.7.0_04\bin (go to the location where your jdk is stored)
6.2)keytool.exe -V -list -alias "<your alias name>" -keystore "<location where your keystore i stored>" -storepass "<your password>" -keypass "<your password>"
6.3) Make sure you copy the MD5 key,DO NOT COPY THE SHA-1 key.
6.4)go to this link ----> https://developers.google.com/maps/documentation/android/v1/maps-api-signup
6.5) Paste your MD5 key and your maps api key will be generated along with the xml file copy paste the api-key in your android application
7) Then instead of testing in the emulator you can send the .apk to your phone.
8)In order to send the .apk file,right click your application click on Export -> Export ANdroid application ->Use existing keystore->enter your password and alias name
9)Just send the .apk file to your phone and your phone will install the apk file automatically.
10) There you go your very own google maps application 
