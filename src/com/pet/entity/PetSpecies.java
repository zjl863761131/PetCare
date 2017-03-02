package com.pet.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity 
@Table(name = "store_pic")
public class PetSpecies implements java.io.Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public int getPetNumber() {
		return petNumber;
	}

	public void setPetNumber(int petNumber) {
		this.petNumber = petNumber;
	}

	public StoreUser getStoreUser() {
		return storeUser;
	}

	public void setStoreUser(StoreUser storeUser) {
		this.storeUser = storeUser;
	}

	@Column(length = 20,nullable=false)
	private String petName;
	
	@Column(nullable=false)
	private int petNumber;
	
	@ManyToOne
	@JoinColumn(name = "StoreUserID")
	private StoreUser storeUser;
	
	

}