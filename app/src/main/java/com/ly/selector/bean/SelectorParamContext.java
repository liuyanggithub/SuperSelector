package com.ly.selector.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 图片基础数据选项
 */
public class SelectorParamContext implements Serializable {
	
	protected int maxCount;//最大选图数量
	protected boolean hasQualityMenu;//是否有图片清晰度选项
	protected boolean isHighQuality;//是否高清
	protected boolean isMulti;//是否多选
	protected ArrayList<String> selectedFile; //选中图片path
	
	/**
	 * 通用常量
	 */
	public static final String TAG_SELECTOR = "SelectorParamContext";
	private static final long serialVersionUID = 1L;
	public static final int MAX_COUNT = 9;//最多选择张数
	public static final int mcolor = 0xff0072c6;//预览按钮文字颜色(可点击)
	public static final int gcolor = 0xffcccccc;//预览按钮文字颜色(不可点击)
	public static final String[]menuItems = new String[] {"标清","原图"};

	public SelectorParamContext() {
		maxCount = MAX_COUNT;
		isMulti = true;
		hasQualityMenu = true;
		isHighQuality = false;
		selectedFile = new ArrayList<String>();
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public boolean isMulti() {
		return isMulti;
	}

	public void setMulti(boolean isMult) {
		this.isMulti = isMult;
	}

	public boolean hasQulityMenu() {
		return hasQualityMenu;
	}

	public void setHasQualityMenu(boolean hasQualityMenu) {
		this.hasQualityMenu = hasQualityMenu;
	}

	public boolean isHighQuality() {
		return isHighQuality;
	}

	public void setHighQuality(boolean isHighQulity) {
		this.isHighQuality = isHighQulity;
	}
	
	public ArrayList<String> getSelectedFile() {
		return selectedFile;
	}

	public void setSelectedFile(ArrayList<String> selectedFile) {
		this.selectedFile = selectedFile;
	}
	
	public String getPercent(){
		return selectedFile.size()+"/"+getMaxCount();
	}
	
	public String getQuality(){
		return menuItems[isHighQuality ?1:0];
	}
	
	public boolean isAvaliable(){
		return selectedFile.size()<maxCount;
	}
	
	public boolean isChecked(String path){
		return selectedFile.contains(path);
	}
	
	public void addItem(String path){
		selectedFile.add(path);
	}
	
	public void removeItem(String path){
		selectedFile.remove(path);
	}
}
