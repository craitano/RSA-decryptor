# RSA Decryptor
This RSA decryption tool was created to solve the RSA decryption problems for PicoCTF 2017.
It decrypts rsa given the inputs c, n, and d; c, p, q, dp, and dq; or c,n, and e if d is small.

## Compilation
From the project directory run the following command:
```
javac -d bin/ -cp src ./src/*.java
```
## Running the App
From the project directory run the following command:
```
java -cp bin GUI
```
