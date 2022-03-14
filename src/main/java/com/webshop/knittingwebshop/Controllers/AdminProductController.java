package com.webshop.knittingwebshop.Controllers;

import com.webshop.knittingwebshop.models.Product;
import com.webshop.knittingwebshop.models.ProductType;
import com.webshop.knittingwebshop.repository.ProductRepository;
import com.webshop.knittingwebshop.repository.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;


    @GetMapping
    public String index(Model model, @RequestParam(value="page", required = false) Integer p){

        int perPage = 8;
        int page = (p != null) ? p : 0;

        Pageable pageable = PageRequest.of(page, perPage);
        Page<Product> products = productRepository.findAll(pageable);

        List<ProductType> productTypes = productTypeRepository.findAll();

        HashMap<Integer, String> types = new HashMap<>();
        for (ProductType type : productTypes){
            types.put(type.getId(), type.getName());
        }
        model.addAttribute("products", products);
        model.addAttribute("types", types);

        Long count = productRepository.count();
        double pageCount = Math.ceil((double)count / (double)perPage);

        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "admin/products/index";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute Product product, Model model){

        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        List<ProductType> productTypes = productTypeRepository.findAll();
        model.addAttribute("productTypes", productTypes);


        return "admin/products/add";
    }
    @PostMapping("/add")
    public String add(@Valid Product product,
                      BindingResult bindingResult,
                      MultipartFile file,
                      RedirectAttributes redirectAttributes,
                      Model model) throws IOException {

        List<ProductType> productTypes = productTypeRepository.findAll();

        if (bindingResult.hasErrors()) {
            model.addAttribute("productTypes", productTypes);
            return "admin/products/add";
        }

        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + filename);

        if (filename.endsWith("jpg") || filename.endsWith("png") ) {
            fileOK = true;
        }

        redirectAttributes.addFlashAttribute("message", "Product added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = product.getName().toLowerCase().replace(" ", "-");

        Product productExists = productRepository.findBySlug(slug);

        if (! fileOK ) {
            redirectAttributes.addFlashAttribute("message", "Image must be a jpg or a png");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("product", product);
        }
        else if ( productExists != null ) {
            redirectAttributes.addFlashAttribute("message", "Product exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("product", product);
        } else {
            product.setSlug(slug);
            product.setImage(filename);
            productRepository.save(product);

            Files.write(path, bytes);
        }

        return "redirect:/admin/products/add";
    }
}