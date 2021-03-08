package com.svceindore.userservice.model.helpers;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Vijay Patidar
 * Date: 08/03/21
 * Time: 10:04 PM
 **/
@Getter
@Setter
public class ResetPassword {
    private String username;
    private String password;
}
