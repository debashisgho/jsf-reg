package com.debashis.jsf.registration;

import java.util.Map;

import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@RequestScoped
public abstract class AbstractBacking {
	
	@ManagedProperty(value = "#{facesContext}")
	private FacesContext facesContext;
	
	@ManagedProperty(value = "#{sessionScope}")
	private Map<String, Object> sessionMap;
	
	public UserBean getCurrentUser(){
		
		return (UserBean)getSessionMap().get("CurrentUser");
	
	}

	public FacesContext getFacesContext() {
		
	 facesContext = FacesContext.getCurrentInstance();
		
	 return facesContext;
}

	public void setFacesContext(FacesContext facesContext) {
		this.facesContext = facesContext;
	}

	
	public Map<String, Object> getSessionMap() {
		sessionMap = getFacesContext().getExternalContext().getSessionMap();
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}	
	
	public void setCurrentUser(UserBean currentUser){
		
		getSessionMap().remove("CurrentUser");
	    if(currentUser!=null){
	    	getSessionMap().put("CurrentUser", currentUser);
	    }
	}

	public boolean isUserLoggedIn(){
		
		return getSessionMap().containsKey("CurrentUser");
	}
	
}

