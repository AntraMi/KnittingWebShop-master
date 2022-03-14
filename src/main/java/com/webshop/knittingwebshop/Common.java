package com.webshop.knittingwebshop;

import com.webshop.knittingwebshop.models.Cart;
import com.webshop.knittingwebshop.models.Page;
import com.webshop.knittingwebshop.models.ProductType;
import com.webshop.knittingwebshop.repository.PageRepository;
import com.webshop.knittingwebshop.repository.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class Common {

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @ModelAttribute
    public void shareData(Model model, HttpSession session, Principal principal){

        if (principal != null) {
            model.addAttribute("principal", principal.getName());
        }

        List<Page> pages = pageRepository.findAll();

        List<ProductType> productTypes = productTypeRepository.findAll();

        boolean cartActive = false;

        if (session.getAttribute("cart") != null) {

            HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>)session.getAttribute("cart");

            int size = 0;
            double total = 0;

            for (Cart value : cart.values()) {
                size += value.getQuantity();
                total += value.getQuantity() * Double.parseDouble(value.getPrice());
            }

            model.addAttribute("csize", size);
            model.addAttribute("ctotal", total);

            cartActive = true;
        }

        model.addAttribute("cpages", pages);
        model.addAttribute("cproductTypes", productTypes);
        model.addAttribute("cartActive", cartActive);
    }
}
