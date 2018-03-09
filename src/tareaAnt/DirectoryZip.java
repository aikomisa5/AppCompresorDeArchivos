package tareaAnt;




import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class DirectoryZip {


	
	public static void main(String[] args) throws IOException {
		
	
		File directoryToZip = new File("D:\\logs_engines_produccion");
		
		List<File> fileList = new ArrayList<File>();
		System.out.println("---Getting references to all files in: " + directoryToZip.getCanonicalPath());
		getAllFiles(directoryToZip, fileList);
		System.out.println("---Creating zip file");
		comprimir(fileList);
		System.out.println("---Done");
	}
	
	
	
	
	public static void getAllFiles(File dir, List<File> fileList) {
		try {
			File[] files = dir.listFiles();
			
			for (File file : files) {
				fileList.add(file);				
				if (file.isDirectory()) {
					//System.out.println("directory:" + file.getCanonicalPath());
					//System.out.println("dir : " + file.getName());
					getAllFiles(file, fileList);
					
				} else {
					System.out.println("     file:" + file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws FileNotFoundException,
			IOException {

		FileInputStream fis = new FileInputStream(file);

		// we want the zipEntry's path to be a relative path that is relative
		// to the directory being zipped, so chop off the rest of the path
		String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
				file.getCanonicalPath().length());
		System.out.println("Writing '" + zipFilePath + "' to zip file");
		ZipEntry zipEntry = new ZipEntry(zipFilePath);
		zos.putNextEntry(zipEntry);
		zos.setMethod(Deflater.DEFLATED);
		zos.setLevel(Deflater.BEST_COMPRESSION);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}
	
	

	public static void writeZipFile(File directoryToZip, List<File> fileList) {

		try {
			FileOutputStream fos = new FileOutputStream(directoryToZip.getCanonicalPath()+"\\"+directoryToZip.getName() + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			
			System.out.println("Comprimiendo en " + directoryToZip.getCanonicalPath());
			
			for (File file : fileList) {
				if (!file.isDirectory()) { // we only zip files, not directories
					addToZip(directoryToZip, file, zos);
					file.delete();
				}else {
					List<File> arrayArchivos = new ArrayList<File>();
					getAllFiles(file,arrayArchivos);
					comprimir(arrayArchivos);
				}
			}

			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	public static void comprimir (List<File> fileList) throws FileNotFoundException,
	IOException {
		
		for(File archivo : fileList ) {
			if( archivo.isDirectory()) {
				List<File> arrayArchivos = new ArrayList<File>();
				getAllFiles(archivo,arrayArchivos);
				if(!estaComprimido(archivo)) {
					//comprimir los archivos
					//writeZipFile(dir,archivo.listFiles());
					if(!(archivo.listFiles().length == 0)) {
						writeZipFile(archivo,arrayArchivos);
						System.out.println("Carpeta "+archivo.getName()+" Se creo zip");
					}
				}else {
					//borrar los archivos menos el zip
					borrarArchivos(arrayArchivos);
					System.out.println("Carpeta " +archivo.getName()+" tiene zip");
					
				}
			}
		}
		
		
	}
	
	
	
	public static void borrarArchivos(List<File> directorio) throws IOException {
		
		for (File archivo : directorio) {
			if(!archivo.getName().contains(".zip") && !archivo.getName().contains(".rar")) {
				archivo.delete();	
				System.out.println("borrando archivo " + archivo.getCanonicalPath());
			}
		
		}
	}


	
	public static boolean estaComprimido(File dir ) {
		
		boolean estaComprimido = false;
		
		try {
		//recorrer el array buscando un archivo de extenxion zip
			File[] arrayArchivos = dir.listFiles();
			
			for(File archivo : arrayArchivos) {
				if(archivo.getName().contains(".zip") || archivo.getName().contains(".rar")){
					estaComprimido = true;
					break;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	
		return estaComprimido;
	}
	
	
	
	
}