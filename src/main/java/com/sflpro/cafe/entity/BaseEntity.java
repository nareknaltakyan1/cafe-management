package com.sflpro.cafe.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "ID")
	private Long id;

	@CreatedDate
	@Column(name = "CREATION_DATE")
	private LocalDateTime createDate;

	@CreatedBy
	@Column(name = "CREATED_BY")
	private String createdBy;

	@LastModifiedDate
	@Column(name = "LAST_MODIFICATION_DATE")
	private LocalDateTime lastModifyDate;

	@LastModifiedBy
	@Column(name = "LAST_MODIFIED_BY")
	private String modifiedBy;
}
