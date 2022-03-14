package com.webshop.knittingwebshop.Controllers;

import com.webshop.knittingwebshop.models.Yarn;
import com.webshop.knittingwebshop.repository.YarnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/yarns")
public class AdminYarnsController {

    @Autowired
    private YarnRepository yarnRepository;

    @GetMapping
    public String index(Model model){
        List<Yarn> yarns = yarnRepository.findAll();

        model.addAttribute("yarns", yarns);
        return "admin/yarns/index";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute Yarn yarn){

        return "admin/yarns/add";
    }

    @PostMapping("/add")
    public String add(@Valid Yarn yarn, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){

        if (bindingResult.hasErrors()){
            return "admin/yarns/add";
        }
        redirectAttributes.addFlashAttribute("yarn", yarn);
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
        redirectAttributes.addFlashAttribute("message", "Yarn added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        redirectAttributes.addFlashAttribute("yarn", yarn);

        String slug = yarn.getName().toLowerCase();
        Yarn yarnExists = yarnRepository.findByName(yarn.getName().replace(" ", "-"));


        if(yarnExists != null){
            redirectAttributes.addFlashAttribute("message", "Yarn exists, please choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("yarn", yarn);
        }else {
            yarn.setSlug(slug);

            yarnRepository.save(yarn);
        }
        return "redirect:/admin/yarns/add";
    }
}
