package com.mvc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModelAndView {
	
	private String view;
	private Map<String, Object> objectList= new ConcurrentHashMap<String, Object>();
	
	public ModelAndView() {
	}

	public void setViewName(String view) {
		this.view = view;
	}
	
	public String getViewName() {
		return view;
	}

	public Object getMap(String str) {
		return objectList.get(str);
	}

	public void addObject(String str, Object val) {
		
		objectList.put(str, val);
	}
	
	public Map<String, Object> getObjList(){
		
		return objectList;
		
	}
}
