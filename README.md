# Password-Manager
Password manager using Javafx and AES algorithm for encryption/decryption  
This is a project from a course from my university, Information System Security. This project doesn't have an executable yet, so to use it, build it from source using [Intellij](https://www.jetbrains.com/idea/) with [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).
## Feature:  
- Store passwords locally on your machine in [JSON format](https://www.json.org/json-en.html). Passwords are encrypted/decrypted with the [AES algorithm](https://en.wikipedia.org/wiki/Advanced_Encryption_Standard).
- Access all the passwords with a master password. Users must remember the master password (and maybe other passwords).
- Users can group passwords into a Group. Groups can either have their own password or not and must be remembered. The group's password and the master password will be hashed with the [SHA256 algorithm](https://en.wikipedia.org/wiki/SHA-2).
## Library:
- [JavaFX 18](https://mvnrepository.com/artifact/org.openjfx) for the UX/UI.
- [org.json](https://mvnrepository.com/artifact/org.json/json) for handling JSON file.