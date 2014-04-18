package com.debashis.jsf.registration;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIInput;
import javax.faces.event.AjaxBehaviorEvent;


@ManagedBean
@SessionScoped
public class UiExtras {
	
	private String userIdAvailabilityMsg="";
	private String pwdStandard="";
	private String pwdStandardColor="";
		
	
	public UiExtras(){
	
	}

	//generate Getters and Setters

	public String getUserIdAvailabilityMsg() {
		
	 		return userIdAvailabilityMsg;
	}


	public void setUserIdAvailabilityMsg(String userIdAvailabilityMsg) {
		this.userIdAvailabilityMsg = userIdAvailabilityMsg;
	}
	
	
	public String getPwdStandard() {
		return pwdStandard;
	}


	public void setPwdStandard(String pwdStandard) {
		this.pwdStandard = pwdStandard;
	}
	
	public String getPwdStandardColor() {
		return pwdStandardColor;
	}

	public void setPwdStandardColor(String pwdStandardColor) {
		
		this.pwdStandardColor=pwdStandardColor;
	}

	//check availability of user id	

	public void checkUserIdAvailability(AjaxBehaviorEvent e){
		
		UIInput userIdTextInput = (UIInput)e.getComponent();
		String userId = userIdTextInput.getValue().toString();		
		boolean isUserIdAvailable = UserBean.checkUserIdAvailability(userId);		
		String userIdAvailabilityMsg=null;
		if(isUserIdAvailable)
		{
			userIdAvailabilityMsg ="User id is available";
		}
		
		else{
			userIdAvailabilityMsg ="User id is not available";
		}
				
		this.setUserIdAvailabilityMsg(userIdAvailabilityMsg);
	}
	
	//check pwd standard
	public void checkPwdStandard(AjaxBehaviorEvent e){
		UIInput pwdTextInput = (UIInput)e.getComponent();
		String pwd = pwdTextInput.getValue().toString();
		String pwdStandard = UserBean.checkPwdStandard(pwd);
		this.setPwdStandard(pwdStandard);
		String pwdStandardColor="";
		
		if(this.pwdStandard =="Weak")
		{
			pwdStandardColor = "#FF9980"; //Red
		}
		
		if(this.pwdStandard =="Medium")
		{
			pwdStandardColor = "#FFFF00"; //Yellow
		}
		
		if(this.pwdStandard =="Strong")
		{
			pwdStandardColor = "#99FF66"; //Green
		}
		
		this.setPwdStandardColor(pwdStandardColor);
		
	}
	

}
