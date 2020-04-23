package com.gaimit.helper;

/**
 * ?��로드 ?�� ?��?��?�� ?��보�?? ???��?���? ?��?�� JavaBeans
 * - ?�� ?��?��?��?�� 객체�? ?��로드 ?�� ?��?��?�� ?�� 만큼 ?��?��?��?�� List ?��?���? 보�??��?��.
 */
public class FileInfo {
	private String fieldName; 	// <input type="file">?�� name ?��?��
	private String orginName; 	// ?���? ?��?�� ?���?
	private String fileDir; 	// ?��?��?�� ???��?��?�� ?��?�� ?��버상?�� 경로
	private String fileName; 	// ?��버상?�� ?��?�� ?���?
	private String contentType; // ?��?��?�� ?��?��
	private long fileSize; 		// ?��?��?�� ?��?��

	// getter + setter + toString() 추�?
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getOrginName() {
		return orginName;
	}

	public void setOrginName(String orginName) {
		this.orginName = orginName;
	}

	public String getFileDir() {
		return fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	@Override
	public String toString() {
		return "FileInfo [fieldName=" + fieldName + ", orginName=" + orginName + ", fileDir=" + fileDir + ", fileName="
				+ fileName + ", contentType=" + contentType + ", fileSize=" + fileSize + "]";
	}

}