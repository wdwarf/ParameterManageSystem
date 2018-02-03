package com.parammgr.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AutoDeleteFileInputStream extends FileInputStream {
	private String fileName;

	public AutoDeleteFileInputStream(String name) throws FileNotFoundException {
		super(name);
		
		this.fileName = name;
	}

	public void finalize() throws IOException {
		super.finalize();
		File f = new File(fileName);
		if(f.exists()) {
			System.out.println("Deleting file: " + fileName);
			f.delete();
		}
	}
}
