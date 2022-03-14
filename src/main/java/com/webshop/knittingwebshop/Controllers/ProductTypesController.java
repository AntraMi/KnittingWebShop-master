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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/product-type")
public class ProductTypesController {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{slug}")
    public String category(@PathVariable String slug, Model model, @RequestParam(value="page", required = false) Integer p) {

        int perPage = 6;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);
        long count = 0;

        if (slug.equals("all")) {

            Page<Product> products = productRepository.findAll(pageable);

            count = productRepository.count();

            model.addAttribute("products", products);
        } else {

            ProductType productType = productTypeRepository.findBySlug(slug);

            if (productType == null) {
                return "redirect:/";
            }

            int productTypeId = productType.getId();
            String categoryName = productType.getName();
            List<Product> products = productRepository.findAllByProductTypeId(Integer.toString(productTypeId), pageable);

            count = productRepository.countByProductTypeId(Integer.toString(productTypeId));

            model.addAttribute("products", products);
            model.addAttribute("categoryName", categoryName);
        }

        double pageCount = Math.ceil((double)count / (double)perPage);

        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "products";
    }

}
