package com.svceindore.userservice.model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Vijay Patidar
 * Date: 03/02/21
 * Time: 8:32 PM
 **/

@Document
public class ProfilePicture {
    @Id
    private String username;
    private Binary image;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Binary getImage() {
        return image;
    }

    public void setImage(Binary image) {
        this.image = image;
    }
}
