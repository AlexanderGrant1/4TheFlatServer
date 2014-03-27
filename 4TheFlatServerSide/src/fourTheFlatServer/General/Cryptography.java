package fourTheFlatServer.General;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Cryptography 
{	
	public static String sha256(String input) {
	     
	        MessageDigest md;
			try {
				md = MessageDigest.getInstance("SHA-256");

	        md.update(input.getBytes());
	 
	        byte byteData[] = md.digest();
	 
	        //convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	 
	        System.out.println("Hex format : " + sb.toString());
	 
	        //convert the byte to hex format method 2
	        StringBuffer hexString = new StringBuffer();
	     for (int i=0;i<byteData.length;i++) {
	      String hex=Integer.toHexString(0xff & byteData[i]);
	          if(hex.length()==1) hexString.append('0');
	          hexString.append(hex);
	     }
	     return hexString.toString();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
	}
}