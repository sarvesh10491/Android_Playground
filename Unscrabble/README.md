# Unscrabble

App to implement client-server model to generate all valid Scrabble word combinations for input set of letters.  

Unscrabble_Apps contains Android app code.  

Unscrabble_Apps contains server-side nodejs code to connect with client app & python code with wordlist that will be used to generate set of valid words to return. Upload this on your server which has nodejs installed. run below in folder to get server running.  

```
javascript
nodejs unscrabble_appserver.js
```

Change your server's IP in Unscrabble_App\app\src\main\java\com\example\unscrabble\properties\Constants.java to get it working with your own server.