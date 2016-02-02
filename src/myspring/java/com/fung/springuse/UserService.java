package com.fung.springuse;

public class UserService {
	  private UserDAO userDAO;
	    
	  public UserDAO getUserDAO() {
	    return userDAO;
	  }
	  
	  public void setUserDAO(UserDAO userDAO) {
	    this.userDAO = userDAO;
	  }
	  
	  public void add(User user) {
	    this.userDAO.save(user);
	  }
	}
