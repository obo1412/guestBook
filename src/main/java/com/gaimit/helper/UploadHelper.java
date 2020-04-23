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
	
	/** ?—…ë¡œë“œ ?œ ê²°ê³¼ë¬¼ì´ ???¥?  ?´?” */
	public String fileDir = null;
	/** ?—…ë¡œë“œê°? ì§„í–‰?  ?„?‹œ ?´?” */
	public String tempDir = null;
	
	public UploadHelper(String homeDir) {
		this.fileDir = homeDir + "/upload";
		this.tempDir = fileDir + "/temp";
	}

	/** File? •ë³´ë?? ???¥?•˜ê¸? ?œ„?•œ ì»¬ë ‰?…˜ */
	private List<FileInfo> fileList;

	/** ê·? ë°–ì˜ ?¼ë°? ?°?´?„°ë¥? ???¥?•˜ê¸? ?œ„?•œ ì»¬ë ‰?…˜ */
	private Map<String, String> paramMap;

	/** ?—…ë¡œë“œ?œ ?ŒŒ?¼?˜ ë¦¬ìŠ¤?Š¸ë¥? ë¦¬í„´?•œ?‹¤. */
	public List<FileInfo> getFileList() {
		return this.fileList;
	}

	/** ?—…ë¡œë“œ?‹œ?— ?•¨ê»? ? „?‹¬?œ ?ŒŒ?¼ë¯¸í„°?“¤?˜ ì»¬ë ‰?…˜?„ ë¦¬í„´?•œ?‹¤. */
	public Map<String, String> getParamMap() {
		return this.paramMap;
	}

	/**
	 * Multipartë¡? ? „?†¡?œ ?°?´?„°ë¥? ?Œë³„í•˜?—¬ ?ŒŒ?¼ë¦¬ìŠ¤?Š¸?? ?…?Š¤?Š¸ ?ŒŒ?¼ë¯¸í„°ë¥? ë¶„ë¥˜?•œ?‹¤.
	 * @throws Exception
	 */
	public void multipartRequest() throws Exception {
		/** JSP ?‚´?¥ê°ì²´ë¥? ?‹´ê³? ?ˆ?Š” Spring?˜ ê°ì²´ë¥? ?†µ?•´?„œ ?‚´?¥ê°ì²´ ?š?“?•˜ê¸? */
		// --> import org.springframework.web.context.request.RequestContextHolder;
		// --> import org.springframework.web.context.request.ServletRequestAttributes;
		ServletRequestAttributes requestAttr 
			= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttr.getRequest();
		
		/** multipartë¡? ? „?†¡?˜?—ˆ?Š”ì§? ?—¬ë¶? ê²??‚¬ */
		// --> import org.apache.commons.fileupload.servlet.ServletFileUpload
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		if (!isMultipart) {
			// ? „?†¡?œ ?°?´?„°ê°? ?—†?œ¼ë¯?ë¡? ê°•ì œ ?˜ˆ?™¸ ë°œìƒ.
			throw new Exception();
		}

		/** ?´?”?˜ ì¡´ì¬ ?—¬ë¶? ì²´í¬?•´?„œ ?ƒ?„±?•˜ê¸? */
		// import java.io.File
		File uploadDirFile = new File(fileDir);
		if (!uploadDirFile.exists()) {
			uploadDirFile.mkdirs();
		}

		File tempDirFile = new File(tempDir);
		if (!tempDirFile.exists()) {
			tempDirFile.mkdirs();
		}

		/** ?—…ë¡œë“œê°? ?ˆ˜?–‰?  ?„?‹œ ?´?” ?—°ê²? */
		// --> import org.apache.commons.fileupload.disk.DiskFileItemFactory
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(tempDirFile);

		/** ?—…ë¡œë“œ ?‹œ?‘ */
		ServletFileUpload upload = new ServletFileUpload(factory);
		// UTF-8 ì²˜ë¦¬ ì§?? •
		upload.setHeaderEncoding("UTF-8");
		// ìµœë? ?ŒŒ?¼ ?¬ê¸? --> 20M
		upload.setSizeMax(20 * 1024 * 1024);
		// ?‹¤? œ ?—…ë¡œë“œë¥? ?ˆ˜?–‰?•˜?—¬ ?ŒŒ?¼ ë°? ?ŒŒ?¼ë¯¸í„°?“¤?„ ?–»ê¸?
		List<FileItem> items = upload.parseRequest(request);

		// items?— ???¥ ?°?´?„°ê°? ë¶„ë¥˜?  ì»¬ë ‰?…˜?“¤ ?• ?‹¹?•˜ê¸?
		fileList = new ArrayList<FileInfo>();
		paramMap = new HashMap<String, String>();

		/** ?—…ë¡œë“œ ?œ ?ŒŒ?¼?˜ ? •ë³? ì²˜ë¦¬ */
		for (int i = 0; i < items.size(); i++) {
			// ? „?†¡?œ ? •ë³? ?•˜?‚˜ë¥? ì¶”ì¶œ?•œ?‹¤.
			// import org.apache.commons.fileupload.FileItem
			FileItem f = items.get(i);

			if (f.isFormField()) {
				/** ?ŒŒ?¼ ?˜•?‹?˜ ?°?´?„°ê°? ?•„?‹Œ ê²½ìš° --> paramMap?— ? •ë³? ë¶„ë¥˜ */
				String key = f.getFieldName();
				// valueë¥? UTF-8 ?˜•?‹?œ¼ë¡? ì·¨ë“?•œ?‹¤.
				String value = f.getString("UTF-8");

				// ?´ë¯? ?™?¼?•œ ?‚¤ê°’ì´ map?•ˆ?— ì¡´ì¬?•œ?‹¤ë©?? --> checkbox
				if (paramMap.containsKey(key)) {
					// ê¸°ì¡´?˜ ê°? ?’¤?— ì½¤ë§ˆ(,)ë¥? ì¶”ê??•´?„œ ê°’ì„ ë³‘í•©?•œ?‹¤.
					String new_value = paramMap.get(key) + "," + value;
					paramMap.put(key, new_value);
				} else {
					// ê·¸ë ‡ì§? ?•Š?‹¤ë©? ?‚¤?? ê°’ì„ ?‹ ê·œë¡œ ì¶”ê??•œ?‹¤.
					paramMap.put(key, value);
				}

			} else {
				/** ?ŒŒ?¼ ?˜•?‹?˜ ?°?´?„°?¸ ê²½ìš° --> fileList?— ? •ë³? ë¶„ë¥˜ */
				
				/** 1) ?ŒŒ?¼?˜ ? •ë³´ë?? ì¶”ì¶œ?•œ?‹¤ */
				String orginName = f.getName(); 		// ?ŒŒ?¼?˜ ?›ë³? ?´ë¦?
				String contentType = f.getContentType();// ?ŒŒ?¼ ?˜•?‹
				long fileSize = f.getSize(); 			// ?ŒŒ?¼ ?‚¬?´ì¦?

				// ?ŒŒ?¼ ?‚¬?´ì¦ˆê? ?—†?‹¤ë©? ì¡°ê±´?œ¼ë¡? ?Œ?•„ê°„ë‹¤.
				if (fileSize < 1) {
					continue;
				}

				// ?ŒŒ?¼?´ë¦„ì—?„œ ?™•?¥?ë§? ì¶”ì¶œ
				String ext = orginName.substring(orginName.lastIndexOf("."));

				/** 2) ?™?¼?•œ ?´ë¦„ì˜ ?ŒŒ?¼?´ ì¡´ì¬?•˜?Š”ì§? ê²??‚¬?•œ?‹¤. */
				// ?›¹ ?„œë²„ì— ???¥?  ?´ë¦„ì„ "?˜„?¬?˜ Timestamp+?™•?¥?(ext)"ë¡? ì§?? • (ì¤‘ë³µ???¥ ?š°? ¤)
				String fileName = System.currentTimeMillis() + ext;
				// ???¥?œ ?ŒŒ?¼ ? •ë³´ë?? ?‹´ê¸? ?œ„?•œ Fileê°ì²´
				File uploadFile = null;
				// ì¤‘ë³µ?œ ?´ë¦„ì˜ ?ŒŒ?¼?´ ì¡´ì¬?•  ê²½ìš° indexê°’ì„ 1?”© ì¦ê??•˜ë©´ì„œ ?’¤?— ?§ë¶™ì¸?‹¤.
				int index = 0;
				
				// ?¼?‹¨ ë¬´í•œë£¨í”„
				while (true) {
					// ?—…ë¡œë“œ ?ŒŒ?¼?´ ???¥?  ?´?” + ?ŒŒ?¼?´ë¦„ìœ¼ë¡? ?ŒŒ?¼ê°ì²´ë¥? ?ƒ?„±?•œ?‹¤.
					uploadFile = new File(uploadDirFile, fileName);

					// ?™?¼?•œ ?´ë¦„ì˜ ?ŒŒ?¼?´ ?—†?‹¤ë©? ë°˜ë³µ ì¤‘ë‹¨.
					if (!uploadFile.exists()) {
						break;
					}

					// ê·¸ë ‡ì§? ?•Š?‹¤ë©? ?ŒŒ?¼?´ë¦„ì— indexê°’ì„ ? ?š©?•˜?—¬ ?´ë¦? ë³?ê²?
					fileName = System.currentTimeMillis() + (++index) + ext;
				} // end while

				// ìµœì¢…? ?œ¼ë¡? êµ¬ì„±?œ ?ŒŒ?¼ê°ì²´ë¥? ?‚¬?š©?•´?„œ
				// ?„?‹œ ?´?”?— ì¡´ì¬?•˜?Š” ?ŒŒ?¼?„ ë³´ê??š© ?´?”?— ë³µì‚¬?•˜ê³?, ?„?‹œ?ŒŒ?¼ ?‚­? œ
				f.write(uploadFile);
				f.delete();

				/** 3) ?ŒŒ?¼ ? •ë³? ë¶„ë¥˜ ì²˜ë¦¬ */
				// ?ƒ?„±?œ ? •ë³´ë?? Beans?˜ ê°ì²´ë¡? ?„¤? •?•´?„œ ì»¬ë ‰?…˜?— ???¥?•œ?‹¤.
				// --> ?´ ? •ë³´ëŠ” ì¶”í›„ ?ŒŒ?¼?˜ ?—…ë¡œë“œ ?‚´?—­?„ DB?— ???¥?•  ?•Œ ?‚¬?š©?œ?‹¤.
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
	 * ì§?? •?œ ê²½ë¡œ?˜ ?ŒŒ?¼?„ ?½?–´?“¤?¸?‹¤. ê·? ?‚´?š©?„ ?‘?‹µê°ì²´(response)ë¥? ?‚¬?š©?•´?„œ ì¶œë ¥?•œ?‹¤.  
	 * @param filePath - ?„œë²„ìƒ?˜ ?ŒŒ?¼ ê²½ë¡œ
	 * @param orginName - ?›ë³? ?ŒŒ?¼ ?´ë¦?
	 * @throws IOException
	 */
	public void printFileStream(String filePath, String orginName) throws IOException {
		/** JSP ?‚´?¥ê°ì²´ë¥? ?‹´ê³? ?ˆ?Š” Spring?˜ ê°ì²´ë¥? ?†µ?•´?„œ ?‚´?¥ê°ì²´ ?š?“?•˜ê¸? */
		// --> import org.springframework.web.context.request.RequestContextHolder;
		// --> import org.springframework.web.context.request.ServletRequestAttributes;
		ServletRequestAttributes requestAttr 
				= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletResponse response = requestAttr.getResponse();
		
		/** ?ŒŒ?¼?˜ ì¡´ì¬?—¬ë¶?ë¥? ?™•?¸?•˜ê³? ?ŒŒ?¼?˜ ? •ë³? ì¶”ì¶œ?•˜ê¸? */
		// --> import java.io.File;
		File f = new File(filePath);
		if (!f.exists()) {
			// --> import java.io.FileNotFoundException;
			throw new FileNotFoundException();
		}

		// ?ŒŒ?¼?˜ ?¬ê¸? ì¶”ì¶œ?•˜ê¸?
		long size = f.length();
		// ?„œë²„ì— ë³´ê??˜?–´ ?ˆ?Š” ?ŒŒ?¼?˜ ?´ë¦? ì¶”ì¶œ?•˜ê¸?
		String name = f.getName();
		
		// ?›ë³? ?ŒŒ?¼ëª…ì´ ? „?‹¬?˜ì§? ?•Š?? ê²½ìš° ?„œë²„ìƒ?˜ ?ŒŒ?¼?´ë¦„ìœ¼ë¡? ??ì²?
		if (orginName == null) {
			orginName = name;
		}
		
		// ?ŒŒ?¼?˜•?‹ ?–»ê¸? (?—…ë¡œë“œ ? •ë³´ì—?„œ ì¶”ì¶œ?–ˆ?˜ contentTypeê³? ê°™ì? ê°?)
		// --> import javax.activation.MimetypesFileTypeMap;
		MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();
		String fileType = typeMap.getContentType(f);

		/** ?Š¤?Š¸ë¦¼ì„ ?†µ?•œ ?ŒŒ?¼?˜ ë°”ì´?„ˆë¦? ?½ê¸? */
		// ?ŒŒ?¼ ?½ê¸? ?Š¤?Š¸ë¦¼ì„ ?ƒ?„±?•œ?‹¤.
		// --> import java.io.InputStream;
		// --> import java.io.FileInputStream;
		InputStream is = new FileInputStream(f);

		// ?ŒŒ?¼?˜ ?‚´?š©?„ ?‹´ê¸? ?œ„?•œ byte ë°°ì—´
		byte b[] = new byte[(int) size];

		// ?ŒŒ?¼ ?½ê¸?
		is.read(b);

		/** ë¸Œë¼?š°???—ê²? ?´ ë©”ì„œ?“œë¥? ?˜¸ì¶œí•˜?Š” ?˜?´ì§??˜ ?˜•?‹?„ ?¼ë°? ?ŒŒ?¼ë¡? ?¸?‹?‹œ?‚¤ê¸? ?œ„?•œ ì²˜ë¦¬ */
		// ?‹¤ë¥? ?°?´?„°?? ?„?´ì§? ?•Š?„ë¡? ?‘?‹µê°ì²´(response)ë¥? ë¦¬ì…‹?•œ?‹¤.
		response.reset();

		// ?ŒŒ?¼?˜•?‹ ? •ë³? ?„¤? •
		response.setHeader("Content-Type", fileType + "; charset=UTF-8");

		// ?ŒŒ?¼?˜ ?´ë¦? ?„¤? • (?¸ì½”ë”© ?•„?š”?•¨)
		// --> import java.net.URLEncoder;
		String encFileName = URLEncoder.encode(orginName, "UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + encFileName + ";");

		// ?ŒŒ?¼?˜ ?š©?Ÿ‰ ?„¤? •
		response.setContentLength((int) size);

		/** ?½?–´?“¤?¸ ?ŒŒ?¼ ?°?´?„° ì¶œë ¥?•˜ê¸? */
		// ì¶œë ¥ê°ì²´ë¥? ?ƒ?„±?•´?„œ ?ŒŒ?¼?˜ ?°?´?„°ë¥? ?˜„?¬ ì»¨íŠ¸ë¡¤ëŸ¬?— ì¶œë ¥?•œ?‹¤.
		// --> import java.io.OutputStream;
		OutputStream os = response.getOutputStream();
		os.write(b);
		os.flush();

		/** ?Š¤?Š¸ë¦¼ì„ ?‹«?Š”?‹¤. */
		is.close();
		os.close();
	}
	
	/**
	 * ?›ë³¸íŒŒ?¼?˜ ê²½ë¡œ?? ?•¨ê»? ?´ë¯¸ì??˜ ê°?ë¡?,?„¸ë¡? ?¬ê¸°ê? ? „?‹¬?  ê²½ìš°,
	 * ì§?? •?œ ?¬ê¸°ë¡œ ?¸?„¤?¼ ?´ë¯¸ì?ë¥? ?ƒ?„±?•˜ê³?, ?ƒ?„±?œ ?¸?„¤?¼?„ ì¶œë ¥?‹œ?‚¨?‹¤.
	 * @param response	- ?‘?‹µê°ì²´
	 * @param filePath	- ?›ë³? ?´ë¯¸ì? ê²½ë¡œ
	 * @param width		- ê°?ë¡? ?¬ê¸?
	 * @param height	- ?„¸ë¡? ?¬ê¸?
	 * @throws IOException
	 */
	public void printFileStream(String filePath, 
			int width, int height, boolean crop) throws IOException {
		
		// ?¸?„¤?¼?„ ?ƒ?„±?•˜ê³? ê²½ë¡œë¥? ë¦¬í„´ë°›ëŠ”?‹¤.
		String thumbPath = this.createThumbnail(filePath, width, height, crop);
		
		// ?¸?„¤?¼?„ ì¶œë ¥?•œ?‹¤.
		// --> 	?´ ë©”ì„œ?“œë¥? ?˜¸ì¶œí•˜ê¸? ?œ„?•´?„œ try~catchê°? ?š”êµ¬ë˜ì§?ë§?,
		//	   	?˜„?¬ ë©”ì„œ?“œ ?—­?‹œ throwsë¥? ëª…ì‹œ?–ˆê¸? ?•Œë¬¸ì—
		//		?˜ˆ?™¸ì²˜ë¦¬ê°? ?˜„?¬ ë©”ì„œ?“œë¥? ?˜¸ì¶œí•˜?Š” ê³³ìœ¼ë¡? ?´ê´??œ?‹¤.
		this.printFileStream(thumbPath, null);
	}
	
	/**
	 * ë¦¬ì‚¬?´ì¦? ?œ ?¸?„¤?¼ ?´ë¯¸ì?ë¥? ?ƒ?„±?•œ?‹¤.
	 * @param loadFile	- ?›ë³? ?ŒŒ?¼?˜ ê²½ë¡œ
	 * @param width		- ìµœë? ?´ë¯¸ì? ê°?ë¡? ?¬ê¸?
	 * @param height	- ìµœë? ?´ë¯¸ì? ?„¸ë¡? ?¬ê¸? 
	 * @param crop 		- ?´ë¯¸ì? ?¬ë¡? ?‚¬?š© ?—¬ë¶?
	 * @return ?ƒ?„±?œ ?´ë¯¸ì??˜ ? ˆ?? ê²½ë¡œ
	 * @throws IOException
	 */
	public String createThumbnail(String loadFile, int width, int height, boolean crop) 
			throws IOException {
		// ?ƒ?„±?œ ?¸?„¤?¼ ?´ë¯¸ì??˜ ê²½ë¡œ 
		String saveFile = null;
		
		// ?›ë³? ?ŒŒ?¼ëª…ì—?„œ ???¥?  ?ŒŒ?¼ ê²½ë¡œë¥? ?ƒ?„±?•œ?‹¤.
		File load = new File(loadFile);
		String dirPath = load.getParent();
		String fileName = load.getName();
		
		// ?›ë³? ?ŒŒ?¼?´ë¦„ì—?„œ ?´ë¦„ê³¼ ?™•?¥?ë¥? ë¶„ë¦¬?•œ?‹¤.
		int p = fileName.lastIndexOf(".");
		String name = fileName.substring(0, p);
		String ext = fileName.substring(p+1);
		
		// ?›ë³¸ì´ë¦„ì— ?š”ì²??œ ?‚¬?´ì¦ˆë?? ?§ë¶™ì—¬?„œ ?ƒ?„±?  ?ŒŒ?¼ëª…ì„ êµ¬ì„±?•œ?‹¤.
		// ex) myphoto.jpg --> myphoto_resize_320x240.jpg
		String prefix = "_resize_";
		if (crop) {
			prefix = "_crop_";
		}
		
		String thumbName = name + prefix + width + "x" + height + "." + ext;
		File f = new File(dirPath, thumbName);
		
		// ? ˆ??ê²½ë¡œ ì¶”ì¶œ
		saveFile = f.getAbsolutePath();
		
		// ?•´?‹¹ ê²½ë¡œ?— ?´ë¯¸ì?ê°? ?—†?Š” ê²½ìš°ë§? ?ˆ˜?–‰
		if (!f.exists()) {
			// ?›ë³? ?´ë¯¸ì? ê°?? ¸?˜¤ê¸?
			// --> import net.coobird.thumbnailator.Thumbnails;
			// --> import net.coobird.thumbnailator.Thumbnails.Builder;
			Builder<File> builder = Thumbnails.of(loadFile);
			// ?´ë¯¸ì? ?¬ë¡? ?—¬ë¶?
			if (crop == true) {
				builder.crop(Positions.CENTER);
			}
			// ì¶•ì†Œ?•  ?‚¬?´ì¦? ì§?? •
			builder.size(width, height);
			// ?„¸ë¡œë¡œ ì´¬ì˜?œ ?‚¬ì§„ì„ ?šŒ? „?‹œ?‚´
			builder.useExifOrientation(true);
			// ?ŒŒ?¼?˜ ?™•?¥ëª? ì§?? •
			builder.outputFormat(ext);
			// ???¥?•  ?ŒŒ?¼?´ë¦? ì§?? •
			builder.toFile(saveFile);
		}
		
		return saveFile;
	}
	
	/**
	 * ? „?‹¬?œ ê²½ë¡œ?— ???•œ ?ŒŒ?¼?´ ?‹¤? œë¡? ì¡´ì¬?•  ê²½ìš°
	 * ?•´?‹¹ ?ŒŒ?¼ê³? ë¹„ìŠ·?•œ ?´ë¦„ì„ ê°–ëŠ” ?¸?„¤?¼?„ ?¼ê´? ?‚­? œ?•œ?‹¤.
	 * @param filePath
	 */
	public void removeFile(String filePath) {
		
		/** 1) ?ŒŒ?¼?˜ ì¡´ì¬ ?—¬ë¶? ê²??‚¬ */
		// ê²½ë¡œê°’ì´ ì¡´ì¬?•˜ì§? ?•Š?Š”?‹¤ë©? ì²˜ë¦¬ ì¤‘ë‹¨
		if (filePath == null) {
			return;
		}
		
		// ì£¼ì–´ì§? ê²½ë¡œ?— ???•œ ?ŒŒ?¼ ê°ì²´ ?ƒ?„±
		File file = new File(filePath);
		
		// ?‹¤? œë¡? ì¡´ì¬?•˜?Š”ì§? ê²??‚¬?•œ?‹¤.
		if (!file.exists()) {
			return;
		}
		
		/** 2) ê²½ë¡œ?—?„œ ?ŒŒ?¼?´ë¦?(?™•?¥? ? œ?™¸), ?´?”ê²½ë¡œ ì¶”ì¶œ */
		// ì²¨ë??ŒŒ?¼?˜ ?´ë¦„ì—?„œ ?™•?¥?ë¥? ? œ?™¸?•˜ê³? ì¶”ì¶œ
		String fileName = file.getName();
		final String prefix = fileName.substring(0, fileName.lastIndexOf("."));

		// ?ŒŒ?¼?´ ???¥?˜?–´ ?ˆ?Š” ?´?”?— ???•œ ê°ì²´ ?ƒ?„±
		// --> ?´ ?•ˆ?˜ ?ŒŒ?¼ ëª©ë¡?„ ?Š¤ìº”í•´?•¼ ?•œ?‹¤.
		File dir = file.getParentFile();

		/** 3) ? •?•´ì§? ?´?” ?•ˆ?—?„œ ë¹„ìŠ·?•œ ?ŒŒ?¼?´ë¦„ì„ ê°–ëŠ” ëª¨ë“  ?ŒŒ?¼?˜ ëª©ë¡?„ ì¶”ì¶œ */
		// ?´?”ê°ì—ê²? ?•„?„°ë§? ê·œì¹™?„ ? ?š©?•˜?—¬ ?¼ì¹˜í•˜?Š” ê·œì¹™?˜ ?ŒŒ?¼?˜ ?´ë¦„ë“¤?„ ë°°ì—´ë¡? ë°›ëŠ”?‹¤.
		String[] list = dir.list(new FilenameFilter() {
			// dirê°ì²´ê°? ?˜ë¯¸í•˜?Š” ?´?” ?‚´?˜ ëª¨ë“  ?ŒŒ?¼?˜ ?´ë¦„ì„
			// ?´ ë©”ì„œ?“œ?—ê²? ? „?‹¬?•˜ê²? ?œ?‹¤.
			// ?´ ë©”ì„œ?“œ?—?„œ?Š” ? „?‹¬ë°›ì? ?´ë¦„ì´ ?›?•˜?Š” ê·œì¹™ê³? ?˜¸?™˜?˜?Š”ì§?ë¥?
			// ?Œë³„í•˜?—¬ true/falseë¥? ë¦¬í„´?•œ?‹¤.
			@Override
			public boolean accept(File dir, String name) {
				// ?ŒŒ?¼?´ë¦„ì— ?›ë³¸íŒŒ?¼?´ë¦„ì´ ?¬?•¨?˜?–´ ?ˆ?‹¤ë©? true
				// ex) ?›ë³¸ì´ë¦„ì´ helloworld?¼ ê²½ìš° 
				//     helloworld_crop_40x40, helloworld_resize_120x80 ?“±?˜ ?ŒŒ?¼?´ë¦„ì´ ì¶”ì¶œ?œ?‹¤.
				return (name.indexOf(prefix) > -1);
			}
		});

		/** 4) ì¡°íšŒ?œ ?ŒŒ?¼ ëª©ë¡?„ ?ˆœì°¨ì ?œ¼ë¡? ?‚­? œ?•œ?‹¤. */
		for (int j = 0; j < list.length; j++) {
			File f = new File(dir, list[j]);
			if (f.exists()) {
				f.delete();
			}
		}
	}
}
