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
@Table(name = "store_user")
public class StoreUser implements java.io.Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(length = 20,nullable=false)
	private String stUsername;
	
	@Column(length = 20,nullable=false)
	private String stPwd;
	
	@Column(length = 40,nullable=false)
	private String stEmail;
	
	@Column(length = 11,nullable=false)
	private String stPhone;
	
	@Column(length = 60,nullable=false)
	private String stAddress;
	
	

	@Column(length = 100,nullable=false)
	private String stDescribe;
	
	@Column(length = 100,nullable=false)
	private String stLicense;
	
	@OneToMany(mappedBy = "storeUser", fetch = FetchType.EAGER)
	private Set<StorePic> storePic;
	
	@OneToMany(mappedBy = "storeUser", fetch = FetchType.EAGER)
	private Set<StoreComment> storeComment;
	
	@OneToMany(mappedBy = "storeUser", fetch = FetchType.EAGER)
	private Set<PetSpecies> petSpecies;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStUsername() {
		return stUsername;
	}

	public void setStUsername(String stUsername) {
		this.stUsername = stUsername;
	}

	public String getStPwd() {
		return stPwd;
	}

	public void setStPwd(String stPwd) {
		this.stPwd = stPwd;
	}

	public String getStEmail() {
		return stEmail;
	}

	public void setStEmail(String stEmail) {
		this.stEmail = stEmail;
	}

	public String getStPhone() {
		return stPhone;
	}

	public void setStPhone(String stPhone) {
		this.stPhone = stPhone;
	}

	public String getStAddress() {
		return stAddress;
	}

	public void setStAddress(String stAddress) {
		this.stAddress = stAddress;
	}

	public String getStDescribe() {
		return stDescribe;
	}

	public void setStDescribe(String stDescribe) {
		this.stDescribe = stDescribe;
	}

	public String getStLicense() {
		return stLicense;
	}

	public void setStLicense(String stLicense) {
		this.stLicense = stLicense;
	}

	public Set<StorePic> getStorePic() {
		return storePic;
	}

	public void setStorePic(Set<StorePic> storePic) {
		this.storePic = storePic;
	}

	public Set<StoreComment> getStoreComment() {
		return storeComment;
	}

	public void setStoreComment(Set<StoreComment> storeComment) {
		this.storeComment = storeComment;
	}

	public Set<PetSpecies> getPetSpecies() {
		return petSpecies;
	}

	public void setPetSpecies(Set<PetSpecies> petSpecies) {
		this.petSpecies = petSpecies;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}
