package usuarios;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Hasher {

	public static String hashSHA512(String passwordToHash, String salt) 
	{
		String hashedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt.getBytes());
			byte[] bytes = md.digest(passwordToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			hashedPassword = sb.toString();
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		}
		return hashedPassword;
	}
	
	public static Boolean sonCorrespondientes(String passwordIngresada, String[] hashedPasswordWithSalt) 
	{
		String storedHash = hashedPasswordWithSalt[0];
		String storedSalt = hashedPasswordWithSalt[1];
		String hashedpassword = hashSHA512(passwordIngresada, storedSalt);
		return hashedpassword.equals(storedHash);
	}
	
	public static String generarSalt() throws NoSuchAlgorithmException
	{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt.toString();
	}
	
}
