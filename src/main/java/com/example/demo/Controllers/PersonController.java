package com.example.demo.Controllers;

import com.example.demo.Entity.Basket;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.User;
import com.example.demo.Services.BasketService;
import com.example.demo.Services.ProductService;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


@Controller
//@RequestMapping("/person")
public class PersonController {

    @Autowired
    ProductService productService;
    @Autowired
    BasketService basketService;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String profile(Model model){
        User user = userService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("name",user.getName());
        model.addAttribute("surname",user.getSurname());
        model.addAttribute("login",user.getLogin());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("telephoneNumber",user.getTelephone_number());


        return "profile";
    }

    @GetMapping("/basket")
    public String basket(Model model){
        List<Basket> baskets = basketService.getAllBasketCurrentUser();
        model.addAttribute("products", baskets);
        return "basket";
    }

    @GetMapping("/personalAccount")
    public String personalAccount(Model model){
        model.addAttribute("product",productService.findAllSorted());
        return "personalAccount";
    }

    @PostMapping("/check")
    public String trying(@RequestParam(name = "product_id") String id){
        Product product = productService.findById(id);
        if(productService.deleteOneAmountOfProduct(product))
            basketService.saveOrUpdateOneAmountOfProduct(product);
        return "redirect:/personalAccount";
    }

    @PostMapping("/actionInBasket")
    public String actionInBasket(@RequestParam(name="nameOfProduct")String nameOfProduct,
                                 @RequestParam(name="operation") String operation){

        Product product = productService.findByTypeProduct(nameOfProduct);
        if(operation.equals("+")){
            if(productService.deleteOneAmountOfProduct(product)){
                basketService.addByNameOfProduct(nameOfProduct);
            }
        }else{
            basketService.deleteByNameOfProduct(nameOfProduct);
            productService.addOneAmountOfProduct(product);
        }

        return "redirect:/basket";
    }

    @GetMapping("/editProfile")
    public String editProfile(Model model, HttpServletRequest request){
        String psw = "";
        Cookie[] cookies = request.getCookies();
        for (Cookie c: cookies){
            if(c.getName().equals("password"))
                 psw = c.getValue();
        }

        User user = userService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("currentLogin",user.getLogin());
        model.addAttribute("currentPassword",psw);
        model.addAttribute("currentEmail",user.getEmail());
        model.addAttribute("currentName",user.getName());
        model.addAttribute("currentSurname",user.getSurname());
        model.addAttribute("currentTelephoneNumber",user.getTelephone_number());

        return "editProfile";
    }

    @GetMapping("/editProfileAction")
    public String editProfile(@ModelAttribute @Valid User user,
                              Errors errors,
                              Model model,
                              @RequestParam("realPassword") String realPassword,
                              HttpServletResponse response){

        User currentUser = userService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        if(errors.hasErrors()
//                || userService.checkingExistingUsers(user)
                || !passwordEncoder.matches(realPassword, currentUser.getPassword())) {
            model.addAttribute("currentLogin",user.getLogin());
            model.addAttribute("currentPassword",user.getPassword());
            model.addAttribute("currentEmail",user.getEmail());
            model.addAttribute("currentName",user.getName());
            model.addAttribute("currentSurname",user.getSurname());
            model.addAttribute("currentTelephoneNumber",user.getTelephone_number());

            List<FieldError> list = errors.getFieldErrors();
            for (FieldError f : list) {
                model.addAttribute(f.getField(), f.getDefaultMessage());
            }

//            if (userService.checkingExistingUsers(user)) {
//                model.addAttribute("login", "Пользователь с таким логином уже существует");
//            }

            if(!passwordEncoder.matches(realPassword, currentUser.getPassword())){
                model.addAttribute("uncorrectedPassword","Неверный пароль");
            }
            return "/editProfile";
        }

        userService.editProfile(user, currentUser.getLogin());

        Cookie pCookie = new Cookie("password", user.getPassword());
        pCookie.setMaxAge(60*60*24*365);
        response.addCookie(pCookie);

        return "redirect:/profile";
    }




}
