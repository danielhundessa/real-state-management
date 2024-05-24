package com.dan.backend.domain.dto.response;

import com.dan.backend.domain.dto.PropertyDto;
import com.dan.backend.enumSet.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private long id;
    private String name;

    private String email;
    private RoleType role;

    private List<PropertyDto> properties;


}
