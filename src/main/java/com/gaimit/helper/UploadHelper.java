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
	
	/** ?��로드 ?�� 결과물이 ???��?�� ?��?�� */
	public String fileDir = null;
	/** ?��로드�? 진행?�� ?��?�� ?��?�� */
	public String tempDir = null;
	
	public UploadHelper(String homeDir) {
		this.fileDir = homeDir + "/upload";
		this.tempDir = fileDir + "/temp";
	}

	/** File?��보�?? ???��?���? ?��?�� 컬렉?�� */
	private List<FileInfo> fileList;

	/** �? 밖의 ?���? ?��?��?���? ???��?���? ?��?�� 컬렉?�� */
	private Map<String, String> paramMap;

	/** ?��로드?�� ?��?��?�� 리스?���? 리턴?��?��. */
	public List<FileInfo> getFileList() {
		return this.fileList;
	}

	/** ?��로드?��?�� ?���? ?��?��?�� ?��?��미터?��?�� 컬렉?��?�� 리턴?��?��. */
	public Map<String, String> getParamMap() {
		return this.paramMap;
	}

	/**
	 * Multipart�? ?��?��?�� ?��?��?���? ?��별하?�� ?��?��리스?��?? ?��?��?�� ?��?��미터�? 분류?��?��.
	 * @throws Exception
	 */
	public void multipartRequest() throws Exception {
		/** JSP ?��?��객체�? ?���? ?��?�� Spring?�� 객체�? ?��?��?�� ?��?��객체 ?��?��?���? */
		// --> import org.springframework.web.context.request.RequestContextHolder;
		// --> import org.springframework.web.context.request.ServletRequestAttributes;
		ServletRequestAttributes requestAttr 
			= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttr.getRequest();
		
		/** multipart�? ?��?��?��?��?���? ?���? �??�� */
		// --> import org.apache.commons.fileupload.servlet.ServletFileUpload
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		if (!isMultipart) {
			// ?��?��?�� ?��?��?���? ?��?���?�? 강제 ?��?�� 발생.
			throw new Exception();
		}

		/** ?��?��?�� 존재 ?���? 체크?��?�� ?��?��?���? */
		// import java.io.File
		File uploadDirFile = new File(fileDir);
		if (!uploadDirFile.exists()) {
			uploadDirFile.mkdirs();
		}

		File tempDirFile = new File(tempDir);
		if (!tempDirFile.exists()) {
			tempDirFile.mkdirs();
		}

		/** ?��로드�? ?��?��?�� ?��?�� ?��?�� ?���? */
		// --> import org.apache.commons.fileupload.disk.DiskFileItemFactory
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(tempDirFile);

		/** ?��로드 ?��?�� */
		ServletFileUpload upload = new ServletFileUpload(factory);
		// UTF-8 처리 �??��
		upload.setHeaderEncoding("UTF-8");
		// 최�? ?��?�� ?���? --> 20M
		upload.setSizeMax(20 * 1024 * 1024);
		// ?��?�� ?��로드�? ?��?��?��?�� ?��?�� �? ?��?��미터?��?�� ?���?
		List<FileItem> items = upload.parseRequest(request);

		// items?�� ???�� ?��?��?���? 분류?�� 컬렉?��?�� ?��?��?���?
		fileList = new ArrayList<FileInfo>();
		paramMap = new HashMap<String, String>();

		/** ?��로드 ?�� ?��?��?�� ?���? 처리 */
		for (int i = 0; i < items.size(); i++) {
			// ?��?��?�� ?���? ?��?���? 추출?��?��.
			// import org.apache.commons.fileupload.FileItem
			FileItem f = items.get(i);

			if (f.isFormField()) {
				/** ?��?�� ?��?��?�� ?��?��?���? ?��?�� 경우 --> paramMap?�� ?���? 분류 */
				String key = f.getFieldName();
				// value�? UTF-8 ?��?��?���? 취득?��?��.
				String value = f.getString("UTF-8");

				// ?���? ?��?��?�� ?��값이 map?��?�� 존재?��?���?? --> checkbox
				if (paramMap.containsKey(key)) {
					// 기존?�� �? ?��?�� 콤마(,)�? 추�??��?�� 값을 병합?��?��.
					String new_value = paramMap.get(key) + "," + value;
					paramMap.put(key, new_value);
				} else {
					// 그렇�? ?��?���? ?��?? 값을 ?��규로 추�??��?��.
					paramMap.put(key, value);
				}

			} else {
				/** ?��?�� ?��?��?�� ?��?��?��?�� 경우 --> fileList?�� ?���? 분류 */
				
				/** 1) ?��?��?�� ?��보�?? 추출?��?�� */
				String orginName = f.getName(); 		// ?��?��?�� ?���? ?���?
				String contentType = f.getContentType();// ?��?�� ?��?��
				long fileSize = f.getSize(); 			// ?��?�� ?��?���?

				// ?��?�� ?��?��즈�? ?��?���? 조건?���? ?��?��간다.
				if (fileSize < 1) {
					continue;
				}

				// ?��?��?��름에?�� ?��?��?���? 추출
				String ext = orginName.substring(orginName.lastIndexOf("."));

				/** 2) ?��?��?�� ?��름의 ?��?��?�� 존재?��?���? �??��?��?��. */
				// ?�� ?��버에 ???��?�� ?��름을 "?��?��?�� Timestamp+?��?��?��(ext)"�? �??�� (중복???�� ?��?��)
				String fileName = System.currentTimeMillis() + ext;
				// ???��?�� ?��?�� ?��보�?? ?���? ?��?�� File객체
				File uploadFile = null;
				// 중복?�� ?��름의 ?��?��?�� 존재?�� 경우 index값을 1?�� 증�??��면서 ?��?�� ?��붙인?��.
				int index = 0;
				
				// ?��?�� 무한루프
				while (true) {
					// ?��로드 ?��?��?�� ???��?�� ?��?�� + ?��?��?��름으�? ?��?��객체�? ?��?��?��?��.
					uploadFile = new File(uploadDirFile, fileName);

					// ?��?��?�� ?��름의 ?��?��?�� ?��?���? 반복 중단.
					if (!uploadFile.exists()) {
						break;
					}

					// 그렇�? ?��?���? ?��?��?��름에 index값을 ?��?��?��?�� ?���? �?�?
					fileName = System.currentTimeMillis() + (++index) + ext;
				} // end while

				// 최종?��?���? 구성?�� ?��?��객체�? ?��?��?��?��
				// ?��?�� ?��?��?�� 존재?��?�� ?��?��?�� 보�??�� ?��?��?�� 복사?���?, ?��?��?��?�� ?��?��
				f.write(uploadFile);
				f.delete();

				/** 3) ?��?�� ?���? 분류 처리 */
				// ?��?��?�� ?��보�?? Beans?�� 객체�? ?��?��?��?�� 컬렉?��?�� ???��?��?��.
				// --> ?�� ?��보는 추후 ?��?��?�� ?��로드 ?��?��?�� DB?�� ???��?�� ?�� ?��?��?��?��.
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
	 * �??��?�� 경로?�� ?��?��?�� ?��?��?��?��?��. �? ?��?��?�� ?��?��객체(response)�? ?��?��?��?�� 출력?��?��.  
	 * @param filePath - ?��버상?�� ?��?�� 경로
	 * @param orginName - ?���? ?��?�� ?���?
	 * @throws IOException
	 */
	public void printFileStream(String filePath, String orginName) throws IOException {
		/** JSP ?��?��객체�? ?���? ?��?�� Spring?�� 객체�? ?��?��?�� ?��?��객체 ?��?��?���? */
		// --> import org.springframework.web.context.request.RequestContextHolder;
		// --> import org.springframework.web.context.request.ServletRequestAttributes;
		ServletRequestAttributes requestAttr 
				= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletResponse response = requestAttr.getResponse();
		
		/** ?��?��?�� 존재?���?�? ?��?��?���? ?��?��?�� ?���? 추출?���? */
		// --> import java.io.File;
		File f = new File(filePath);
		if (!f.exists()) {
			// --> import java.io.FileNotFoundException;
			throw new FileNotFoundException();
		}

		// ?��?��?�� ?���? 추출?���?
		long size = f.length();
		// ?��버에 보�??��?�� ?��?�� ?��?��?�� ?���? 추출?���?
		String name = f.getName();
		
		// ?���? ?��?��명이 ?��?��?���? ?��?? 경우 ?��버상?�� ?��?��?��름으�? ??�?
		if (orginName == null) {
			orginName = name;
		}
		
		// ?��?��?��?�� ?���? (?��로드 ?��보에?�� 추출?��?�� contentType�? 같�? �?)
		// --> import javax.activation.MimetypesFileTypeMap;
		MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();
		String fileType = typeMap.getContentType(f);

		/** ?��?��림을 ?��?�� ?��?��?�� 바이?���? ?���? */
		// ?��?�� ?���? ?��?��림을 ?��?��?��?��.
		// --> import java.io.InputStream;
		// --> import java.io.FileInputStream;
		InputStream is = new FileInputStream(f);

		// ?��?��?�� ?��?��?�� ?���? ?��?�� byte 배열
		byte b[] = new byte[(int) size];

		// ?��?�� ?���?
		is.read(b);

		/** 브라?��???���? ?�� 메서?���? ?��출하?�� ?��?���??�� ?��?��?�� ?���? ?��?���? ?��?��?��?���? ?��?�� 처리 */
		// ?���? ?��?��?��?? ?��?���? ?��?���? ?��?��객체(response)�? 리셋?��?��.
		response.reset();

		// ?��?��?��?�� ?���? ?��?��
		response.setHeader("Content-Type", fileType + "; charset=UTF-8");

		// ?��?��?�� ?���? ?��?�� (?��코딩 ?��?��?��)
		// --> import java.net.URLEncoder;
		String encFileName = URLEncoder.encode(orginName, "UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + encFileName + ";");

		// ?��?��?�� ?��?�� ?��?��
		response.setContentLength((int) size);

		/** ?��?��?��?�� ?��?�� ?��?��?�� 출력?���? */
		// 출력객체�? ?��?��?��?�� ?��?��?�� ?��?��?���? ?��?�� 컨트롤러?�� 출력?��?��.
		// --> import java.io.OutputStream;
		OutputStream os = response.getOutputStream();
		os.write(b);
		os.flush();

		/** ?��?��림을 ?��?��?��. */
		is.close();
		os.close();
	}
	
	/**
	 * ?��본파?��?�� 경로?? ?���? ?��미�??�� �?�?,?���? ?��기�? ?��?��?�� 경우,
	 * �??��?�� ?��기로 ?��?��?�� ?��미�?�? ?��?��?���?, ?��?��?�� ?��?��?��?�� 출력?��?��?��.
	 * @param response	- ?��?��객체
	 * @param filePath	- ?���? ?��미�? 경로
	 * @param width		- �?�? ?���?
	 * @param height	- ?���? ?���?
	 * @throws IOException
	 */
	public void printFileStream(String filePath, 
			int width, int height, boolean crop) throws IOException {
		
		// ?��?��?��?�� ?��?��?���? 경로�? 리턴받는?��.
		String thumbPath = this.createThumbnail(filePath, width, height, crop);
		
		// ?��?��?��?�� 출력?��?��.
		// --> 	?�� 메서?���? ?��출하�? ?��?��?�� try~catch�? ?��구되�?�?,
		//	   	?��?�� 메서?�� ?��?�� throws�? 명시?���? ?��문에
		//		?��?��처리�? ?��?�� 메서?���? ?��출하?�� 곳으�? ?���??��?��.
		this.printFileStream(thumbPath, null);
	}
	
	/**
	 * 리사?���? ?�� ?��?��?�� ?��미�?�? ?��?��?��?��.
	 * @param loadFile	- ?���? ?��?��?�� 경로
	 * @param width		- 최�? ?��미�? �?�? ?���?
	 * @param height	- 최�? ?��미�? ?���? ?���? 
	 * @param crop 		- ?��미�? ?���? ?��?�� ?���?
	 * @return ?��?��?�� ?��미�??�� ?��?? 경로
	 * @throws IOException
	 */
	public String createThumbnail(String loadFile, int width, int height, boolean crop) 
			throws IOException {
		// ?��?��?�� ?��?��?�� ?��미�??�� 경로 
		String saveFile = null;
		
		// ?���? ?��?��명에?�� ???��?�� ?��?�� 경로�? ?��?��?��?��.
		File load = new File(loadFile);
		String dirPath = load.getParent();
		String fileName = load.getName();
		
		// ?���? ?��?��?��름에?�� ?��름과 ?��?��?���? 분리?��?��.
		int p = fileName.lastIndexOf(".");
		String name = fileName.substring(0, p);
		String ext = fileName.substring(p+1);
		
		// ?��본이름에 ?���??�� ?��?��즈�?? ?��붙여?�� ?��?��?�� ?��?��명을 구성?��?��.
		// ex) myphoto.jpg --> myphoto_resize_320x240.jpg
		String prefix = "_resize_";
		if (crop) {
			prefix = "_crop_";
		}
		
		String thumbName = name + prefix + width + "x" + height + "." + ext;
		File f = new File(dirPath, thumbName);
		
		// ?��??경로 추출
		saveFile = f.getAbsolutePath();
		
		// ?��?�� 경로?�� ?��미�?�? ?��?�� 경우�? ?��?��
		if (!f.exists()) {
			// ?���? ?��미�? �??��?���?
			// --> import net.coobird.thumbnailator.Thumbnails;
			// --> import net.coobird.thumbnailator.Thumbnails.Builder;
			Builder<File> builder = Thumbnails.of(loadFile);
			// ?��미�? ?���? ?���?
			if (crop == true) {
				builder.crop(Positions.CENTER);
			}
			// 축소?�� ?��?���? �??��
			builder.size(width, height);
			// ?��로로 촬영?�� ?��진을 ?��?��?��?��
			builder.useExifOrientation(true);
			// ?��?��?�� ?��?���? �??��
			builder.outputFormat(ext);
			// ???��?�� ?��?��?���? �??��
			builder.toFile(saveFile);
		}
		
		return saveFile;
	}
	
	/**
	 * ?��?��?�� 경로?�� ???�� ?��?��?�� ?��?���? 존재?�� 경우
	 * ?��?�� ?��?���? 비슷?�� ?��름을 갖는 ?��?��?��?�� ?���? ?��?��?��?��.
	 * @param filePath
	 */
	public void removeFile(String filePath) {
		
		/** 1) ?��?��?�� 존재 ?���? �??�� */
		// 경로값이 존재?���? ?��?��?���? 처리 중단
		if (filePath == null) {
			return;
		}
		
		// 주어�? 경로?�� ???�� ?��?�� 객체 ?��?��
		File file = new File(filePath);
		
		// ?��?���? 존재?��?���? �??��?��?��.
		if (!file.exists()) {
			return;
		}
		
		/** 2) 경로?��?�� ?��?��?���?(?��?��?�� ?��?��), ?��?��경로 추출 */
		// 첨�??��?��?�� ?��름에?�� ?��?��?���? ?��?��?���? 추출
		String fileName = file.getName();
		final String prefix = fileName.substring(0, fileName.lastIndexOf("."));

		// ?��?��?�� ???��?��?�� ?��?�� ?��?��?�� ???�� 객체 ?��?��
		// --> ?�� ?��?�� ?��?�� 목록?�� ?��캔해?�� ?��?��.
		File dir = file.getParentFile();

		/** 3) ?��?���? ?��?�� ?��?��?�� 비슷?�� ?��?��?��름을 갖는 모든 ?��?��?�� 목록?�� 추출 */
		// ?��?��객에�? ?��?���? 규칙?�� ?��?��?��?�� ?��치하?�� 규칙?�� ?��?��?�� ?��름들?�� 배열�? 받는?��.
		String[] list = dir.list(new FilenameFilter() {
			// dir객체�? ?��미하?�� ?��?�� ?��?�� 모든 ?��?��?�� ?��름을
			// ?�� 메서?��?���? ?��?��?���? ?��?��.
			// ?�� 메서?��?��?��?�� ?��?��받�? ?��름이 ?��?��?�� 규칙�? ?��?��?��?���?�?
			// ?��별하?�� true/false�? 리턴?��?��.
			@Override
			public boolean accept(File dir, String name) {
				// ?��?��?��름에 ?��본파?��?��름이 ?��?��?��?�� ?��?���? true
				// ex) ?��본이름이 helloworld?�� 경우 
				//     helloworld_crop_40x40, helloworld_resize_120x80 ?��?�� ?��?��?��름이 추출?��?��.
				return (name.indexOf(prefix) > -1);
			}
		});

		/** 4) 조회?�� ?��?�� 목록?�� ?��차적?���? ?��?��?��?��. */
		for (int j = 0; j < list.length; j++) {
			File f = new File(dir, list[j]);
			if (f.exists()) {
				f.delete();
			}
		}
	}
}
