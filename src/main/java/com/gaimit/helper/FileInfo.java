package com.gaimit.helper;

/**
 * ?—…ë¡œë“œ ?œ ?ŒŒ?¼?˜ ? •ë³´ë?? ???¥?•˜ê¸? ?œ„?•œ JavaBeans
 * - ?´ ?´?˜?Š¤?˜ ê°ì²´ê°? ?—…ë¡œë“œ ?œ ?ŒŒ?¼?˜ ?ˆ˜ ë§Œí¼ ?ƒ?„±?˜?–´ List ?˜•?ƒœë¡? ë³´ê??œ?‹¤.
 */
public class FileInfo {
	private String fieldName; 	// <input type="file">?˜ name ?†?„±
	private String orginName; 	// ?›ë³? ?ŒŒ?¼ ?´ë¦?
	private String fileDir; 	// ?ŒŒ?¼?´ ???¥?˜?–´ ?ˆ?Š” ?„œë²„ìƒ?˜ ê²½ë¡œ
	private String fileName; 	// ?„œë²„ìƒ?˜ ?ŒŒ?¼ ?´ë¦?
	private String contentType; // ?ŒŒ?¼?˜ ?˜•?‹
	private long fileSize; 		// ?ŒŒ?¼?˜ ?š©?Ÿ‰

	// getter + setter + toString() ì¶”ê?
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