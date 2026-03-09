package com.cruise.booking.controller;

import com.cruise.booking.entity.Ship;
import com.cruise.booking.service.FileStorageService;
import com.cruise.booking.service.ShipService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ships")
public class ShipController {

    private final ShipService shipService;
    private final FileStorageService fileStorageService;

    public ShipController(ShipService shipService, FileStorageService fileStorageService) {
        this.shipService = shipService;
        this.fileStorageService = fileStorageService;
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

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String save(@ModelAttribute Ship ship,
                       @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                       RedirectAttributes ra) {
        boolean isUpdate = ship.getShipId() != null;

        // For updates, preserve existing imageUrl if no new file is provided
        if (isUpdate && (imageFile == null || imageFile.isEmpty())) {
            shipService.getShipById(ship.getShipId()).ifPresent(existing ->
                    ship.setImageUrl(existing.getImageUrl()));
        }

        // Persist ship (create gets its generated ID here)
        Ship saved = isUpdate
                ? shipService.updateShip(ship.getShipId(), ship)
                : shipService.saveShip(ship);

        // Handle image upload after save so the ship ID is available for naming
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Delete old image when replacing
                if (saved.getImageUrl() != null) {
                    fileStorageService.deleteFile(saved.getImageUrl());
                }
                String imageUrl = fileStorageService.storeShipImage(imageFile, saved.getShipId());
                saved.setImageUrl(imageUrl);
                shipService.updateShip(saved.getShipId(), saved);
            } catch (Exception e) {
                ra.addFlashAttribute("warning", "Ship saved but image upload failed: " + e.getMessage());
                return "redirect:/ships";
            }
        }

        ra.addFlashAttribute("success", isUpdate ? "Ship updated successfully." : "Ship created successfully.");
        return "redirect:/ships";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        // Clean up image file before deleting the record
        shipService.getShipById(id).ifPresent(ship -> {
            if (ship.getImageUrl() != null) {
                fileStorageService.deleteFile(ship.getImageUrl());
            }
        });
        shipService.deleteShip(id);
        ra.addFlashAttribute("success", "Ship deleted.");
        return "redirect:/ships";
    }
}
