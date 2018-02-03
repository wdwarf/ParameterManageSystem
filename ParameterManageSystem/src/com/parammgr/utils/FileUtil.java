package com.parammgr.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.struts2.ServletActionContext;

public class FileUtil {

	private static String tempDir = "";

	static {
		tempDir = ServletActionContext.getServletContext().getRealPath("/" + Consts.TEMP_DIR_NAME);
		System.out.println("tempDir: " + tempDir);
		File f = new File(tempDir);
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	/**
	 * 临时目录
	 * @return
	 */
	public static String getTempDir() {
		return tempDir;
	}

	/**
	 * 删除文件/目录
	 * @param f
	 */
	public static void rm(File f) {
		if (!f.exists())
			return;

		System.out.println("Deleting " + f.getPath());
		
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (File file : files) {
				rm(file);
			}
		}

		if (!f.delete()) {
			System.out.println("Delete failed: " + f.getPath());
		}
	}

	public static void rm(String path) {
		File f = new File(path);
		if (!f.exists())
			return;

		FileUtil.rm(f);
	}

	/**
	 * 取后缀
	 * @param fileName
	 * @return
	 */
	public static String getSuffix(String fileName) {
		int pos = fileName.lastIndexOf(".");
		if (pos < 0) {
			return "";
		}
		return fileName.substring(pos + 1);
	}

	/**
	 * 查找目录
	 * @param rootDir
	 * @param findDirName
	 * @return
	 */
	public static String findDir(String rootDir, String findDirName) {
		File f = new File(rootDir);
		String path = "";
		for (File file : f.listFiles()) {
			if (file.isDirectory()) {
				String tempPath = file.getPath();
				if (findDirName.equals(file.getName())) {
					path = tempPath;
					break;
				} else {
					tempPath = findDir(tempPath, findDirName);
					if (!tempPath.isEmpty()) {
						path = tempPath;
						break;
					}
				}
			}
		}

		return path;
	}

	/**
	 * 列出subfix类型的文件
	 * @param files
	 * @param path
	 * @param subfix
	 */
	public static void listFiles(List<String> files, String path, String subfix) {
		File f = new File(path);
		subfix = subfix.trim().toLowerCase();
		if (f.isDirectory()) {
			for (File file : f.listFiles()) {
				listFiles(files, file.getPath(), subfix);
			}
		} else {
			if (subfix.equals(getSuffix(f.getPath()).trim().toLowerCase())) {
				files.add(f.getPath());
			}
		}
	}

	/**
	 * 解压文件
	 * @param zipFile
	 * @param ulTempDir
	 * @throws IOException
	 */
	public static void unzipFile(File zipFile, String ulTempDir) throws IOException {
		ZipFile z = new ZipFile(zipFile, Charset.forName("GBK"));
		@SuppressWarnings("unchecked")
		Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) z.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			String entryFilePath = ulTempDir + File.separator + entry.getName();
			if (entry.isDirectory()) {
				File dir = new File(entryFilePath);
				dir.mkdirs();
			} else {
				File f = new File(entryFilePath);
				if (!f.exists()) {
					File dir = new File(f.getParent());
					dir.mkdirs();
				}
				f.createNewFile();
				InputStream is = z.getInputStream(entry);
				FileOutputStream out = new FileOutputStream(f);

				int count;
				byte[] buf = new byte[8192];
				while ((count = is.read(buf)) != -1) {
					out.write(buf, 0, count);
				}

				is.close();
				out.close();
			}
		}
		z.close();
	}

	public static void unzipFile(String zipFile, String ulTempDir) throws IOException {
		FileUtil.unzipFile(new File(zipFile), ulTempDir);
	}
	
	private static void doZipFiles(File root, String parentPath, ZipOutputStream zos) throws IOException{
		if(root.isDirectory()){
			String base = root.getName();
			if(!parentPath.isEmpty()){
				base = parentPath + File.separator + base;
			}
			for(File file : root.listFiles()){
				doZipFiles(file, base, zos); 
			}
		}else{
			if(!parentPath.isEmpty()){
				zos.putNextEntry(new ZipEntry(parentPath + File.separator + root.getName()));
			}else{
				zos.putNextEntry(new ZipEntry(root.getName()));
			}

			FileInputStream in = new FileInputStream(root);
			int count = 0;
			byte[] buf = new byte[8192];
			while((count = in.read(buf)) != -1){
				zos.write(buf, 0, count);
			}
			in.close();
			zos.closeEntry();
		}
	}
	
	/**
	 * 压缩文件
	 * @param path
	 * @param outputFile
	 * @throws IOException
	 */
	public static void zipFiles(String path, File outputFile) throws IOException{
		System.out.println("zip [" + path + "] to [" + outputFile.getPath() + "]");
		CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(outputFile), new CRC32());
		ZipOutputStream zos = new ZipOutputStream(cos);
		
		doZipFiles(new File(path), "", zos);
		zos.closeEntry();
		zos.close();
		cos.close();
	}
}
