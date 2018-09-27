import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Decryption {
	
	/**
	 * Attempt to decrypt the message from the given args
	 * @param args
	 * @return
	 * 		An integer representation of the decrypted message, if enough information is given to decrypt it
	 * 		null, otherwise
	 */
	static BigInteger decrypt(boolean usePrivateKey, Map<String, BigInteger> args) {
		// ensure there is a ciphertext
		if(args.containsKey("c")) {
			// check if public or private key should be used
			if(usePrivateKey) {
				/* Decrypt a message encrypted with the public key */
				// use private key if available,
				if(args.containsKey("n") && args.containsKey("d")) {
					return decrypt_c_n_d(args.get("c"), args.get("n"), args.get("d"));
				}
				
				// get dp and dq if possible
				if(args.containsKey("p") && args.containsKey("d") && !args.containsKey("dp"))
					args.put("dp", getDP(args.get("d"), args.get("p")));
				if(args.containsKey("q") && args.containsKey("d") && !args.containsKey("dq"))
					args.put("dq", getDP(args.get("d"), args.get("q")));
				
				// use primes to calculate, if possible
				if(args.containsKey("p") && args.containsKey("q") && args.containsKey("dp") && args.containsKey("dq")) {
					return decrypt_c_p_q_dp_dq(args.get("c"), args.get("p"), args.get("q"), args.get("dp"), args.get("dq"));
				}
				if(args.containsKey("p") && args.containsKey("q") && args.containsKey("d")) {
					BigInteger d = args.get("d");
					BigInteger dq = getDQ(d, args.get("q"));
					BigInteger dp = getDP(d, args.get("p"));
					return decrypt_c_p_q_dp_dq(args.get("c"), args.get("p"), args.get("q"), dq, dp);
				}
				/* Decrypt a message encrypted with the public key */
				if(args.containsKey("n") && args.containsKey("e")) {
					return decrypt_c_n_e(args.get("c"), args.get("n"), args.get("e"));
				}
				
			}else {
				/* Decrypt a message encrypted with the public key */
				if(args.containsKey("n") && args.containsKey("e")) {
					// Can use the same function as private key decryption
					return decrypt_c_n_e(args.get("c"), args.get("n"), args.get("e"));
				}
			}
		}
		return null;
	}
	
	/**
	 * Decrypt given c, n, and d
	 * @param c
	 * @param n
	 * @param d
	 * @return
	 * 		An integer representation of the decrypted message.
	 */
	private static BigInteger decrypt_c_n_d(BigInteger c, BigInteger n, BigInteger d){
		return c.modPow(d, n);
	}
	
	/**
	 * Attempt to decrypt given c, n, and e using Wiener's attack
	 * @param c
	 * @param n
	 * @param e
	 * @return
	 * 		An integer representation of the decrypted message.
	 */
	private static BigInteger decrypt_c_n_e(BigInteger c, BigInteger n, BigInteger e){
		// These values are used throughout the method
		final BigInteger zero = new BigInteger("0");
		final BigInteger one = new BigInteger("1");
		final BigInteger two = new BigInteger("2");
		final BigInteger four = new BigInteger("4");
		// Get the continued fraction expansion
		List<BigInteger> continuedFraction = new ArrayList<>();
		BigInteger divisor = n.multiply(one);
		BigInteger result = e.divide(divisor);
		BigInteger remainder = e.mod(divisor);
		continuedFraction.add(result);
		while (!remainder.equals(zero)) {
			BigInteger num = divisor;
			divisor = remainder;
			result = num.divide(divisor);
			remainder = num.mod(divisor);
			continuedFraction.add(result);
		}
		// Attempt to find likely roots of N
		int i = 0;
		while(i < continuedFraction.size()) {
			// find k and d by simplifying continued fraction up to i
			BigInteger k = continuedFraction.get(i);
			BigInteger d = new BigInteger("1");
			for(int j = i; j > 0; j--) {
				BigInteger tmp = k;
				k = d;
				d = tmp;
				k = k.add(continuedFraction.get(j-1).multiply(d));
			}
			if(!k.equals(zero)) {
				// calculate p(N) numerator
				BigInteger pN = (e.multiply(d)).subtract(one);
				// if p(N) is a whole number, Solve x^2-(n-p(N)+1)x+N=0
				if(pN.mod(k).equals(zero)) {
					pN = pN.divide(k);
					BigInteger b = n.subtract(pN).add(one);
					//quadratic formula
					try {
						BigInteger p = b.negate().add(b.multiply(b).subtract(four.multiply(n)).sqrt()).divide(two);
						BigInteger q = b.negate().subtract(b.multiply(b).subtract(four.multiply(n)).sqrt()).divide(two);
						System.out.println("p:" + p + ", q: " + q);
						p = p.abs();
						q = q.abs();
						if(n.equals(p.multiply(q))) {
							return decrypt_c_n_d(c, n, d);
						}
					} catch(ArithmeticException exception) {
						// d is too large for weiner's method to work
						return null;
					}
					
				}
			}
			i++;
		}
		
		return null;
	}
	
	/**
	 * Decrypt given c, p, q, dp, and dq
	 * @param c
	 * @param p
	 * @param q
	 * @param dp
	 * @param dq
	 * @return
	 * 		An integer representation of the decrypted message.
	 */
	private static BigInteger decrypt_c_p_q_dp_dq(BigInteger c, BigInteger p, BigInteger q, BigInteger dp, BigInteger dq) {
		BigInteger qinv = q.modInverse(p);
		BigInteger m1 = c.modPow(dp, p);
		BigInteger m2 = c.modPow(dq, q);
		BigInteger h = qinv.multiply(m1.subtract(m2)).mod(p);
		return m2.add(h.multiply(q));
	}
	
	private static BigInteger getDQ(BigInteger d, BigInteger q){
		// TODO dq = d mod (q-1)
		return null;
	}
	
	private static BigInteger getDP(BigInteger d, BigInteger q){
		// TODO dp = d mod (p-1)
		return null;
	}

}
