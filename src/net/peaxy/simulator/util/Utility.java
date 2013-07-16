package net.peaxy.simulator.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Utility {

	private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
	private static long id = 0;
	private static long messageID = 0;
	private Utility() {
	}

	public static void main(String[] ar){
		
	}
	
	public static long getID(){
		return ++id;
	}
	
	public static String getMessageID(){
		return ++messageID + "";
	}
	
	public static boolean isPingable(String ip){
		boolean valid = true;
		try {
			File f = new File("simulator/" + ip + ".ip");
			valid = f.exists();
		} catch (Exception e){
			valid = false;
		}
		return valid;
	}
	
	public static boolean isValidIP(String ip){
		boolean valid = true;
		if(ip != null && !"".equals(ip)){
			try{
				String[] subIP = ip.split("\\.");
				if(subIP.length == 4){
					int sub;
					for(int i=0;i<4;i++){
						sub = Integer.parseInt(subIP[i]);
						if(sub < 0 || sub > 255){
							valid = false;
							break;
						}
					}
				} else {
					valid = false;
				}
			} catch(Exception e){
				valid = false;
			}
		} else {
			valid = false;
		}
		return valid;
	}
	
	public static List<String> getIpList(String ipStr){
		List<String> list = new ArrayList<String>();
		if(Utility.isValidIP(ipStr)){
			list.add(ipStr);
		} else {
			String[] subip = ipStr.split("\\.");
			if(subip.length == 4){
				boolean valid = true;
				for(int i=0;i<3;i++){
					try{
						int ipv = Integer.parseInt(subip[i]);
						if(ipv < 0 || ipv > 254){
							valid = false;
							break;
						}
					}catch(Exception e){
						valid = false;
						break;
					}
				}
				if(valid){
					if("*".equals(subip[3])){
						String tmp = "";
						for(int i=1;i<255;i++){
							tmp = subip[0]+"."+subip[1]+"."+subip[2]+"."+i;
							if(!list.contains(tmp)){
								list.add(tmp);
							}
						}
					} else if(subip[3].indexOf("-") > 0){
						String tmp = "";
						String[] range = subip[3].split("-");
						if(range.length == 2){
							try {
								int r1 = Integer.parseInt(range[0]);
								int r2 = Integer.parseInt(range[1]);
								for(int i = r1;i<r2+1;i++){
									tmp = subip[0]+"."+subip[1]+"."+subip[2]+"."+i;
									if(!list.contains(tmp)){
										list.add(tmp);
									}
								}
							} catch (Exception e){
							}
						}
					}
				}
			}
		}
		return list;
	}
	
	public static boolean isNetmask(String netmask){
		boolean valid = true;
		int mask = 0;
		if(netmask != null && !"".equals(netmask)){
			try{
				String[] subIP = netmask.split("\\.");
				if(subIP.length == 4){
					int sub;
					for(int i=0;i<4;i++){
						sub = Integer.parseInt(subIP[i]);
						mask |= (sub << (24-i*8));
					}
					String s = Integer.toBinaryString(mask);
					int index = s.indexOf("0");
					if(s.indexOf("1",index) > 0){
						valid = false;
					}
				} else {
					valid = false;
				}
			} catch(Exception e){
				valid = false;
			}
		} else {
			valid = false;
		}
		return valid;
	}
	
	public static String generateSN(int length){
		Random r = new Random(System.nanoTime());
		char[] chars = new char[length];
		for(int i=0;i<length;i++){
			chars[i] = numbersAndLetters[r.nextInt(numbersAndLetters.length-1)];
		}
		return new String(chars);
	}

	public static void writeInstallLog(String logFile,String message){
		try {
			File f = new File(logFile);
			DateFormat df = new SimpleDateFormat("[HH:mm.ss]");
			f.createNewFile();
			FileOutputStream bos = new FileOutputStream(f, true);
			OutputStreamWriter osw = new OutputStreamWriter(bos);
			osw.write(df.format(new Date()));
			osw.write("\t");
			osw.write(message);
			osw.write("\r\n");
			osw.close();
			bos.close();
		} catch (Exception e) {
		}
		
		
	}
	
	public static String readFile(String filename) {
		StringBuffer buffer = new StringBuffer();
		File f = new File(filename);
		if (f.canRead()) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(f));
				String line = br.readLine();
				while (line != null) {
					buffer.append(line);
					line = br.readLine();
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return buffer.toString();
	}

	public static boolean canDiscover() {
		return !new File("simulator/servers.ser").exists();
	}
}
