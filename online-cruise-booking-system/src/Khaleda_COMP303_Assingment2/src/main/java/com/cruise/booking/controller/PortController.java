package com.cruise.booking.controller;

import com.cruise.booking.entity.Port;
import com.cruise.booking.service.PortService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ports")
public class PortController {

    private final PortService portService;

    public PortController(PortService portService) {
        this.portService = portService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("ports", portService.getAllPorts());
        return "ports/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("port", new Port());
        return "ports/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return portService.getPortById(id).map(port -> {
            model.addAttribute("port", port);
            return "ports/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Port not found");
            return "redirect:/ports";
        });
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Port port, RedirectAttributes ra) {
        if (port.getPortId() != null) {
            portService.updatePort(port.getPortId(), port);
            ra.addFlashAttribute("success", "Port updated successfully.");
        } else {
            portService.savePort(port);
            ra.addFlashAttribute("success", "Port created successfully.");
        }
        return "redirect:/ports";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        portService.deletePort(id);
        ra.addFlashAttribute("success", "Port deleted.");
        return "redirect:/ports";
    }
}
