package com.pet.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "pet_user")
public class PetUser implements java.io.Serializable{
	
	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(length = 20,nullable=false)
	private String username;
	
	@Column(length = 20,nullable=false)
	private String pwd;
	
	@Column(length = 20,nullable=false)
	private String email;
	
	@Column(length = 20,nullable=false)
	private String phone;
	
	@Column(length = 60)
	private String userAdderss;

	@OneToMany(mappedBy = "petUser", fetch = FetchType.EAGER)
	private Set<StoreComment> storecomment;
	
	
	public String getUserAdderss() {
		return userAdderss;
	}
	
	

	public void setUserAdderss(String userAdderss) {
		this.userAdderss = userAdderss;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Set<StoreComment> getStorecomment() {
		return storecomment;
	}

	public void setStorecomment(Set<StoreComment> storecomment) {
		this.storecomment = storecomment;
	}

}
