package com.webshop.knittingwebshop.Controllers;

import com.webshop.knittingwebshop.models.Page;
import com.webshop.knittingwebshop.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/pages")
public class AdminPagesController {

    @Autowired
    private PageRepository pageRepository;

    @GetMapping
    public String index(Model model){

        List<Page> pages = pageRepository.findAll();

        model.addAttribute("pages", pages);
        return "admin/pages/index";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute Page page){

        return "admin/pages/add";
    }

    @PostMapping("/add")
    public String add(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){

        if (bindingResult.hasErrors()){
            return "admin/pages/add";
        }
        redirectAttributes.addFlashAttribute("page", page);
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
        redirectAttributes.addFlashAttribute("message", "Page added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-") : page.getSlug().toLowerCase().replace(" ", "-");

//        String slug = page.getSlug();

        Page slugExists = pageRepository.findBySlug(slug);

        if(slugExists != null){
            redirectAttributes.addFlashAttribute("message", "Slug exists, please choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("page", page);
        }else {
            page.setSlug(slug);
            page.setSorting(100);

            pageRepository.save(page);
        }
        return "redirect:/admin/pages/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        Page page = pageRepository.getReferenceById(id);

        model.addAttribute("page", page);

        return "admin/pages/edit";

    }

    @PostMapping("/edit")
    public String edit(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){

        Page pageCurrent = pageRepository.getReferenceById(page.getId());
        if (bindingResult.hasErrors()){
            model.addAttribute("pageTitle", pageCurrent.getTitle());
            return "admin/pages/edit";
        }
        redirectAttributes.addFlashAttribute("page", page);
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
        redirectAttributes.addFlashAttribute("message", "Page edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
//
        String slug = page.getSlug() ==
                "" ? page.getTitle().toLowerCase().replace(" ", "-") :
                    page.getSlug().toLowerCase().replace(" ", "-");

        Page slugExists = pageRepository.findBySlugAndIdNot(slug, page.getId());

        if(slugExists != null){
            redirectAttributes.addFlashAttribute("message", "Slug exists, please choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("page", page);
        }else {
            page.setSlug(slug);
            pageRepository.save(page);
        }
        return "redirect:/admin/pages/edit/" + page.getId();
    }
    @GetMapping("/delete/{id}")
    public String edit(@PathVariable int id, RedirectAttributes redirectAttributes) {

        pageRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("message", "Page deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/pages";
    }

    @PostMapping("/reorder")
    public @ResponseBody String reorder(@RequestParam("id[]") int[] id) {

        int count = 1;
        Page page;

        for (int pageId : id) {

            page = pageRepository.getReferenceById(pageId);

            page.setSorting(count);
            pageRepository.save(page);
            count++;
        }

        return "ok";
    }

}
