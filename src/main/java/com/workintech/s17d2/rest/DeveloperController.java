package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.model.Experience;
import com.workintech.s17d2.model.JuniorDeveloper;
import com.workintech.s17d2.model.MidDeveloper;
import com.workintech.s17d2.model.SeniorDeveloper;
import com.workintech.s17d2.tax.DeveloperTax;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    public Map<Integer, Developer> developers;

    private final Taxable taxable;

    public DeveloperController(DeveloperTax developerTax) {
        this.taxable = developerTax;
    }

    @PostConstruct
    public void init() {
        developers = new HashMap<>();
    }

    @GetMapping
    public List<Developer> getAllDevelopers() {
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable Integer id) {
        return developers.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Developer addDeveloper(@RequestBody Developer developer) {

        Double salary = developer.getSalary();

        if (developer.getExperience() == Experience.JUNIOR) {

            salary = salary - (salary * taxable.getSimpleTaxRate() / 100);

            JuniorDeveloper juniorDeveloper =
                    new JuniorDeveloper(
                            developer.getId(),
                            developer.getName(),
                            salary
                    );

            developers.put(juniorDeveloper.getId(), juniorDeveloper);

            return juniorDeveloper;
        }

        if (developer.getExperience() == Experience.MID) {

            salary = salary - (salary * taxable.getMiddleTaxRate() / 100);

            MidDeveloper midDeveloper =
                    new MidDeveloper(
                            developer.getId(),
                            developer.getName(),
                            salary
                    );

            developers.put(midDeveloper.getId(), midDeveloper);

            return midDeveloper;
        }

        salary = salary - (salary * taxable.getUpperTaxRate() / 100);

        SeniorDeveloper seniorDeveloper =
                new SeniorDeveloper(
                        developer.getId(),
                        developer.getName(),
                        salary
                );

        developers.put(seniorDeveloper.getId(), seniorDeveloper);

        return seniorDeveloper;
    }

    @PutMapping("/{id}")
    public Developer updateDeveloper(
            @PathVariable Integer id,
            @RequestBody Developer developer) {

        developers.put(id, developer);

        return developer;
    }

    @DeleteMapping("/{id}")
    public Developer deleteDeveloper(@PathVariable Integer id) {
        return developers.remove(id);
    }
}