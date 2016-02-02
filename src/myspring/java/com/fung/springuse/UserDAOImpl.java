package com.fung.springuse;

public class UserDAOImpl implements UserDAO {
	public void save(User user) {
	    //Hibernate or Jdbc add
	    System.out.println("user saved!");
	  }
}
