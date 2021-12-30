package com.sflpro.cafe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadataDTO
{

	private Long id;

	private String originalName;

	private String guid;

	private String type;

	private String context;

	private String path;
}
