package com.mvc;

import java.io.File;
import java.util.Map;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class DispatcherServlet extends HttpServlet {
	
	ArrayList<Class<?>> controllerList = new ArrayList<Class<?>>();
       
    /**
     *@see HttpServlet#HttpServlet()
     */
    public DispatcherServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {

		LoadController("e:\\JAVA\\eclipseJee\\SpringMVC\\src");
	
		ModelAndView mdv=(ModelAndView) MatchRequestMapping(request.getServletPath(),
				getInput(request));
		
		for (Map.Entry<String, Object> entry : mdv.getObjList().entrySet()) {  	
			request.setAttribute(entry.getKey(), entry.getValue());
		    System.out.println("The Key is " +
			entry.getKey() + ", The Value is " + 
		    		entry.getValue());  	  
		}
			
		request.getRequestDispatcher(mdv.getViewName() + 
				".jsp").forward(request, response);
	}
	
	
	/**
	 * @param request
	 * @return ModelAndView
	 */
	public ModelAndView getInput(HttpServletRequest request){		
		ModelAndView mav=new ModelAndView();
		Map<?, ?> map=request.getParameterMap();
		ArrayList<String> keyList = new ArrayList<String>();
		ArrayList<Object> valueList = new ArrayList<Object>();
			
		for (Object key : map.keySet()) { 
			keyList.add((String)key);	  
		}
		
		for (Object values : map.values()) {  
			String[]  value= (String[]) values;	
			valueList.add(value[0]);  
		}
		
		for(int i=0;i<keyList.size();i++){
			mav.addObject(keyList.get(i), valueList.get(i));
		}
		
		return mav;	
	}

	
	/**
	 * @param filePath
	 */
	public void LoadController(String filePath) {
			File readFile = new File(filePath);
			File[] files = readFile.listFiles();
			
			String fileName = null;
			for (File file : files) {
				fileName = file.getName();
				if (file.isFile()) {
					
					if (fileName.endsWith(".java")) {			
						try {							
							String  str=filePath+File.separator+ fileName;
							String beanClassName=str.substring("e:\\JAVA\\eclipseJee\\SpringMVC\\src".length()+1, 
									str.length()-5).replace('\\', '.');
							Class<?> beanClass = Class.forName(beanClassName);
						
							if(beanClass.isAnnotationPresent(Controller.class)){
								controllerList.add(beanClass);
							}										
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
					}
				} else {
					LoadController(filePath + File.separator + fileName);
				}
			}
	}

	/**
	 * @param servletPath
	 * @return 
	 */
	public Object MatchRequestMapping(String servletPath,ModelAndView mav){
		
		for(Class<?> controllerClass: controllerList ) {
			for(Method method : controllerClass.getMethods()){      
	        	if(method.getAnnotation(RequestMapping.class).value().equals(servletPath)){
        		
	        		try {
	        			Object args[]=new Object[1];
	        			args[0]=mav;
						return method.invoke(controllerClass.newInstance(),args);
					} catch (Exception e) {
						e.printStackTrace();
					} 
	        	}  
	  
	        }  
		}
		return null;	
	}
}
