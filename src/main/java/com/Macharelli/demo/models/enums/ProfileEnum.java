package com.Macharelli.demo.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.annotation.Profile;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum ProfileEnum {
    ADMIN(1,"ROLE_ADMIN"),
    USER(2, "ROLE_USERS");

    private final Integer code;
    private final String description;

    public static ProfileEnum toEnum(Integer code){

        if(Objects.isNull(code)){
            return null;
        }

        for(ProfileEnum x : ProfileEnum.values()){
            if (code.equals(x.getCode())){
                return x;
            }
        }
        throw new IllegalArgumentException("Invalid code: "+code);

    }
}
