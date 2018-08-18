package com.evanhayes.evanhayes.controllers;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.evanhayes.evanhayes.Security.SecurityService;
import com.evanhayes.evanhayes.Security.UserService;
import com.evanhayes.evanhayes.Security.Validation.UserValidator;
import com.evanhayes.evanhayes.models.Category.Category;
import com.evanhayes.evanhayes.models.Data.CategoryDAO;
import com.evanhayes.evanhayes.models.Data.ImagesDAO;
import com.evanhayes.evanhayes.models.Data.UserRepository;
import com.evanhayes.evanhayes.models.Forms.LoginForm;
import com.evanhayes.evanhayes.models.Images.Images;
import com.evanhayes.evanhayes.models.Images.RotateImage;
import com.evanhayes.evanhayes.models.Images.fileImages;
import com.evanhayes.evanhayes.models.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Controller
public class indexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImagesDAO imagesDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserValidator logInValidator;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, Principal user) {
        model.addAttribute("title", "Home Page");
        model.addAttribute("images", imagesDAO.findAll());
        model.addAttribute("categories", categoryDAO.findAll());
        if(user != null) {
            String name = user.getName();
            model.addAttribute("user",name);
            return "index.html";
        }
        return "index.html";
    }

    @RequestMapping(value = "/{cat_id}", method = RequestMethod.GET)
    public String filteredIndex(Model model, Principal user, @PathVariable(value = "cat_id") int cat_id) {
        model.addAttribute("title", "Home Page");
        model.addAttribute("images", imagesDAO.findByCategoryId(cat_id));
        model.addAttribute("categories", categoryDAO.findAll());
        if(user != null) {
            String name = user.getName();
            model.addAttribute("user",name);
            return "index.html";
        }
        return "index.html";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAdd(Model model, Principal user) {
        model.addAttribute("title", "Add Images");
        model.addAttribute("categories", categoryDAO.findAll());
        if(user != null) {
            String name = user.getName();
            model.addAttribute("user",name);
            return "add.html";
        }
        return "add.html";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAdd(@RequestParam("files")MultipartFile[] files,
                             @RequestParam("category") int categoryId) throws ImageProcessingException, IOException {
        for(MultipartFile eachFile : files) {
            //Add each file to AWS bucket
            String fileName = eachFile.getOriginalFilename();
            String ext = eachFile.getOriginalFilename().split("\\.")[1];
            BufferedImage image = fileImages.autoRotate(eachFile);
            String path = fileImages.uploadImage(image, fileName, ext);

            //Add file to SQL database (table = images)
            Images newImage = new Images(path);
            Optional<Category> oCat = categoryDAO.findById(categoryId);
            Category cat = oCat.get();
            newImage.setCategory(cat);
            imagesDAO.save(newImage);
        }
        return "redirect:/";
    }

    @RequestMapping(value = "add_category", method = RequestMethod.POST)
    public String processAddCategory(@RequestParam("category") String category, Model model) {
        Category newCategory = new Category(category);
        categoryDAO.save(newCategory);
        model.addAttribute("title", "Add Images");
        return "redirect:/add";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String displayLogin(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        model.addAttribute("title", "Log In");
        return "login.html";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String processLogin(Model model, @ModelAttribute LoginForm form, Errors errors) {

        logInValidator.validate(form, errors);

        if(errors.hasErrors()) {
            return "login.html";
        }

        securityService.autologin(form.getUsername(), form.getPassword());
        return "redirect:";
    }

    @RequestMapping(value = "signup", method = RequestMethod.GET)
    public String displaySignup(Model model) {
        model.addAttribute("title", "Sign Up");
        model.addAttribute("signUpForm", new User());
        return "signup.html";
    }

    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public String processSignup(Model model, @ModelAttribute("signUpForm") User userForm, Errors errors) {

        userValidator.validate(userForm, errors);

        if(errors.hasErrors()) {
            return "signup.html";
        }

        userService.save(userForm);
        securityService.autologin(userForm.getUsername(),userForm.getPasswordConfirm());
        model.addAttribute("signedIn", "Signed in as " + userForm.getUsername());

        return "redirect:";
    }
}
