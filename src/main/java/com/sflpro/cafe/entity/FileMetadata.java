package com.sflpro.cafe.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Table;


@Entity
@Table(name = "file_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadata extends BaseEntity {

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "guid", nullable = false, length = 2147483647)
    private String guid;

    @Column(name = "type")
    private String type;

    @Column(name = "context")
    private String context;

    @Column(name = "path", nullable = false, length = 2147483647)
    private String path;
}
