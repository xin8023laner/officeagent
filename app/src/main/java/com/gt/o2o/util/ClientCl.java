/**
 * 贯通云网ClientCl
 */
package com.gt.o2o.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * ClientCl
 * @author uimagine
 */
public class ClientCl  extends ClassLoader {
	
	private String destPath;
	
	public ClientCl(String destPath) {
		this.destPath = destPath;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		String destPath = this.destPath;
		String className = name.substring(name.lastIndexOf('.') + 1);
		String classFileName = destPath + "\\" + className + ".class";
		InputStream fileInStream = null;
		ByteArrayOutputStream byteArrayOutStream = null;
		try {
			fileInStream = new FileInputStream(classFileName);
			byteArrayOutStream = new ByteArrayOutputStream();
			ec(fileInStream, byteArrayOutStream);
			byte[] classByte = byteArrayOutStream.toByteArray();
			return defineClass(classByte, 0, classByte.length);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				byteArrayOutStream.close();
				fileInStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return super.findClass(name);
	}
	
	/**
	 * ec
	 */
	public void ec(InputStream inStream, OutputStream outStream)
			throws Exception {
		int b = -1;
		while ((b = inStream.read()) != -1) {
			outStream.write(b ^ 0xff);
		}
	}
	
}