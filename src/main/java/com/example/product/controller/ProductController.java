package com.example.product.controller;

import com.example.product.Model.Product;
import com.example.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;


    // product adding html form
    @GetMapping("/addproducthtml")
    public String productaddheml() {
        return "addproduct";
    }

    //product adding end point
    @PostMapping("/addproduct")
    public Product addproduct(

            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image,

            @RequestParam("price") Double price,
            @RequestParam("quantity") String quantity,
            @RequestParam("email") String email,
            @RequestParam("phone_number") Long phone_number,
            @RequestParam("location") String location

    ) {
        return productService.addproduct(name, description, image, price, quantity, email, phone_number, location);
    }

    //get the product  by using end point show on product.html
    @GetMapping("/Access_by_user_product")
    public String getallproduct(Model model) {

        List<Product> products = productService.getallproduct();
        model.addAttribute("products", products);
        return "product";
    }

    @GetMapping("/Access_by_customer_product")
    public String allproduct(Model model) {

        List<Product> products = productService.allproduct();
        model.addAttribute("products", products);
        return "cproduct";
    }

    //user for show image
    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {
        Path uploadDir = Paths.get("C:/Users/kailash/uploads");
        Path file = uploadDir.resolve(filename);
        Resource resource = new UrlResource(file.toUri());

        if(resource.exists() && resource.isReadable()) {
            // Detect the content type dynamically
            String contentType = "application/octet-stream";
            try {
                contentType = Files.probeContentType(file);
            } catch (IOException e) {
                // fallback to default
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(resource);
        } else {
            throw new RuntimeException("Could not read file: " + filename);
        }
    }

}
