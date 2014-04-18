package com.debashis.jsf.registration;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ComponentSystemEvent;

@ManagedBean
public class LoginBacking extends AbstractBacking {

	public LoginBacking() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void forwardToLogInIfNotLoggedIn(ComponentSystemEvent csEvt){
		
		String viewId =getFacesContext().getViewRoot().getViewId();
		if(!isUserLoggedIn() && 
				!viewId.startsWith("/login") && !viewId.startsWith("/register") && !viewId.startsWith("/confirm"))
		{
			
			getFacesContext().getApplication().getNavigationHandler().handleNavigation(getFacesContext(), null, "/login.xhtml?faces-redirect=true");
		}
		
	}
	
	public String logOut(){
				
	getFacesContext().getExternalContext().invalidateSession();
	return "Success";
	}


	
}
