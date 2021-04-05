package com.example.demo.Controllers;

import com.example.demo.Entity.Basket;
import com.example.demo.Entity.Product;
import com.example.demo.Services.BasketService;
import com.example.demo.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/person")
public class PersonController {

    @Autowired
    ProductService productService;
    @Autowired
    BasketService basketService;

    @GetMapping("/profile")
    public String profile(){
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
        return "redirect:/person/personalAccount";
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

        return "redirect:/person/basket";
    }

}
