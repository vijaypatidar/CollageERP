package com.svceindore.userservice.cotrollers;

import com.svceindore.userservice.ProfilePicRepository;
import com.svceindore.userservice.configs.Roles;
import com.svceindore.userservice.model.ProfilePicture;
import org.bson.types.Binary;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Vijay Patidar
 * Date: 03/02/21
 * Time: 8:37 PM
 **/
@RestController
@RequestMapping("/api/user/photos/")
public class ProfilePicController {

    private final ProfilePicRepository profilePicRepository;

    public ProfilePicController(ProfilePicRepository profilePicRepository) {
        this.profilePicRepository = profilePicRepository;
    }

    @PostMapping("/profile/{username}")
    public String uploadProfile(@RequestParam("file") MultipartFile file, @PathVariable String username) throws IOException {

        ProfilePicture picture = new ProfilePicture();
        picture.setImage(new Binary(file.getBytes()));
        picture.setId(username);
        profilePicRepository.save(picture);

        return "Profile picture uploaded";
    }

    @RolesAllowed(Roles.ADMIN_ROLE)
    @GetMapping("/profile/{username}")
    @ResponseBody
    public void getProfile(HttpServletResponse response, @PathVariable String username) {
        response.setContentType("image/jpg");
        try {
            ProfilePicture picture = profilePicRepository.findById(username).get();
            byte[] data = picture.getImage().getData();
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
