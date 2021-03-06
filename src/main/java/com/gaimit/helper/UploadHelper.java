package com.gaimit.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

public class UploadHelper {
	
	/** ?λ‘λ ? κ²°κ³Όλ¬Όμ΄ ???₯?  ?΄? */
	public String fileDir = null;
	/** ?λ‘λκ°? μ§ν?  ?? ?΄? */
	public String tempDir = null;
	
	public UploadHelper(String homeDir) {
		this.fileDir = homeDir + "/upload";
		this.tempDir = fileDir + "/temp";
	}

	/** File? λ³΄λ?? ???₯?κΈ? ?? μ»¬λ ? */
	private List<FileInfo> fileList;

	/** κ·? λ°μ ?Όλ°? ?°?΄?°λ₯? ???₯?κΈ? ?? μ»¬λ ? */
	private Map<String, String> paramMap;

	/** ?λ‘λ? ??Ό? λ¦¬μ€?Έλ₯? λ¦¬ν΄??€. */
	public List<FileInfo> getFileList() {
		return this.fileList;
	}

	/** ?λ‘λ?? ?¨κ»? ? ?¬? ??Όλ―Έν°?€? μ»¬λ ?? λ¦¬ν΄??€. */
	public Map<String, String> getParamMap() {
		return this.paramMap;
	}

