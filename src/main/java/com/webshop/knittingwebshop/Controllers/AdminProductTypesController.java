package com.webshop.knittingwebshop.Controllers;

import com.webshop.knittingwebshop.models.ProductType;
import com.webshop.knittingwebshop.repository.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/product-types")
public class AdminProductTypesController {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @GetMapping
    public String index(Model model){
        List<ProductType> productTypes = productTypeRepository.findAll();

        model.addAttribute("productTypes", productTypes);

        return "admin/product-types/index";
    }

    @GetMapping("/add")
    public String add(ProductType productType){

        return "admin/product-types/add";
    }
    @PostMapping("/add")
    public String add(@Valid ProductType productType,
                      BindingResult bindingResult,
                      RedirectAttributes redirectAttributes,
                      Model model){

        if (bindingResult.hasErrors()){
            return "admin/product-types/add";
        }
        redirectAttributes.addFlashAttribute("productType", productType);
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
        redirectAttributes.addFlashAttribute("message", "Product Type added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = productType.getName().toLowerCase().replace(" ", "-");

        ProductType productTypeExists = productTypeRepository.findByName(productType.getName());

        if(productTypeExists != null){

            redirectAttributes.addFlashAttribute("message", "Product Type exists, please choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("productTypeInfo", productType);
        }else {
            productType.setSlug(slug);
            productType.setSorting(100);

            productTypeRepository.save(productType);
        }
        return "redirect:/admin/product-types/add";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        ProductType productType = productTypeRepository.getReferenceById(id);

        model.addAttribute("productType", productType);

        return "admin/product-types/edit";

    }
    @PostMapping("/edit")
    public String edit(@Valid ProductType productType, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){

        ProductType productTypeCurrent = productTypeRepository.getReferenceById(productType.getId());

        if (bindingResult.hasErrors()){
            model.addAttribute("productTypeName", productTypeCurrent.getName());
            return "admin/product-types/edit";
        }
        redirectAttributes.addFlashAttribute("message", "Product Types edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = productType.getName().toLowerCase().replace(" ", "-");

        ProductType productTypeExists = productTypeRepository.findByName(productType.getName());

        if(productTypeExists != null){
            redirectAttributes.addFlashAttribute("message", "Product Type exists, please choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

        }else {
            productType.getSlug();

            productTypeRepository.save(productType);
        }
        return "redirect:/admin/product-types/edit" + productType.getId();
    }

    @PostMapping("/reorder")
    public @ResponseBody String reorder(@RequestParam("id[]") int[] id) {

        int count = 1;
        ProductType productType;

        for (int productTypeId : id) {
            productType = productTypeRepository.getById(productTypeId);
            productType.setSorting(count);
            productTypeRepository.save(productType);
            count++;
        }

        return "ok";
    }

}
