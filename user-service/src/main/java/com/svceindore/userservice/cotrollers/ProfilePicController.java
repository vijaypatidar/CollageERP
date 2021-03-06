package com.svceindore.userservice.cotrollers;

import com.svceindore.userservice.repositories.ProfilePicRepository;
import com.svceindore.userservice.configs.Roles;
import com.svceindore.userservice.model.ProfilePicture;
import net.minidev.json.JSONObject;
import org.bson.types.Binary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;

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

    @PostMapping("/profile/update/{username}")
    public ResponseEntity<?> updateUserProfile(@RequestParam("file") MultipartFile file, @PathVariable String username,Principal principal) throws IOException {

        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(Roles.ROLE_ADMIN)) {
                isAdmin = true;
                break;
            }
        }
        if (isAdmin||username.equals(principal.getName())){
            JSONObject jsonObject = new JSONObject();
            try {
                ProfilePicture picture = new ProfilePicture();
                picture.setImage(new Binary(file.getBytes()));
                picture.setUsername(username);
                profilePicRepository.save(picture);
                jsonObject.appendField("status",true);
                jsonObject.appendField("message","Profile picture updated successfully.");
            }catch (Exception e){
                jsonObject.appendField("status",false);
                jsonObject.appendField("message","Unable to update profile");
            }
            return ResponseEntity.ok(jsonObject.toJSONString());
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/profile/update")
    public ResponseEntity<String> updateSelfProfile(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        JSONObject jsonObject = new JSONObject();
        try {
            String contentType = file.getContentType();
            if (contentType==null||!contentType.startsWith("image/")){
                return ResponseEntity.badRequest().body("Invalid content type");
            }
            ProfilePicture picture = new ProfilePicture();
            picture.setImage(new Binary(file.getBytes()));
            picture.setUsername(principal.getName());
            profilePicRepository.save(picture);
            jsonObject.appendField("status",true);
            jsonObject.appendField("message","Profile picture uploaded successfully.");
        }catch (Exception e){
            jsonObject.appendField("status",false);
            jsonObject.appendField("message","Unable to upload profile");
        }
        return ResponseEntity.ok(jsonObject.toJSONString());
    }

    @PermitAll
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
            response.setStatus(HttpStatus.NOT_FOUND.value());
            e.printStackTrace();
        }
    }

}
