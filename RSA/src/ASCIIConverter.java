import java.math.BigInteger;

public class ASCIIConverter {
	static String toASCII(BigInteger num) {
		// get binary representation as a string
		String binary = num.toString(2);
		return binaryStringToASCII(binary);
	}
	
	static String binaryStringToASCII(String s) {
		String message = "";
		// pad with leading 0s
		while(s.length() % 8 != 0) {
			s = "0" + s;
		}
		for(int i = 0; i < s.length() ; i += 8) {
			message += (char)(Integer.parseInt(s.substring(i,i + 8), 2));
		}
		return message;
	}
}
