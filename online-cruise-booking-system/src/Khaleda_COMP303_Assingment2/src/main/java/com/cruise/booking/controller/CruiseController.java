package com.cruise.booking.controller;

import com.cruise.booking.entity.Cruise;
import com.cruise.booking.service.CruiseService;
import com.cruise.booking.service.PortService;
import com.cruise.booking.service.ShipService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cruises")
public class CruiseController {

    private final CruiseService cruiseService;
    private final ShipService shipService;
    private final PortService portService;

    public CruiseController(CruiseService cruiseService, ShipService shipService, PortService portService) {
        this.cruiseService = cruiseService;
        this.shipService = shipService;
        this.portService = portService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("cruises", cruiseService.getAllCruises());
        return "cruises/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("cruise", new Cruise());
        model.addAttribute("ships", shipService.getAllShips());
        model.addAttribute("ports", portService.getAllPorts());
        return "cruises/form";
    }

    @GetMapping("/{id}/view")
    public String viewDetail(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return cruiseService.getCruiseById(id).map(cruise -> {
            model.addAttribute("cruise", cruise);
            return "cruises/detail";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Cruise not found");
            return "redirect:/cruises";
        });
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return cruiseService.getCruiseById(id).map(cruise -> {
            model.addAttribute("cruise", cruise);
            model.addAttribute("ships", shipService.getAllShips());
            model.addAttribute("ports", portService.getAllPorts());
            return "cruises/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Cruise not found");
            return "redirect:/cruises";
        });
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Cruise cruise, RedirectAttributes ra) {
        if (cruise.getCruiseId() != null) {
            cruiseService.updateCruise(cruise.getCruiseId(), cruise);
            ra.addFlashAttribute("success", "Cruise updated successfully.");
        } else {
            cruiseService.saveCruise(cruise);
            ra.addFlashAttribute("success", "Cruise created successfully.");
        }
        return "redirect:/cruises";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        cruiseService.deleteCruise(id);
        ra.addFlashAttribute("success", "Cruise deleted.");
        return "redirect:/cruises";
    }
}