	/**
	 * Multipartλ‘? ? ?‘? ?°?΄?°λ₯? ?λ³ν?¬ ??Όλ¦¬μ€?Έ?? ??€?Έ ??Όλ―Έν°λ₯? λΆλ₯??€.
	 * @throws Exception
	 */
	public void multipartRequest() throws Exception {
		/** JSP ?΄?₯κ°μ²΄λ₯? ?΄κ³? ?? Spring? κ°μ²΄λ₯? ?΅?΄? ?΄?₯κ°μ²΄ ???κΈ? */
		// --> import org.springframework.web.context.request.RequestContextHolder;
		// --> import org.springframework.web.context.request.ServletRequestAttributes;
		ServletRequestAttributes requestAttr 
			= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttr.getRequest();
		
		/** multipartλ‘? ? ?‘???μ§? ?¬λΆ? κ²??¬ */
		// --> import org.apache.commons.fileupload.servlet.ServletFileUpload
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		if (!isMultipart) {
			// ? ?‘? ?°?΄?°κ°? ??Όλ―?λ‘? κ°μ  ??Έ λ°μ.
			throw new Exception();
		}

		/** ?΄?? μ‘΄μ¬ ?¬λΆ? μ²΄ν¬?΄? ??±?κΈ? */
		// import java.io.File
		File uploadDirFile = new File(fileDir);
		if (!uploadDirFile.exists()) {
			uploadDirFile.mkdirs();
		}

		File tempDirFile = new File(tempDir);
		if (!tempDirFile.exists()) {
			tempDirFile.mkdirs();
		}

		/** ?λ‘λκ°? ???  ?? ?΄? ?°κ²? */
		// --> import org.apache.commons.fileupload.disk.DiskFileItemFactory
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(tempDirFile);

		/** ?λ‘λ ?? */
		ServletFileUpload upload = new ServletFileUpload(factory);
		// UTF-8 μ²λ¦¬ μ§?? 
		upload.setHeaderEncoding("UTF-8");
		// μ΅λ? ??Ό ?¬κΈ? --> 20M
		upload.setSizeMax(20 * 1024 * 1024);
		// ?€?  ?λ‘λλ₯? ????¬ ??Ό λ°? ??Όλ―Έν°?€? ?»κΈ?
		List<FileItem> items = upload.parseRequest(request);

		// items? ???₯ ?°?΄?°κ°? λΆλ₯?  μ»¬λ ??€ ? ?Ή?κΈ?
		fileList = new ArrayList<FileInfo>();
		paramMap = new HashMap<String, String>();

		/** ?λ‘λ ? ??Ό? ? λ³? μ²λ¦¬ */
		for (int i = 0; i < items.size(); i++) {
			// ? ?‘? ? λ³? ??λ₯? μΆμΆ??€.
			// import org.apache.commons.fileupload.FileItem
			FileItem f = items.get(i);

			if (f.isFormField()) {
				/** ??Ό ??? ?°?΄?°κ°? ?? κ²½μ° --> paramMap? ? λ³? λΆλ₯ */
				String key = f.getFieldName();
				// valueλ₯? UTF-8 ???Όλ‘? μ·¨λ??€.
				String value = f.getString("UTF-8");

				// ?΄λ―? ??Ό? ?€κ°μ΄ map?? μ‘΄μ¬??€λ©?? --> checkbox
				if (paramMap.containsKey(key)) {
					// κΈ°μ‘΄? κ°? ?€? μ½€λ§(,)λ₯? μΆκ??΄? κ°μ λ³ν©??€.
					String new_value = paramMap.get(key) + "," + value;
					paramMap.put(key, new_value);
				} else {
					// κ·Έλ μ§? ??€λ©? ?€?? κ°μ ? κ·λ‘ μΆκ???€.
					paramMap.put(key, value);
				}

			} else {
				/** ??Ό ??? ?°?΄?°?Έ κ²½μ° --> fileList? ? λ³? λΆλ₯ */
				
				/** 1) ??Ό? ? λ³΄λ?? μΆμΆ??€ */
				String orginName = f.getName(); 		// ??Ό? ?λ³? ?΄λ¦?
				String contentType = f.getContentType();// ??Ό ??
				long fileSize = f.getSize(); 			// ??Ό ?¬?΄μ¦?

				// ??Ό ?¬?΄μ¦κ? ??€λ©? μ‘°κ±΄?Όλ‘? ??κ°λ€.
				if (fileSize < 1) {
					continue;
				}

				// ??Ό?΄λ¦μ? ??₯?λ§? μΆμΆ
				String ext = orginName.substring(orginName.lastIndexOf("."));

				/** 2) ??Ό? ?΄λ¦μ ??Ό?΄ μ‘΄μ¬??μ§? κ²??¬??€. */
				// ?Ή ?λ²μ ???₯?  ?΄λ¦μ "??¬? Timestamp+??₯?(ext)"λ‘? μ§??  (μ€λ³΅???₯ ?°? €)
				String fileName = System.currentTimeMillis() + ext;
				// ???₯? ??Ό ? λ³΄λ?? ?΄κΈ? ?? Fileκ°μ²΄
				File uploadFile = null;
				// μ€λ³΅? ?΄λ¦μ ??Ό?΄ μ‘΄μ¬?  κ²½μ° indexκ°μ 1?© μ¦κ??λ©΄μ ?€? ?§λΆμΈ?€.
				int index = 0;
				
				// ?Ό?¨ λ¬΄νλ£¨ν
				while (true) {
					// ?λ‘λ ??Ό?΄ ???₯?  ?΄? + ??Ό?΄λ¦μΌλ‘? ??Όκ°μ²΄λ₯? ??±??€.
					uploadFile = new File(uploadDirFile, fileName);

					// ??Ό? ?΄λ¦μ ??Ό?΄ ??€λ©? λ°λ³΅ μ€λ¨.
					if (!uploadFile.exists()) {
						break;
					}

					// κ·Έλ μ§? ??€λ©? ??Ό?΄λ¦μ indexκ°μ ? ?©??¬ ?΄λ¦? λ³?κ²?
					fileName = System.currentTimeMillis() + (++index) + ext;
				} // end while

				// μ΅μ’? ?Όλ‘? κ΅¬μ±? ??Όκ°μ²΄λ₯? ?¬?©?΄?
				// ?? ?΄?? μ‘΄μ¬?? ??Ό? λ³΄κ??© ?΄?? λ³΅μ¬?κ³?, ????Ό ?­? 
				f.write(uploadFile);
				f.delete();

				/** 3) ??Ό ? λ³? λΆλ₯ μ²λ¦¬ */
				// ??±? ? λ³΄λ?? Beans? κ°μ²΄λ‘? ?€? ?΄? μ»¬λ ?? ???₯??€.
				// --> ?΄ ? λ³΄λ μΆν ??Ό? ?λ‘λ ?΄?­? DB? ???₯?  ? ?¬?©??€.
				FileInfo info = new FileInfo();
				info.setOrginName(orginName);
				info.setFileDir(fileDir);
				info.setFileName(fileName);
				info.setContentType(contentType);
				info.setFileSize(fileSize);

				fileList.add(info);
			} // end if
		} // end for
	}
	
