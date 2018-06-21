package org.ollide.fussifinder.controller;

import org.ollide.fussifinder.service.ZipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegionController {

    private final ZipService zipService;

    @Autowired
    public RegionController(ZipService zipService) {
        this.zipService = zipService;
    }

    @RequestMapping("/stadt")
    public String cityOverview(Model model) {
        model.addAttribute("cities", zipService.getCityOverview());
        return "city";
    }

    @RequestMapping("/kreis")
    public String districtOverview(Model model) {
        model.addAttribute("districts", zipService.getDistrictOverview());
        return "district";
    }

}
