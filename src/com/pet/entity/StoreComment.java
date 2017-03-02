package com.pet.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity 
@Table(name = "store_comment")
public class StoreComment implements java.io.Serializable{

	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	
	@ManyToOne
	@JoinColumn(name = "StoreUserID")
	private StoreUser storeUser;
	

	@ManyToOne
	@JoinColumn(name = "PetUserID")
	private PetUser petUser;
	
	
	@Column(length = 1000,nullable=false)
	private String conmment;
	
	@Column
	private int stars = 0;
	
	@Column(length = 20,nullable=false)
	private String email;
	
	@Column
	private boolean anonymous = false;
	
	@Column
	private Date dateOfComment;
	
	

}