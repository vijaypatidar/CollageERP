package com.svceindore.userservice.repositories;

import com.svceindore.userservice.model.ProfilePicture;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Vijay Patidar
 * Date: 03/02/21
 * Time: 8:36 PM
 **/
public interface ProfilePicRepository extends MongoRepository<ProfilePicture,String> {
}