	/**
	 * μ§?? ? κ²½λ‘? ??Ό? ?½?΄?€?Έ?€. κ·? ?΄?©? ??΅κ°μ²΄(response)λ₯? ?¬?©?΄? μΆλ ₯??€.  
	 * @param filePath - ?λ²μ? ??Ό κ²½λ‘
	 * @param orginName - ?λ³? ??Ό ?΄λ¦?
	 * @throws IOException
	 */
	public void printFileStream(String filePath, String orginName) throws IOException {
		/** JSP ?΄?₯κ°μ²΄λ₯? ?΄κ³? ?? Spring? κ°μ²΄λ₯? ?΅?΄? ?΄?₯κ°μ²΄ ???κΈ? */
		// --> import org.springframework.web.context.request.RequestContextHolder;
		// --> import org.springframework.web.context.request.ServletRequestAttributes;
		ServletRequestAttributes requestAttr 
				= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletResponse response = requestAttr.getResponse();
		
		/** ??Ό? μ‘΄μ¬?¬λΆ?λ₯? ??Έ?κ³? ??Ό? ? λ³? μΆμΆ?κΈ? */
		// --> import java.io.File;
		File f = new File(filePath);
		if (!f.exists()) {
			// --> import java.io.FileNotFoundException;
			throw new FileNotFoundException();
		}

		// ??Ό? ?¬κΈ? μΆμΆ?κΈ?
		long size = f.length();
		// ?λ²μ λ³΄κ???΄ ?? ??Ό? ?΄λ¦? μΆμΆ?κΈ?
		String name = f.getName();
		
		// ?λ³? ??Όλͺμ΄ ? ?¬?μ§? ??? κ²½μ° ?λ²μ? ??Ό?΄λ¦μΌλ‘? ??μ²?
		if (orginName == null) {
			orginName = name;
		}
		
		// ??Ό?? ?»κΈ? (?λ‘λ ? λ³΄μ? μΆμΆ?? contentTypeκ³? κ°μ? κ°?)
		// --> import javax.activation.MimetypesFileTypeMap;
		MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();
		String fileType = typeMap.getContentType(f);

		/** ?€?Έλ¦Όμ ?΅? ??Ό? λ°μ΄?λ¦? ?½κΈ? */
		// ??Ό ?½κΈ? ?€?Έλ¦Όμ ??±??€.
		// --> import java.io.InputStream;
		// --> import java.io.FileInputStream;
		InputStream is = new FileInputStream(f);

		// ??Ό? ?΄?©? ?΄κΈ? ?? byte λ°°μ΄
		byte b[] = new byte[(int) size];

		// ??Ό ?½κΈ?
		is.read(b);

		/** λΈλΌ?°???κ²? ?΄ λ©μ?λ₯? ?ΈμΆν? ??΄μ§?? ??? ?Όλ°? ??Όλ‘? ?Έ???€κΈ? ?? μ²λ¦¬ */
		// ?€λ₯? ?°?΄?°?? ??΄μ§? ??λ‘? ??΅κ°μ²΄(response)λ₯? λ¦¬μ??€.
		response.reset();

		// ??Ό?? ? λ³? ?€? 
		response.setHeader("Content-Type", fileType + "; charset=UTF-8");

		// ??Ό? ?΄λ¦? ?€?  (?Έμ½λ© ???¨)
		// --> import java.net.URLEncoder;
		String encFileName = URLEncoder.encode(orginName, "UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + encFileName + ";");

		// ??Ό? ?©? ?€? 
		response.setContentLength((int) size);

		/** ?½?΄?€?Έ ??Ό ?°?΄?° μΆλ ₯?κΈ? */
		// μΆλ ₯κ°μ²΄λ₯? ??±?΄? ??Ό? ?°?΄?°λ₯? ??¬ μ»¨νΈλ‘€λ¬? μΆλ ₯??€.
		// --> import java.io.OutputStream;
		OutputStream os = response.getOutputStream();
		os.write(b);
		os.flush();

		/** ?€?Έλ¦Όμ ?«??€. */
		is.close();
		os.close();
	}
	
	/**
	 * ?λ³Έν?Ό? κ²½λ‘?? ?¨κ»? ?΄λ―Έμ?? κ°?λ‘?,?Έλ‘? ?¬κΈ°κ? ? ?¬?  κ²½μ°,
	 * μ§?? ? ?¬κΈ°λ‘ ?Έ?€?Ό ?΄λ―Έμ?λ₯? ??±?κ³?, ??±? ?Έ?€?Ό? μΆλ ₯??¨?€.
	 * @param response	- ??΅κ°μ²΄
	 * @param filePath	- ?λ³? ?΄λ―Έμ? κ²½λ‘
	 * @param width		- κ°?λ‘? ?¬κΈ?
	 * @param height	- ?Έλ‘? ?¬κΈ?
	 * @throws IOException
	 */
	public void printFileStream(String filePath, 
			int width, int height, boolean crop) throws IOException {
		
		// ?Έ?€?Ό? ??±?κ³? κ²½λ‘λ₯? λ¦¬ν΄λ°λ?€.
		String thumbPath = this.createThumbnail(filePath, width, height, crop);
		
		// ?Έ?€?Ό? μΆλ ₯??€.
		// --> 	?΄ λ©μ?λ₯? ?ΈμΆνκΈ? ??΄? try~catchκ°? ?κ΅¬λμ§?λ§?,
		//	   	??¬ λ©μ? ?­? throwsλ₯? λͺμ?κΈ? ?λ¬Έμ
		//		??Έμ²λ¦¬κ°? ??¬ λ©μ?λ₯? ?ΈμΆν? κ³³μΌλ‘? ?΄κ΄???€.
		this.printFileStream(thumbPath, null);
	}
	
	/**
	 * λ¦¬μ¬?΄μ¦? ? ?Έ?€?Ό ?΄λ―Έμ?λ₯? ??±??€.
	 * @param loadFile	- ?λ³? ??Ό? κ²½λ‘
	 * @param width		- μ΅λ? ?΄λ―Έμ? κ°?λ‘? ?¬κΈ?
	 * @param height	- μ΅λ? ?΄λ―Έμ? ?Έλ‘? ?¬κΈ? 
	 * @param crop 		- ?΄λ―Έμ? ?¬λ‘? ?¬?© ?¬λΆ?
	 * @return ??±? ?΄λ―Έμ?? ? ?? κ²½λ‘
	 * @throws IOException
	 */
	public String createThumbnail(String loadFile, int width, int height, boolean crop) 
			throws IOException {
		// ??±? ?Έ?€?Ό ?΄λ―Έμ?? κ²½λ‘ 
		String saveFile = null;
		
		// ?λ³? ??Όλͺμ? ???₯?  ??Ό κ²½λ‘λ₯? ??±??€.
		File load = new File(loadFile);
		String dirPath = load.getParent();
		String fileName = load.getName();
		
		// ?λ³? ??Ό?΄λ¦μ? ?΄λ¦κ³Ό ??₯?λ₯? λΆλ¦¬??€.
		int p = fileName.lastIndexOf(".");
		String name = fileName.substring(0, p);
		String ext = fileName.substring(p+1);
		
		// ?λ³Έμ΄λ¦μ ?μ²?? ?¬?΄μ¦λ?? ?§λΆμ¬? ??±?  ??Όλͺμ κ΅¬μ±??€.
		// ex) myphoto.jpg --> myphoto_resize_320x240.jpg
		String prefix = "_resize_";
		if (crop) {
			prefix = "_crop_";
		}
		
		String thumbName = name + prefix + width + "x" + height + "." + ext;
		File f = new File(dirPath, thumbName);
		
		// ? ??κ²½λ‘ μΆμΆ
		saveFile = f.getAbsolutePath();
		
		// ?΄?Ή κ²½λ‘? ?΄λ―Έμ?κ°? ?? κ²½μ°λ§? ??
		if (!f.exists()) {
			// ?λ³? ?΄λ―Έμ? κ°?? Έ?€κΈ?
			// --> import net.coobird.thumbnailator.Thumbnails;
			// --> import net.coobird.thumbnailator.Thumbnails.Builder;
			Builder<File> builder = Thumbnails.of(loadFile);
			// ?΄λ―Έμ? ?¬λ‘? ?¬λΆ?
			if (crop == true) {
				builder.crop(Positions.CENTER);
			}
			// μΆμ?  ?¬?΄μ¦? μ§?? 
			builder.size(width, height);
			// ?Έλ‘λ‘ μ΄¬μ? ?¬μ§μ ?? ??΄
			builder.useExifOrientation(true);
			// ??Ό? ??₯λͺ? μ§?? 
			builder.outputFormat(ext);
			// ???₯?  ??Ό?΄λ¦? μ§?? 
			builder.toFile(saveFile);
		}
		
		return saveFile;
	}
	
	/**
	 * ? ?¬? κ²½λ‘? ??? ??Ό?΄ ?€? λ‘? μ‘΄μ¬?  κ²½μ°
	 * ?΄?Ή ??Όκ³? λΉμ·? ?΄λ¦μ κ°λ ?Έ?€?Ό? ?Όκ΄? ?­? ??€.
	 * @param filePath
	 */
	public void removeFile(String filePath) {
		
		/** 1) ??Ό? μ‘΄μ¬ ?¬λΆ? κ²??¬ */
		// κ²½λ‘κ°μ΄ μ‘΄μ¬?μ§? ???€λ©? μ²λ¦¬ μ€λ¨
		if (filePath == null) {
			return;
		}
		
		// μ£Όμ΄μ§? κ²½λ‘? ??? ??Ό κ°μ²΄ ??±
		File file = new File(filePath);
		
		// ?€? λ‘? μ‘΄μ¬??μ§? κ²??¬??€.
		if (!file.exists()) {
			return;
		}
		
		/** 2) κ²½λ‘?? ??Ό?΄λ¦?(??₯? ? ?Έ), ?΄?κ²½λ‘ μΆμΆ */
		// μ²¨λ???Ό? ?΄λ¦μ? ??₯?λ₯? ? ?Έ?κ³? μΆμΆ
		String fileName = file.getName();
		final String prefix = fileName.substring(0, fileName.lastIndexOf("."));

		// ??Ό?΄ ???₯??΄ ?? ?΄?? ??? κ°μ²΄ ??±
		// --> ?΄ ?? ??Ό λͺ©λ‘? ?€μΊν΄?Ό ??€.
		File dir = file.getParentFile();

		/** 3) ? ?΄μ§? ?΄? ??? λΉμ·? ??Ό?΄λ¦μ κ°λ λͺ¨λ  ??Ό? λͺ©λ‘? μΆμΆ */
		// ?΄?κ°μκ²? ??°λ§? κ·μΉ? ? ?©??¬ ?ΌμΉν? κ·μΉ? ??Ό? ?΄λ¦λ€? λ°°μ΄λ‘? λ°λ?€.
		String[] list = dir.list(new FilenameFilter() {
			// dirκ°μ²΄κ°? ?λ―Έν? ?΄? ?΄? λͺ¨λ  ??Ό? ?΄λ¦μ
			// ?΄ λ©μ??κ²? ? ?¬?κ²? ??€.
			// ?΄ λ©μ???? ? ?¬λ°μ? ?΄λ¦μ΄ ??? κ·μΉκ³? ?Έ???μ§?λ₯?
			// ?λ³ν?¬ true/falseλ₯? λ¦¬ν΄??€.
			@Override
			public boolean accept(File dir, String name) {
				// ??Ό?΄λ¦μ ?λ³Έν?Ό?΄λ¦μ΄ ?¬?¨??΄ ??€λ©? true
				// ex) ?λ³Έμ΄λ¦μ΄ helloworld?Ό κ²½μ° 
				//     helloworld_crop_40x40, helloworld_resize_120x80 ?±? ??Ό?΄λ¦μ΄ μΆμΆ??€.
				return (name.indexOf(prefix) > -1);
			}
		});

		/** 4) μ‘°ν? ??Ό λͺ©λ‘? ?μ°¨μ ?Όλ‘? ?­? ??€. */
		for (int j = 0; j < list.length; j++) {
			File f = new File(dir, list[j]);
			if (f.exists()) {
				f.delete();
			}
		}
	}
}
