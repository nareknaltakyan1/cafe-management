package com.sflpro.cafe.entity;

import com.sflpro.cafe.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@javax.persistence.Table(name = "USER")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity
{

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	@Enumerated
	@Column(name = "ROLE")
	private Role role;

	@Column(name = "ENABLED")
	private boolean enabled;

	@OneToMany(mappedBy = "waiter", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Table> tables;
}
