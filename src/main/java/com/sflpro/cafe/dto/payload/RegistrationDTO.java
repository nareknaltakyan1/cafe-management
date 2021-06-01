package com.sflpro.cafe.dto.payload;

import com.sflpro.cafe.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class RegistrationDTO {

    @NotEmpty
    @Size(min = 6, max = 50)
    private String username;

    @NotEmpty
    @Size(min = 8, max = 50)
    private String password;

    @NotNull
    private Role role;
}
