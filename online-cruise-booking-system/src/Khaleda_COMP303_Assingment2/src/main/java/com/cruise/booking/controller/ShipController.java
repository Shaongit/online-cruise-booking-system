package com.cruise.booking.controller;

import com.cruise.booking.entity.Ship;
import com.cruise.booking.service.ShipService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ships")
public class ShipController {

    private final ShipService shipService;

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("ships", shipService.getAllShips());
        return "ships/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("ship", new Ship());
        return "ships/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return shipService.getShipById(id).map(ship -> {
            model.addAttribute("ship", ship);
            return "ships/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Ship not found");
            return "redirect:/ships";
        });
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Ship ship, RedirectAttributes ra) {
        if (ship.getShipId() != null) {
            shipService.updateShip(ship.getShipId(), ship);
            ra.addFlashAttribute("success", "Ship updated successfully.");
        } else {
            shipService.saveShip(ship);
            ra.addFlashAttribute("success", "Ship created successfully.");
        }
        return "redirect:/ships";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        shipService.deleteShip(id);
        ra.addFlashAttribute("success", "Ship deleted.");
        return "redirect:/ships";
    }
}
