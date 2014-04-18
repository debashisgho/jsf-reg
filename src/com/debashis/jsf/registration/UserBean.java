package com.debashis.jsf.registration;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


@ManagedBean
@SessionScoped
public class UserBean {
	
	private String userid;
	private String pwd;
	private String fname;
	private String lname;
	private String sex;
	private Date dob;
	private String email;
	private String profession;
	private ArrayList<String> professions=new ArrayList<String>();
	
	public UserBean() {
		super();
		this.setProfessions();
		
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getFname() {
		return fname;		
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Object getDob() {
		String outDobStr=null;
		if(this.dob!=null){
			SimpleDateFormat toFormatter = new SimpleDateFormat("dd-MM-yyyy");
			outDobStr = toFormatter.format(this.dob);
				
		}
		return outDobStr;
	}
	public void setDob(Object dob) {
		Date dobDate=(Date)dob;
		this.dob =dobDate;
		
		}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public ArrayList<String> getProfessions() {
		return professions;
	}
	public void setProfessions() {
		this.professions.add("Doctor");
		this.professions.add("Engineer");
		this.professions.add("Businessman");
		this.professions.add("Writer");
		this.professions.add("Other");
	}
	//validateEmail
	public void validateEmail(FacesContext context, UIComponent toValidate, Object value) throws ValidatorException {
			String emailStr = (String) value;
			if (-1 == emailStr.indexOf("@")) {
			FacesMessage message = new FacesMessage("Invalid email address, @domain is required");
			throw new ValidatorException(message);
			}
			}
	//add confirmed user
	public String addConfirmedUser() {
		
		PreparedStatement preparedStmt =null;
		int insertCnt=0;
		String sqlStmt=null;
		Connection dbConn=null;
		String addConfirmedUserRsp=null;
		String quoteVal="'";
		try{
			
			dbConn = DbOperations.getDbConn();			
			//insert user details
			dbConn.setAutoCommit(true);
			sqlStmt="insert into jsf_user_details (userid,firstname,lastname,sex,dob,email,profession) values ("+quoteVal+this.getUserid()+quoteVal+","+quoteVal+this.getFname()+quoteVal+","+quoteVal+this.getLname()+quoteVal+","+quoteVal+this.getSex()+quoteVal+",to_date("+quoteVal+this.getDob()+quoteVal+","+quoteVal+"dd-mm-yyyy"+quoteVal+"),"+quoteVal+this.getEmail()+quoteVal+","+quoteVal+this.getProfession()+quoteVal+")";
			preparedStmt= dbConn.prepareStatement(sqlStmt);
			insertCnt = preparedStmt.executeUpdate();
			if(insertCnt ==1){								
				sqlStmt ="insert into jsf_userpwd (userid,password) values ("+quoteVal+this.getUserid()+quoteVal+","+quoteVal+this.getPwd()+quoteVal+")";
				preparedStmt=dbConn.prepareStatement(sqlStmt);
				int insertUserPwdCnt=preparedStmt.executeUpdate();
				if(insertUserPwdCnt ==1){								
					addConfirmedUserRsp="S";									
			}			
												
		}
		}
		
		catch(Exception e){
			System.out.println(e.toString());
		}
		
		finally{
			try{
								
				if(preparedStmt!=null){
					preparedStmt.close();
				}
				
				if(dbConn!=null){
					dbConn.close();			
				}
			}
			catch(Exception e){
				System.out.println(e.toString());
			}
		}
		
		boolean added=false;
		if(addConfirmedUserRsp.equals("S"))	{
			added = true; 
		}
		
		FacesMessage fmessage = null;
		String outcome = null;
		if (added) {
			System.out.println("new user add response"+added);
		fmessage = new FacesMessage("Successfully added new user");
		outcome = "done";
		} else {
		fmessage = new FacesMessage("Failed to add new user");
		outcome = "register";
		}
		
		FacesContext.getCurrentInstance().addMessage(null, fmessage);
			return outcome;
	}	

	public String checkUserCredentials(){
		System.out.println("checkUserCredentials is called");
		FacesMessage fmessage;
		String checkUserPwdRsp=checkUserPwd();
		
		if(checkUserPwdRsp!="S"){
			//log in FAILED
			fmessage= new FacesMessage("User Id or Password is wrong");
			FacesContext.getCurrentInstance().addMessage(null, fmessage);
			return "Failed";
			}
		
		else{
			//user password is alright.		
			//set the currentUser in the SessionMap
			new LoginBacking().setCurrentUser(this);
			System.out.println("User logged in :"+this.userid);
			return "Success";
		}
	}
	

	public String checkUserPwd(){
		
		System.out.println("checkUserPwd() is starting");
		PreparedStatement preparedStmt =null;
		ResultSet resultSet=null;
		String sqlStmt=null;
		Connection dbConn=null;
		String checkPwdRsp=null;
		String checkUserPwdRsp=null;
		try {
			dbConn = DbOperations.getDbConn();
			
			//check password
			sqlStmt="select password from jsf_userpwd where userid='"+getUserid()+"'";
			preparedStmt=dbConn.prepareStatement(sqlStmt);
			resultSet=preparedStmt.executeQuery();
			if(resultSet.next()){
				if(resultSet.getString(1).equals(this.pwd)){
					checkPwdRsp="S";
					System.out.println("checkUserPwd() - password is correct validated against Db");
				}
			}
			
			if(checkPwdRsp.equals("S")){
				//get userBean properties for the logged in user
				
			    sqlStmt="select * from jsf_user_details where userid='"+getUserid()+"'";
			    preparedStmt=dbConn.prepareStatement(sqlStmt);
			    resultSet=preparedStmt.executeQuery();
			    if(resultSet.next()){
			    	this.fname=resultSet.getString("FIRSTNAME");
			    	this.lname=resultSet.getString("LASTNAME");
			    	this.sex=resultSet.getString("SEX");
			    	this.email=resultSet.getString("EMAIL");
			    	this.profession=resultSet.getString("PROFESSION");
			    	this.dob=resultSet.getDate("DOB");
			    }
				checkUserPwdRsp="S";
				System.out.println("checkUserPwd() - user details were retrieved from DB");
			}
			
			
			
		}
		
		catch (Exception e){
			System.out.println(e.toString());
			checkUserPwdRsp="F";
		}
		
		finally{
			
			try{
				
				if(resultSet!=null){
					resultSet.close();
				}
				
				if(preparedStmt!=null){
					preparedStmt.close();
				}
				
				if(dbConn!=null){
					dbConn.close();			
				}
				
			}
			catch(Exception e){
				System.out.println(e.toString());
			}
			
		}
		
		return checkUserPwdRsp;
		
	}
	
	public static boolean checkUserIdAvailability(String userid){
		boolean isUserIdAvailable=false;
		userid = userid.trim();
		PreparedStatement preparedStmt =null;
		String sqlStmt=null;
		Connection dbConn=null;
		ResultSet resultSet =null;
		String quoteVal="'";
		try{
			
			dbConn = DbOperations.getDbConn();			
			//insert user details
			
			sqlStmt="select count(*) from jsf_user_details where userid="+quoteVal+userid+quoteVal;
			preparedStmt= dbConn.prepareStatement(sqlStmt);
			resultSet = preparedStmt.executeQuery();
			
			if(resultSet.next()){
				if(resultSet.getInt(1) >0){
					isUserIdAvailable=false;
				}
				
				else{
					isUserIdAvailable=true;
				}
													
			}
			
		}
		
		catch(Exception e){
			System.out.println(e.toString());
		}
		
		finally{
			try{
								
				if(preparedStmt!=null){
					preparedStmt.close();
				}
				
				if(dbConn!=null){
					dbConn.close();			
				}
			}
			catch(Exception e){
				
			}
		}
		
		return isUserIdAvailable;
	}
	public static String checkPwdStandard(String pwd){
		String pwdStandard=null;
		//logic to check pwd standard -Strong, Medium, Weak
		int pwdLen = pwd.length();
		if(pwdLen <= 4)
		{
			pwdStandard="Weak";
		}
		
		if(pwdLen >4 && pwdLen <=6)
		{
			pwdStandard ="Medium";
		}
		
		if(pwdLen >6)
		{
			pwdStandard ="Strong";
		}
		return pwdStandard;
	}
	
	
	
	

}



