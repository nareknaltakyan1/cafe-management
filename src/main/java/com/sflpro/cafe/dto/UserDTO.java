package com.sflpro.cafe.dto;

import com.sflpro.cafe.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String username;
    private Role role;
    private boolean enabled;
}
