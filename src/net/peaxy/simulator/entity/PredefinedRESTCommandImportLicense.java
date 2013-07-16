package net.peaxy.simulator.entity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.core.MultivaluedMap;

import net.peaxy.simulator.data.TableSpace;
import net.peaxy.simulator.entity.domain.License;
import net.peaxy.simulator.util.RSA;
import sun.misc.BASE64Decoder;

public class PredefinedRESTCommandImportLicense extends PredefinedRESTCommand {

	private static final long serialVersionUID = 6131027489884131606L;
	private int errorCount = 0;
	private final String LICENSE_FILENAME = "license.lic";
	private final String Key_FILENAME = "private.key";

	@Override 
	public Output invoke(final MultivaluedMap<String, String> queryParameters, final String sessionID, final String host) {
		try {
			generateLicenseFile();
		} catch (Exception e) {
			errorCount++;
			return generateOutput(null,"Can't generate license file : " + errorCount,204,false);
		}
		return generateOutput(null,null,201,true);
	}
	
	@Override
	public Output invoke(final MultivaluedMap<String, String> queryParameters, final MultivaluedMap<String, String> formParameters, final String sessionID, final String host) {
		String tableKey = findTableKey(formParameters,this.parameters);
		if(parameters == null || parameters.isEmpty()){
			return generateOutput(null,"The request is invalid.",401,false);
		}
		Set<String> names = formParameters.keySet();
		BASE64Decoder decode = new BASE64Decoder();
		License license = null;
		for (String name : names) {
			CommandParameter parameter = this.parameters.get(name);
			if(parameter != null){
				String value = formParameters.getFirst(name);
				try {
					byte[] data = decode.decodeBuffer(value);
					license = readLicense(data);
					if(license != null){
						break;
					}
				} catch (Exception e) {
					return generateOutput(null,"License is not valid.",205,false);
				}
			}
		}
		if(license != null){
			TableSpace.getInstance().setData(this.getStorageName(),tableKey, license.toJSON());
			return generateOutput(license.toJSON(),null,200,true);
		} else {
			return generateOutput(null,"License is not valid.",205,false);
		}
	}
	
	private License readLicense(byte[] data) throws Exception {
		License license = null;
		byte[] cp = null;
		Hashtable<String, BigInteger> privateKey = null;
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ZipInputStream zin = new ZipInputStream(bais);
		ZipEntry entry = null;
		while ((entry = zin.getNextEntry()) != null) {
			if (entry.isDirectory() || entry.getName().equals("..\\"))
				continue;
			if(LICENSE_FILENAME.equalsIgnoreCase(entry.getName())){
				BufferedInputStream bin = new BufferedInputStream(zin);
				cp = new byte[bin.available()];
				bin.read(cp);
			} else if(Key_FILENAME.equalsIgnoreCase(entry.getName())){
				BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(zin)));
				privateKey = new Hashtable<String, BigInteger>();
				privateKey.put("p", new BigInteger(br.readLine()));
				privateKey.put("q", new BigInteger(br.readLine()));
				privateKey.put("a", new BigInteger(br.readLine()));
			}
		}
		zin.close();
		bais.close();
		if(cp != null && privateKey != null){
			RSA rsa = new RSA();
			rsa.setPrivateKey(privateKey);
			byte[] plain = rsa.decrypt(cp);
			license = new License();
			license.getLicense(plain);
		}
		return license;
	}
	
	private void generateLicenseFile( ) throws Exception {
		RSA rsa = new RSA();
		rsa.genKey(1024);
		BufferedWriter keyout = new BufferedWriter(
				new FileWriter(Key_FILENAME));
		keyout.write(rsa.getPrivateKey().get("p").toString());
		keyout.newLine();
		keyout.write(rsa.getPrivateKey().get("q").toString());
		keyout.newLine();
		keyout.write(rsa.getPrivateKey().get("a").toString());
		keyout.newLine();
		keyout.flush();
		keyout.close();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(128);
		baos.write("Exception in thread \"main\" java.lang.NumberFormatException: Zero length BigInteger".getBytes());
		byte[] cipher = rsa.encrypt(baos.toByteArray());
		FileOutputStream fileout = new FileOutputStream(
				new File(LICENSE_FILENAME));
		fileout.write(cipher);
		fileout.flush();
		fileout.close();
        
		File file = new File(Key_FILENAME);
		File zipFile = new File("license.zip");
		if(zipFile.exists())
			throw new Exception("The License file has existed.");
		FileOutputStream fileOutputStream = new FileOutputStream(zipFile);  
        CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,  
                new CRC32());  
        ZipOutputStream out = new ZipOutputStream(cos); 
		BufferedInputStream bis = new BufferedInputStream(  
                new FileInputStream(file));  
        ZipEntry entry = new ZipEntry(file.getName());
        out.putNextEntry(entry);  
        int count;  
        byte data[] = new byte[1024];  
        while ((count = bis.read(data, 0, 1024)) != -1) {  
            out.write(data, 0, count);  
        }
        bis.close();
        file.delete();
        file = new File(LICENSE_FILENAME);
        bis = new BufferedInputStream(  
                new FileInputStream(file));
        entry = new ZipEntry(file.getName());
        out.putNextEntry(entry); 
        while ((count = bis.read(data, 0, 1024)) != -1) {  
            out.write(data, 0, count);  
        }
        out.close();
        bis.close(); 
        file.delete();
	}
}
