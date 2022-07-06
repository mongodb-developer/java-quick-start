package com.software.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Util {

	
	
	
	public static String ToBase64(byte[] b){
		 
		String image=Base64.getEncoder().encodeToString(b);
		 
		return image;
		 
		}
	
	
	public static byte[] ToByte(String base64){
		String s=null;
		
			base64=base64.replace(" ", "");
			base64=base64.replace("\n", "");
			base64=base64.replace("\r", "");
			
		
		return Base64.getDecoder().decode(base64);
		 
		}
}
