package com.pet.entity;
/**
 * JPA
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysUser entity. @author MyEclipse Persistence Tools
 */
@Entity 
@Table(name = "sys_user")
public class SysUser implements java.io.Serializable {

	// Fields
	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(length = 20)
	private String username;
	
	@Column(length = 20)
	private String pwd;

	// Constructors

	/** default constructor */
	public SysUser() {
	}

	/** full constructor */
	public SysUser(String username, String pwd) {
		this.username = username;
		this.pwd = pwd;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}