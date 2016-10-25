import org.apache.commons.codec.binary.Base64;


public class PasswordEncoderAndDecoder {

	public static void main(String[] args) {
	//Base64 Encoding and Decoding
		
		String orgPassword = "pass@1234";
		
		String encodedPassword = new String(Base64.encodeBase64(orgPassword.getBytes()));
		
		System.out.println(orgPassword+"---"+encodedPassword);
		
		String decodedPassword = new String(Base64.decodeBase64(encodedPassword.getBytes()));
		
		System.out.println(encodedPassword+"---"+decodedPassword);
		
		if(orgPassword.equals(decodedPassword))
		{
			System.out.println("Passwords are same");
		}

	}

}
