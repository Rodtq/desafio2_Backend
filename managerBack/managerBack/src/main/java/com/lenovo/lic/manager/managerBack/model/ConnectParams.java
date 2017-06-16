/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenovo.lic.manager.managerBack.model;

/**
 *
 * @author vntrotq
 */
public class ConnectParams {
    public ConnectParams(){}
    private String user;
    private String password;
    private int[] address;

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the address
     */
    public int[] getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(int[] address) {
        this.address = address;
    }
    
    
}
